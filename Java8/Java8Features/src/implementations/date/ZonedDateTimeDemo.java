package implementations.date;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public class ZonedDateTimeDemo {
    
    public static void main(String[] args) {
       printZonedDateTimeInReadableFormat();
    }
    
    public static void printZonedDateTimeInReadableFormat() {
        ZonedDateTime zdt = Instant.now().atZone(TimeZone.getTimeZone("America/New_York").toZoneId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss zzz", Locale.US);
        System.out.println(zdt.format(formatter));
    }

}
