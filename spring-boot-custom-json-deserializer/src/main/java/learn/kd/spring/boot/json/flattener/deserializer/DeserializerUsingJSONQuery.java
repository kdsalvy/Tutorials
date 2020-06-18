package learn.kd.spring.boot.json.flattener.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jayway.jsonpath.JsonPath;

import learn.kd.spring.boot.json.flattener.pojo.ModelClassForJsonQuery;

public class DeserializerUsingJSONQuery extends StdDeserializer<ModelClassForJsonQuery> {

    private static final long serialVersionUID = 1L;

    public DeserializerUsingJSONQuery(Class<ModelClassForJsonQuery> vc) {
        super(vc);
    }
    
    public DeserializerUsingJSONQuery() {
        this(null);
    }

    @Override
    public ModelClassForJsonQuery deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String jsonStr = p.getValueAsString();
        // To avoid index hardcoding iterate over the array elements. For simplifying the problem
        // I have used hardcoding
        // JSONArray array = JsonPath.read(jsonStr, "$.head.data");
        // int arrSize = array.size();
        String value = JsonPath.read(jsonStr, "$.head.data[0].key");
        String subValue = JsonPath.read(jsonStr, "$.head.data[0].complex-key.subkey");
        ModelClassForJsonQuery modelClass = new ModelClassForJsonQuery(value, subValue);
        return modelClass;
    }

    
}
