package demo.files.list.thread.itr;

import java.io.File;
import java.nio.file.Paths;

public class FileCounterWithoutConcurrentUtils {

    public static void main(String[] args) throws InterruptedException {
	String startDirPath = "D:\\threadDemo";
	ThreadCounter tc = new ThreadCounter();
	tc.dirPath = startDirPath;
	ThreadCounter.val = 0;
	ThreadCounter._LOCK = new Object();
	tc.start();
    }

    static class ThreadCounter extends Thread {
	public String dirPath = "";
	private static int val;
	private static Object _LOCK = null;

	@Override
	public void run() {
	    File[] files = Paths.get(dirPath).toFile().listFiles();
	    for (File file : files) {
		if (file.isDirectory()) {
		    ThreadCounter tc = new ThreadCounter();
		    tc.dirPath = file.getAbsolutePath();
		    tc.start();
		} else {
		    synchronized (_LOCK) {
			val++;
			System.out.println(val + ": " + file.getName());
		    }
		}
	    }
	}
    }
}
