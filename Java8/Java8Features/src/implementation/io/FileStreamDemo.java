package implementation.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileStreamDemo {

    public static void main(String[] args) {
        try (Stream<String> fileStream = Files.lines(Paths.get("C:\\Users\\saukedia1\\Desktop\\POCFolder\\file.txt"))) {
            fileStream.forEach(System.out::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
