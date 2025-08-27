package ps.demo.commonlibx.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class TheCustomDateEditor extends CustomDateEditor {

    // public static final String DATE_FORMAT_STR_DATETTIMESSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT_STR_DATETTIMESSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DATE_FORMAT_STR_DATETTIME = "yyyy-MM-dd'T'HH:mm:ss";
    protected final static String DATE_FORMAT_STR_PLAIN_DATE = "yyyy-MM-dd";

    protected DateFormat dateFormat;

    public TheCustomDateEditor(DateFormat dateFormat, boolean allowEmpty) {
        super(dateFormat, allowEmpty);
        this.dateFormat = dateFormat;
    }

    public TheCustomDateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
        super(dateFormat, allowEmpty, exactDateLength);
        this.dateFormat = dateFormat;
    }


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            super.setAsText(text);
        } catch (IllegalArgumentException e) {
            try {
                setValue(this.dateFormat.parse(text));
            } catch (ParseException e1) {
                Date date = parseDate(DATE_FORMAT_STR_DATETTIMESSS, text);
                if (date != null) {
                    setValue(date);
                }
                date = parseDate(DATE_FORMAT_STR_DATETTIME, text);
                if (date != null) {
                    setValue(date);
                }
                date = parseDate(DATE_FORMAT_STR_PLAIN_DATE, text);
                if (date != null) {
                    setValue(date);
                }

            }
        }
    }

    private Date parseDate(String fmt, String text) {
        try {
            return new SimpleDateFormat(fmt).parse(text);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsText() {
        return super.getAsText();
    }
}
