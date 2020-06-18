package learn.kd.spring.boot.json.flattener.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelWithInnerClass {

    @JsonIgnore
    ObjectMapper mapper;

    @JsonProperty(value = "head")
    private HeadInnerClass head;

    @JsonCreator(mode = Mode.DEFAULT)
    public ModelWithInnerClass() {
        mapper = new ObjectMapper();
    }

    @JsonRootName(value = "head")
    static class HeadInnerClass {
        @JsonProperty(value = "data")
        List<DataInnerClass> dataList;

        @JsonCreator(mode = Mode.DEFAULT)
        public HeadInnerClass() {
        }

        @Override
        public String toString() {
            return "HeadInnerClass [data=" + dataList + "]";
        }
    }

    @JsonRootName("data")
    static class DataInnerClass {
        @JsonProperty(value = "key")
        String value;

        @JsonProperty(value = "complex-key")
        ComplexKey complexKey;

        @JsonCreator(mode = Mode.DEFAULT)
        public DataInnerClass() {
        }

        @Override
        public String toString() {
            return "DataInnerClass [value=" + value + ", complexKey=" + complexKey + "]";
        }
    }

    @JsonRootName("complex-key")
    static class ComplexKey {
        @JsonProperty(value = "subkey")
        String subValue;

        @JsonCreator(mode = Mode.DEFAULT)
        public ComplexKey() {
        }

        @Override
        public String toString() {
            return "ComplexKey [subValue=" + subValue + "]";
        }
    }

    @Override
    public String toString() {
        return "ModelWithInnerClass [head=" + head + "]";
    }

    public HeadInnerClass getHead() {
        return head;
    }

    public void setHead(HeadInnerClass head) {
        this.head = head;
    }
}
