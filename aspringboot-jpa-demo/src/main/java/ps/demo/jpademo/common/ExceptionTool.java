package ps.demo.jpademo.common;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionTool {

    public static String stackTraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

}
