package com.sap.cloud.lm.sl.cf.core.validators.parameters.v1_0;

import java.util.Map;

import com.sap.cloud.lm.sl.cf.core.validators.parameters.ParametersValidator;
import com.sap.cloud.lm.sl.cf.core.validators.parameters.ParametersValidatorHelper;
import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.mta.model.v1_0.Resource;

public class ResourceParametersValidator extends ParametersValidator<Resource> {

    protected Resource resource;

    public ResourceParametersValidator(Resource resource, ParametersValidatorHelper helper) {
        super("", resource.getName(), helper, Resource.class);
        this.resource = resource;
    }

    @Override
    public Resource validate() throws SLException {
        Map<String, Object> properties = validateParameters(resource, resource.getProperties());
        resource.setProperties(properties);
        return resource;
    }

}
