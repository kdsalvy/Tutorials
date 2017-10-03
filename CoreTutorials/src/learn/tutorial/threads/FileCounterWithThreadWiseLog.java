package demo.files.list.thread.itr;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class FileCounterWithThreadWiseLog {

    public static void main(String[] args) throws InterruptedException {
	String startDirPath = "D:\\threadDemo";
	ThreadCounter tc = new ThreadCounter();
	tc.dirPath = startDirPath;
	tc.start();
    }

    static class ThreadCounter extends Thread {
	public String dirPath = "";
	private static int val;
	private static final Object _LOCK = new Object();
	private static ThreadLogBuffer buffer = ThreadLogBuffer.getInstance();

	@Override
	public void run() {
	    Path path = Paths.get(dirPath);
	    Stream<Path> pathStream = null;
	    try {
		pathStream = Files.list(path);
	    } catch (IOException e1) {
		e1.printStackTrace();
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
			System.out.println(Thread.currentThread().getName() + ": " + val + ": " + curPath);
			buffer.writeToBuffer(Thread.currentThread().getName(),
				Thread.currentThread().getName() + ": " + val + ": " + curPath + "\n");
		    }
		}
	    }
	    try {
		synchronized (_LOCK) {
		    buffer.writeToFile(Thread.currentThread().getName());
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}

class FileLogger {
    private static String path = "D:\\Logs\\threadLog.txt";
    private static final Object _LOCK = new Object();
    private static FileWriter writer = null;
    private static FileLogger log = null;

    private FileLogger() throws IOException {
	writer = new FileWriter(Paths.get(path).toFile());
    }

    public static FileLogger getInstance() throws IOException {
	if (log == null)
	    synchronized (_LOCK) {
		if (log == null)
		    log = new FileLogger();
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

class ThreadLogBuffer {
    private static ThreadLogBuffer buffer = null;
    private static final Object _LOCK = new Object();
    private static Map<String, String> threadLogMap = null;

    private ThreadLogBuffer() {
	threadLogMap = new HashMap<>();
    }

    public static ThreadLogBuffer getInstance() {
	if (buffer == null)
	    synchronized (_LOCK) {
		if (buffer == null)
		    buffer = new ThreadLogBuffer();
	    }
	return buffer;
    }

    public void writeToBuffer(String key, String value) {
	String temp = value;
	if (threadLogMap.containsKey(key))
	    temp = threadLogMap.get(key) + value;
	threadLogMap.put(key, temp);
    }

    public void writeToFile(String key) throws IOException {
	FileLogger.getInstance().write(threadLogMap.get(key) == null ? key + "\n" : threadLogMap.get(key) + "\n");
	FileLogger.getInstance().flush();
    }
}
