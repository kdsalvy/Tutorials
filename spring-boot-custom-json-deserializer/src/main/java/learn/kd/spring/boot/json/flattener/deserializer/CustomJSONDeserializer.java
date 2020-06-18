package learn.kd.spring.boot.json.flattener.deserializer;

import java.io.IOException;
import java.util.Map;

import org.springframework.boot.json.JsonParserFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import learn.kd.spring.boot.json.flattener.pojo.ModelClass;

public class CustomJSONDeserializer extends StdDeserializer<ModelClass> {

    private static final long serialVersionUID = 2157952445642587900L;

    public CustomJSONDeserializer(Class<ModelClass> modelClass) {
        super(modelClass);
    }

    public CustomJSONDeserializer() {
        this(ModelClass.class);
    }

    @Override
    public ModelClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        org.springframework.boot.json.JsonParser springJsonParser = JsonParserFactory.getJsonParser();
        Map<String, Object> rootMap = springJsonParser.parseMap(p.getValueAsString());
        Map<String, Object> headMap = (Map<String, Object>) rootMap.get("head");
        Map<String, Object> dataMap = (Map<String, Object>) headMap.get("data");
        Map<String, Object> complexKeyMap = (Map<String, Object>) dataMap.get("complex-key");
        ModelClass obj = new ModelClass();
        obj.setValue((String) dataMap.get("key"));
        obj.setSubValue((String) complexKeyMap.get("subkey"));
        //
        return obj;

    }

}
