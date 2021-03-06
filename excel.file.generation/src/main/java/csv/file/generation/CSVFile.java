package csv.file.generation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVFile {

    public static List<List<String>> readCSV(String filePath) throws FileNotFoundException, IOException {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.substring(1, line.length() - 1);
                String[] values = line.split("\",\"");
                records.add(Arrays.asList(values));
            }
        }
        return records;
    }

    public static void appendToCSV(String filePath, List<String> headers, List<String> values) throws IOException {
        File csvOutputFile = new File(filePath);
        List<List<String>> records = new ArrayList<>();

        if (!csvOutputFile.exists())
            csvOutputFile.createNewFile();
        else
            records.addAll(readCSV(filePath));
        records.add(headers);
        records.add(values);

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            records.stream()
                .map(CSVFile::convertToCSV)
                .forEach(pw::println);
        }
    }

    /** 
     * Handles value that contains comma
     * @param data
     * @return
     */
    public static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        escapedData = "\"" + data + "\"";
        return escapedData;
    }

    public static String convertToCSV(List<String> data) {
        return data.stream()
            .map(CSVFile::escapeSpecialCharacters)
            .collect(Collectors.joining(","));
    }

    public static void main(String... args) {
        /*Arrays.asList("Apple", "Aeroplane", "Axe");
        Arrays.asList("Boy", "Ball", "Balloon");
        Arrays.asList("Cat", "Car", "Comb");
        Arrays.asList("Dog", "Dart", "Deer");*/
        try {
            appendToCSV("./demo.csv", Arrays.asList("A", "B,", "C"), Arrays.asList("Axe", "[Balloon,Ball,Boy]", "Comb"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
