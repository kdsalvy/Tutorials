package implementation.optional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class OptionalDemo {

    public static void main(String[] args) {
        Optional<String> optionalString = returnOptionalString();
        String[] stringArray = optionalString.map(string -> string.split(","))
            .orElse(new String[0]);
        Consumer<String> printString = string -> System.out.println(string);
        Stream.of(stringArray).forEach(printString);
        
        List<String> list = null;
        System.out.println(Objects.toString(list));
    }

    public static Optional<String> returnOptionalString() {
        return Optional.ofNullable(null);
    }
}
