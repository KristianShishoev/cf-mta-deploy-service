package com.sap.cloud.lm.sl.cf.core.dto.serialization.v2;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.sap.cloud.lm.sl.common.model.json.PropertiesAdapterFactory;
import com.sap.cloud.lm.sl.common.model.xml.PropertiesAdapter;
import com.sap.cloud.lm.sl.mta.model.v2_0.TargetResourceType;

public class DeployTargetResourceTypeDto {

    @Expose
    @XmlElement
    protected String name;
    @Expose
    @JsonAdapter(PropertiesAdapterFactory.class)
    @XmlJavaTypeAdapter(PropertiesAdapter.class)
    protected Map<String, Object> parameters;

    protected DeployTargetResourceTypeDto() {
        // Required by JAXB
    }

    public DeployTargetResourceTypeDto(TargetResourceType resourceType) {
        name = resourceType.getName();
        parameters = resourceType.getParameters();
    }

    public TargetResourceType toTargetResourceType() {
        TargetResourceType.Builder result = new TargetResourceType.Builder();
        result.setName(name);
        result.setParameters(parameters);
        return result.build();
    }

}
