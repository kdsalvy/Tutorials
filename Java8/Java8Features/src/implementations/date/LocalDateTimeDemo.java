package implementations.date;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateTimeDemo {

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yy hh.mm.ss.S a");
        String sodStr = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
            .toLocalDate()
            .atStartOfDay()
            .format(dtf);
        LocalDateTime sod = LocalDateTime.parse(sodStr, dtf);

        String eodStr = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
            .toLocalDate()
            .atTime(LocalTime.MAX)
            .format(dtf);
        LocalDateTime eod = LocalDateTime.parse(eodStr, dtf);

        System.out.println(sodStr);
        System.out.println(eodStr);
        System.out.println(sod);
        System.out.println(eod);

    }

}
