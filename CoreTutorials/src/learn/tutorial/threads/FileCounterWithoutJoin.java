package demo.files.list.thread.itr;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class FileCounterWithoutJoin {

    public static void main(String[] args) throws InterruptedException {
	String startDirPath = "D:\\threadDemo";
	ThreadCounter tc = new ThreadCounter();
	tc.dirPath = startDirPath;
	tc.val = new AtomicInteger();
	tc.start();
    }

    static class ThreadCounter extends Thread {
	public String dirPath = "";
	private AtomicInteger val = null;

	@Override
	public void run() {
	    File[] files = Paths.get(dirPath).toFile().listFiles();
	    for (File file : files) {
		if (file.isDirectory()) {
		    ThreadCounter tc = new ThreadCounter();
		    tc.dirPath = file.getAbsolutePath();
		    tc.val = this.val;
		    tc.start();
		} else {
		    val.incrementAndGet();
		    System.out.println(val.get() + ": " + file.getName());
		}
	    }
	}
    }
}
