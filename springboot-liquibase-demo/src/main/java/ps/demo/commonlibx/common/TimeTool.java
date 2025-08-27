package ps.demo.commonlibx.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeTool {

    //public final static String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    //public static final String DATE_FORMAT_STR_DATETTIMESSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT_STR_DATETTIMESSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DATE_FORMAT_STR_DATETTIME = "yyyy-MM-dd'T'HH:mm:ss";
    public final static String DATE_FORMAT_STR_PLAIN_DATE = "yyyy-MM-dd";

    public static String getNowStr() {
        return getNowStr("yyyy-MM-dd_HHmmss");
    }

    public static String getNowDateOnly() {
        return getNowStr("yyyy-MM-dd");
    }

    public static String getNowTimeOnly() {
        return getNowStr("HH:mm:ss");
    }

    public static String getNowStryMdTHmsS() {
        return getNowStr(DATE_FORMAT_STR_DATETTIMESSS);
    }

    public String getNowStryMdTHms() {
        return getNowStr(DATE_FORMAT_STR_DATETTIME);
    }

    public String getNowStryMd() {
        return getNowStr(DATE_FORMAT_STR_PLAIN_DATE);
    }

    public static String getNowStr(String pattern) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }


    public static Date toDate(String dateStr, String pattern) {
        try {
            Date date = new SimpleDateFormat(pattern).parse(dateStr);
            return date;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static long subtractDays(Date date1, Date data2) {
        long diff = date1.getTime() - data2.getTime();
        TimeUnit time = TimeUnit.DAYS;
        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
        return diffrence;
    }

    public static long subtractHours(Date date1, Date data2) {
        long diff = date1.getTime() - data2.getTime();
        TimeUnit time = TimeUnit.HOURS;
        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
        return diffrence;
    }

    public static long subtractMinutes(Date date1, Date data2) {
        long diff = date1.getTime() - data2.getTime();
        TimeUnit time = TimeUnit.MINUTES;
        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
        return diffrence;
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

//    public static Date toDate(String dateStr, String pattern) {
//        java.time.format.DateTimeFormatter dateTimeFormatter =
//                java.time.format.DateTimeFormatter.ofPattern(pattern);
//        Instant instant = Instant.from(dateTimeFormatter.parse(dateStr));
//        return Date.from(instant);
//
//    }

}
