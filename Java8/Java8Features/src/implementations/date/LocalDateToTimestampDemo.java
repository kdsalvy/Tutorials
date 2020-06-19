package implementations.date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class LocalDateToTimestampDemo {

    public static void main(String[] args) throws ParseException {
        Date date = new Date();
        System.out.println(date);
        System.out.println(Timestamp.valueOf(date.toInstant()
            .atZone(ZoneId.of("GMT"))
            .toLocalDateTime()));

        LocalDate ld = date.toInstant()
            .atZone(ZoneId.of("UTC"))
            .toLocalDateTime()
            .toLocalDate();
        
        Instant instant = ld.atStartOfDay(TimeZone.getTimeZone("America/New_York")
            .toZoneId())
            .toInstant();
        
        System.out.println(ld);
        System.out.println(instant);
        System.out.println(Timestamp.from(instant));
        
        // Save Part Logic is correct
        
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("MM/dd/yyyy HH:mm:ss z");
        String dateStr = "03/11/2020 00:40:45 EST";
        Date dateObj = sdf.parse(dateStr);
        System.out.println(dateObj);
        sdf.applyPattern("dd-MMM-yyyy hh.mm.ss a");
        String executionTSStr = sdf.format(dateObj);
        System.out.println(executionTSStr);
        sdf.applyPattern("dd-MMM-yyyy hh.mm.ss a");
        Date executionTSObj = sdf.parse(executionTSStr);
        System.out.println(new Timestamp(executionTSObj.getTime()));
        
        // Fetch Part Logic
        
        LocalDate fetchDate = LocalDate.parse("2020-03-11");
        System.out.println(fetchDate);
        Instant startOfTheDay = fetchDate.atStartOfDay(ZoneId.of("America/New_York")).toInstant();
        System.out.println(startOfTheDay);
        Instant endOfTheDay = fetchDate.atTime(LocalTime.MAX).atZone(ZoneId.of("America/New_York")).toInstant();
        System.out.println(endOfTheDay);
        
        System.out.println(Timestamp.from(startOfTheDay));
        System.out.println(Timestamp.from(endOfTheDay));

    }

}
