package implementations.collection;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamListToReturnMap {

    public static void main(String[] args) {
        List<C> cList = Arrays.asList(new C(1), new C(2), new C(3));
        List<B> bList = Arrays.asList(new B("1", cList), new B("2", cList), new B("3", cList), new B("4", cList));
        List<A> aList = Arrays.asList(new A(1, bList), new A(2, bList), new A(3, bList), new A(4, bList));

        Map<A, List<B>> result1 = convertCompositeObjectListToMap(aList);
        Set<B> result2 = convertMapOfListToSet(result1);

        Map<B, List<C>> result3 = result2.stream()
            .collect(Collectors.toMap(Function.identity(), b -> b.cList));
        System.out.println(result3);

        Set<C> result4 = result3.values()
            .stream()
            .flatMap(list -> list.stream())
            .collect(Collectors.toSet());
        System.out.println(result4);
    }

    public static Map<A, List<B>> convertCompositeObjectListToMap(List<A> aList) {
        // list to map
        Map<A, List<B>> aBListMap = aList.stream()
            .collect(Collectors.toMap(Function.identity(), a -> a.getbList()));

        System.out.println(aBListMap);
        return aBListMap;
    }

    public static Set<B> convertMapOfListToSet(Map<A, List<B>> abList) {
        Set<B> bSet = abList.values()
            .stream()
            .flatMap(list -> list.stream())
            .collect(Collectors.toSet());

        System.out.println(bSet);
        return bSet;
    }
}

class A {
    private Integer i;
    private List<B> bList;

    public A(int j, List<B> bL) {
        i = j;
        bList = bL;
    }

    @Override
    public String toString() {
        return "A [i=" + i + ", bList=" + bList + "]";
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public List<B> getbList() {
        return bList;
    }

    public void setbList(List<B> bList) {
        this.bList = bList;
    }

}

class B {

    String name;
    List<C> cList;

    public B(String n, List<C> c) {
        name = n;
        cList = c;
    }

    @Override
    public String toString() {
        return "B [name=" + name + ", cList=" + cList + "]";
    }

}

class C {

    Integer j;

    public C(int i) {
        j = i;
    }

    @Override
    public String toString() {
        return "C [j=" + j + "]";
    }
}