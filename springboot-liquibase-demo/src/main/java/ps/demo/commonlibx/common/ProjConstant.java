package ps.demo.commonlibx.common;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class ProjConstant {

    public final static String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final String DATE_FORMAT_STR_DATETTIMESSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final String DATE_FORMAT_STR_DATETTIME_T = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String DATE_FORMAT_STR_DATETTIME = "yyyy-MM-dd HH:mm:ss";

    public final static String DATE_FORMAT_STR_PLAIN_DATE = "yyyy-MM-dd";

    public static final int SCALE = 2;

    public static final String PAYMENT_METHOD = "debt_card";

    public static final String PENDING = "PENDING";

    //public static final CorrelationIdFormatter DEFAULT = of("traceId(32),spanId(16)");
    public static final String traceId = "traceId"; // this is the name in MDC

    public static final String spanId = "spanId";

    public static Date getNowDate() {
        return new Date();
    }

    public static String getNowDateStr() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_STR_DATETTIMESSS);
        return dateTime.format(formatter);
    }

    public static BigDecimal scaleTo(BigDecimal d, int sc, RoundingMode rm) {
        return d.setScale(sc, rm).stripTrailingZeros();
    }

    public static BigDecimal scaleTo(BigDecimal d) {
        return scaleTo(d, SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    public static boolean decimalEquals(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) == 0;
    }

    public static String decimalFormat(BigDecimal b) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        } else if (b.compareTo(BigDecimal.ZERO) > 0 && b.compareTo(BigDecimal.ONE) < 0) {
            return "0".concat(df.format(b));
        }
        return df.format(b);
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal doubleToBigDecimal(double d) {
        String string = Double.toString(d);
        return new BigDecimal(string);
    }

    public static BigDecimal multiply(BigDecimal a, Integer b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static Date parseToDate(String text) {
        DateFormat dateFormat = new SimpleDateFormat();
        Date date = parseDate(DATE_FORMAT_STR_ISO8601, text);
        if (date != null) {
            return date;
        }
        date = parseDate(DATE_FORMAT_STR_DATETTIMESSS, text);
        if (date != null) {
            return date;
        }
        date = parseDate(DATE_FORMAT_STR_DATETTIME_T, text);
        if (date != null) {
            return date;
        }
        date = parseDate(DATE_FORMAT_STR_DATETTIME, text);
        if (date != null) {
            return date;
        }
        date = parseDate(DATE_FORMAT_STR_PLAIN_DATE, text);
        if (date != null) {
            return date;
        }
        throw new ClientErrorException(CodeEnum.INVALID_DATE);
    }

    public static Date parseDate(String fmt, String text) {
        try {
            return new SimpleDateFormat(fmt).parse(text);
        } catch (Exception e) {
            log.error("Date parsing error {}", e.getMessage(), e);
        }
        return null;
    }

    public static String formatToString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

}
