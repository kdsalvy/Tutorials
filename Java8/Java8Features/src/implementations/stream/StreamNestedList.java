package implementations.stream;

import java.util.Arrays;
import java.util.List;

public class StreamNestedList {

    public static void main(String[] args) {
        List<AN> aList = Arrays.asList(new AN(), new AN());

        aList.stream()
            .forEach(a -> {
                List<BN> bList = serviceReturningListBN();
                a.bList = bList;
            });

        aList.stream()
            .forEach(a -> {
                a.bList.stream()
                    .forEach( b -> System.out.println(b.a));
                ;
            });
    }

    public static List<BN> serviceReturningListBN() {
        return Arrays.asList(new BN(), new BN(), new BN());
    }

}

class AN {
    List<BN> bList;
}

class BN {
    String a = "Hello World";
}
