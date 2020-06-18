package learn.kd.spring.boot.json.flattener.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import learn.kd.spring.boot.json.flattener.deserializer.CustomJSONDeserializer;

@JsonDeserialize(using = CustomJSONDeserializer.class)
public class ModelClass {

    private String value;
    private String subValue;

    public ModelClass() {
    }

    public ModelClass(String key, String value, String subKey, String subValue) {
        this.value = value;
        this.subValue = subValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSubValue() {
        return subValue;
    }

    public void setSubValue(String subValue) {
        this.subValue = subValue;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "ModelClass [value=" + value + ", subValue=" + subValue + "]";
    }
}
