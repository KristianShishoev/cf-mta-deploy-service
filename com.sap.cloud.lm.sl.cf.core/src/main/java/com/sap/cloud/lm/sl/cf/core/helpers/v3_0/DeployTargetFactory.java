package com.sap.cloud.lm.sl.cf.core.helpers.v3_0;

import com.sap.cloud.lm.sl.mta.model.v3_0.Target;

public class DeployTargetFactory extends com.sap.cloud.lm.sl.cf.core.helpers.v2_0.DeployTargetFactory {

    @Override
    public Target create(String org, String space, String targetType) {
        Target.Builder builder = new Target.Builder();
        builder.setType(targetType);
        builder.setParameters(createImplicitTargetProperties(org, space));
        return builder.build();
    }

}
