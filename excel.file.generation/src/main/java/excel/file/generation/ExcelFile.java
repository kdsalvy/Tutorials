package excel.file.generation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Checks for file at given path, if exists then appends the data to the given sheet.<br/>
 * If sheet doesn't exists then creates a new sheet with headers and enter data in that sheet.<br/>
 * If file doesn't exists then creates a new file and add a sheet with given name and headers to it.<br/>
 * @author saukedia1
 *
 */
public class ExcelFile {

    public static void append(String fileLocation, String sheetName, List<String> cellValues, List<String> headerNames) throws IOException {
        createWorkbookIfNotExists(fileLocation, sheetName, headerNames);
        Workbook workbook = null;
        try (InputStream is = new FileInputStream(Paths.get(fileLocation)
            .toFile())) {
            workbook = new XSSFWorkbook(is);
            Sheet sheet = getSheet(sheetName, workbook, headerNames);
            Row row = getLastRow(sheet);
            setCellData(row, cellValues);
        }
        try (OutputStream os = new FileOutputStream(Paths.get(fileLocation)
            .toFile())) {
            workbook.write(os);
        }

    }

    private static void setCellData(Row row, List<String> cellValues) {
        int cellCount = cellValues.size();
        IntStream.range(0, cellCount)
            .forEach(i -> {
                Cell cell = row.createCell(i);
                cell.setCellValue(cellValues.get(i));
            });
    }

    private static Row getLastRow(Sheet sheet) {
        Iterator<Row> rowItr = sheet.rowIterator();
        int i = 0;
        while (rowItr.hasNext()) {
            rowItr.next();
            i++;
        }
        Row row = sheet.createRow(i);
        return row;
    }

    private static Sheet getSheet(String sheetName, Workbook workbook, List<String> headerNames) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (Objects.isNull(sheet)) {
            sheet = workbook.createSheet(sheetName);
            Row row = getLastRow(sheet);
            setCellData(row, headerNames);
        }
        return sheet;
    }

    private static void createWorkbookIfNotExists(String fileLocation, String sheetName, List<String> headerNames) throws IOException {
        Workbook workbook = null;
        if (!Files.exists(Paths.get(fileLocation))) {
            try (OutputStream fos = new FileOutputStream(Paths.get(fileLocation)
                .toFile())) {
                workbook = new XSSFWorkbook();
                getSheet(sheetName, workbook, headerNames);
                workbook.write(fos);
            }
        }
    }

    public static void main(String... args) {
        /*Arrays.asList("Apple", "Aeroplane", "Axe");
        Arrays.asList("Boy", "Ball", "Balloon");
        Arrays.asList("Cat", "Car", "Comb");
        Arrays.asList("Dog", "Dart", "Deer");*/
        try {
            append("./demo.xlsx", "sheet2", Arrays.asList("Axe", "Balloon", "Comb", "Deer"), Arrays.asList("A", "B", "C", "D"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
