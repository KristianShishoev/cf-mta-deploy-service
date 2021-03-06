package com.sap.cloud.lm.sl.cf.core.dto.serialization.v3;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.sap.cloud.lm.sl.common.model.json.PropertiesAdapterFactory;
import com.sap.cloud.lm.sl.common.model.xml.PropertiesAdapter;
import com.sap.cloud.lm.sl.mta.model.v3_1.TargetModuleType;

public class DeployTargetModuleTypeDto {

    @Expose
    @XmlElement
    private String name;
    @Expose
    @JsonAdapter(PropertiesAdapterFactory.class)
    @XmlJavaTypeAdapter(PropertiesAdapter.class)
    private Map<String, Object> properties;
    @Expose
    @JsonAdapter(PropertiesAdapterFactory.class)
    @XmlJavaTypeAdapter(PropertiesAdapter.class)
    private Map<String, Object> parameters;

    protected DeployTargetModuleTypeDto() {
        // Required by JAXB
    }

    public DeployTargetModuleTypeDto(TargetModuleType moduleType) {
        this.name = moduleType.getName();
        this.properties = moduleType.getProperties();
        this.parameters = moduleType.getParameters();
    }

    public TargetModuleType toTargetModuleType() {
        TargetModuleType.Builder result = new TargetModuleType.Builder();
        result.setName(name);
        result.setProperties(properties);
        result.setParameters(parameters);
        return result.build();
    }

}
