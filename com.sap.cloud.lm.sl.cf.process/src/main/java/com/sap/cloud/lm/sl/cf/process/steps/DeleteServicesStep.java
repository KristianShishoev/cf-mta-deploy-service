package com.sap.cloud.lm.sl.cf.process.steps;

import java.util.List;

import org.cloudfoundry.client.lib.CloudControllerException;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.lib.ServiceBrokerException;
import org.cloudfoundry.client.lib.domain.CloudServiceBinding;
import org.cloudfoundry.client.lib.domain.CloudServiceInstance;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sap.cloud.lm.sl.cf.core.security.serialization.SecureSerializationFacade;
import com.sap.cloud.lm.sl.cf.process.message.Messages;
import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.common.util.CommonUtil;

@Component("deleteServicesStep")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DeleteServicesStep extends SyncActivitiStep {

    private SecureSerializationFacade secureSerializer = new SecureSerializationFacade();

    @Override
    protected StepPhase executeStep(ExecutionWrapper execution) throws SLException {
        try {
            getStepLogger().info(Messages.DELETING_SERVICES);

            CloudFoundryOperations client = execution.getCloudFoundryClient();

            List<String> servicesToDelete = StepsUtil.getServicesToDelete(execution.getContext());
            deleteServices(client, servicesToDelete);

            getStepLogger().debug(Messages.SERVICES_DELETED);
            return StepPhase.DONE;
        } catch (SLException e) {
            getStepLogger().error(e, Messages.ERROR_DELETING_SERVICES);
            throw e;
        } catch (CloudFoundryException cfe) {
            CloudControllerException e = new CloudControllerException(cfe);
            getStepLogger().error(e, Messages.ERROR_DELETING_SERVICES);
            throw e;
        }
    }

    private void deleteServices(CloudFoundryOperations client, List<String> serviceNames) {
        for (String serviceName : serviceNames) {
            deleteService(client, serviceName);
        }
    }

    private void deleteService(CloudFoundryOperations client, String serviceName) {
        try {
            attemptToDeleteService(client, serviceName);
        } catch (CloudFoundryException e) {
            switch (e.getStatusCode()) {
                case NOT_FOUND:
                    getStepLogger().warn(e, Messages.COULD_NOT_DELETE_SERVICE, serviceName);
                    break;
                case BAD_GATEWAY:
                    throw new ServiceBrokerException(e);
                default:
                    throw e;
            }
        }
    }

    private void attemptToDeleteService(CloudFoundryOperations client, String serviceName) {
        CloudServiceInstance serviceInstance = client.getServiceInstance(serviceName);
        List<CloudServiceBinding> bindings = serviceInstance.getBindings();
        if (!CommonUtil.isNullOrEmpty(bindings)) {
            logBindings(bindings);
            getStepLogger().info(Messages.SERVICE_HAS_BINDINGS_AND_CANNOT_BE_DELETED, serviceName);
            return;
        }

        getStepLogger().info(Messages.DELETING_SERVICE, serviceName);
        client.deleteService(serviceName);
        getStepLogger().debug(Messages.SERVICE_DELETED, serviceName);
    }

    private void logBindings(List<CloudServiceBinding> bindings) {
        getStepLogger().debug(Messages.SERVICE_BINDINGS_EXISTS, secureSerializer.toJson(bindings));
    }

}
