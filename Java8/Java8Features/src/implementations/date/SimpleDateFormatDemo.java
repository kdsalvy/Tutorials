package implementations.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatDemo {

    public static void main(String... args) throws ParseException {
        String dateStr = "2020-04-27T12:48:29.000Z";
        String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";

        String dateExpStr = "2020-04-27 12:48:29";
        String datePatternExp = "yyyy-MM-dd HH:mm:ss";
        
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        Date date = sdf.parse(dateStr);
//        sdf.applyPattern(datePattern);
//        String origDateStr = sdf.format(date);
        System.out.println(date);
    }
}
