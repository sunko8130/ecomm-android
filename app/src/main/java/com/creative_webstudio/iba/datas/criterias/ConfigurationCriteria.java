package com.creative_webstudio.iba.datas.criterias;

import java.lang.reflect.Array;

public class ConfigurationCriteria {

    private String name;
    private String key;
    private String value;

    public ConfigurationCriteria() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
