package implementations.stream;

import java.util.Arrays;

public class StreamReduce {

    public static void main(String[] args) {
        // reduce stream to check if it contains any true boolean value in the array
        Boolean[] booleans = { false, false, true, false };
        // result will be true if any true value is present
        Arrays.stream(booleans).reduce((r, e) -> r ^ e).ifPresent(System.out::println);
        
        Boolean result = Arrays.stream(booleans).filter(e -> e != false).count() > 0? true : false;
        System.out.println(result);
    }

}
