package implementations.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadJoinDemo {

    public static void main(String[] args) throws InterruptedException {
        A a = new A();
        List<Future<String>> futureStringList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ExecutorService es = Executors.newSingleThreadExecutor();
            futureStringList.add(es.submit(a));
        }
    }
}

class A implements Callable<String> {

    @Override
    public String call() {
        try {
            System.out.println(Thread.currentThread() + " :: Spawned");
            Thread.sleep(5000);
            System.out.println(Thread.currentThread() + " :: Exit");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Successfully Executed";
    }

}
