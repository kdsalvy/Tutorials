package learn.kd.spring.boot.json.flattener.handler;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import learn.kd.spring.boot.json.flattener.pojo.ModelClass;
import learn.kd.spring.boot.json.flattener.pojo.ModelClassForJsonQuery;
import learn.kd.spring.boot.json.flattener.pojo.ModelWithInnerClass;

@Component
public class ComplexToSimplePojo {

    private static String jsonString = "{ \"head\" : { \"data\" : { \"key\":\"value\", \"complex-key\":{\"subkey\":\"subvalue\"}} } }";
    
    private static String jsonString2 = "{ \"head\" : { \"data\" : [ { \"key\":\"value\", \"complex-key\":{\"subkey\":\"subvalue\"} } ] } }";
    

    public static void main(String... args) throws JsonMappingException, JsonProcessingException {
        JsonMapper mapper = new JsonMapper();
        ModelClass model = mapper.convertValue(jsonString, ModelClass.class);

        System.out.println(model);

        ModelWithInnerClass modelIC = mapper.readValue(jsonString2, ModelWithInnerClass.class);
        System.out.println(modelIC);
        
        ModelClassForJsonQuery modelJQ = mapper.convertValue(jsonString2, ModelClassForJsonQuery.class);
        System.out.println(modelJQ);
        
        System.out.println(mapper.writeValueAsString(modelIC));
    }
}
