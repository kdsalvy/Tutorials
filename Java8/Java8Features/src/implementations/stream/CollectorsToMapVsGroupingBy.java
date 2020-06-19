package implementations.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectorsToMapVsGroupingBy {

    public static void main(String[] args) {
        List<KeyValueClass> kvList = Arrays.asList(
            new KeyValueClass("A", "randomA1"),
            new KeyValueClass("B", "randomB1"),
            new KeyValueClass("B", "randomB2"),
            new KeyValueClass("C", "randomC1"),
            new KeyValueClass("C", "randomC2"),
            new KeyValueClass("D", "randomD1"));
        
        Map<String, List<KeyValueClass>> kvGroupedByKey = 
            kvList.stream().collect(
                Collectors.groupingBy(
                    KeyValueClass::getKey));
        
        System.out.println(kvGroupedByKey);
        
//         This will give exception as we have duplicate keys in our example
        Map<String, String> kvMap = kvList.stream()
            .collect(
                Collectors.toMap(
                    KeyValueClass::getKey, KeyValueClass::getValue));

        System.out.println(kvMap);
        
    }

}

class KeyValueClass {
    private String key;
    private String value;
    public KeyValueClass(String key, String value) {
        this.key = key;
        this.value = value;
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
    @Override
    public String toString() {
        return "KeyValueClass [key=" + key + ", value=" + value + "]";
    }
}
