package demo.files.list.thread.itr;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

public class FileCounterWithLog {

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
	    Path path = Paths.get(dirPath);
	    Stream<Path> pathStream = null;
	    try {
		pathStream = Files.list(path);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    Iterator<Path> dirIterator = pathStream.iterator();
	    while (dirIterator.hasNext()) {
		Path curPath = dirIterator.next();
		if (Files.isDirectory(curPath)) {
		    ThreadCounter tc = new ThreadCounter();
		    tc.dirPath = curPath.toString();
		    tc.start();
		} else {
		    synchronized (_LOCK) {
			val++;
			System.out.println(val + ": " + curPath);
			try {
			    Logger.getInstance().write(val + ": " + curPath + "\n");
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	    try {
		// Internally FileWriter buffers the data to write it in big
		// blocks which was causing some data to be truncated since
		// all the threads completed their execution. Thus to avoid
		// that, flushing the buffer content to the file just before
		// a threads completes its execution. Using the same object in
		// synchronized block to avoid thread conflict while writing.
		synchronized (_LOCK) {
		    Logger.getInstance().flush();
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}

class Logger {
    private static String path = "D:\\Logs\\threadLog.txt";
    private static final Object _LOCK = new Object();
    private static FileWriter writer = null;
    private static Logger log = null;

    private Logger() throws IOException {
	writer = new FileWriter(Paths.get(path).toFile());
    }

    public static Logger getInstance() throws IOException {
	if (log == null)
	    synchronized (_LOCK) {
		if (log == null)
		    log = new Logger();
	    }
	return log;
    }

    public void write(String string) throws IOException {
	writer.append(string);
    }

    public void flush() throws IOException {
	writer.flush();
    }
}
