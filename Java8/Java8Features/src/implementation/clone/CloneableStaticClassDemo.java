package implementation.clone;

public class CloneableStaticClassDemo {

    public static void main(String[] args) {
        CloneableStaticClassDemo demo = new CloneableStaticClassDemo();
        NestedStaticClass nsc1 = new NestedStaticClass();
        NestedStaticClass nsc2 = null;
        try {
            nsc2 = (NestedStaticClass) nsc1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println(demo);
        System.out.println(nsc1);
        System.out.println(nsc2);
    }

    public static class NestedStaticClass implements Cloneable {

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

}
