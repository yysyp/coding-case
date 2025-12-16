package ps.demo.zglj.common;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionsConvertTool {


    //For treating excel loaded List<List<String>> table, to make all the lines List<String> has same length.
    public static List<String> alignSize(List<String> line, int length) {
        if (line == null) {
            List defaultLine = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                defaultLine.add("");
            }
            return defaultLine;
        }
        if (line.size() < length) {
            for (int i = line.size(); i < length; i++) {
                line.add("");
            }
        }
        return line;
    }

    public static List<String> convertToStringList(List<Object> objects) {
        List<String> stringList = objects.stream().map(e -> e + "").collect(Collectors.toList());
        return stringList;
    }

    public static <T> List<Object> convertToObjectList(List<T> ts) {
        List<Object> objList = ts.stream().collect(Collectors.toList());
        return objList;
    }

    public static <T> List<T> convertToTList(List<Object> objects) {
        List<T> objList = objects.stream().map(e -> (T) e).collect(Collectors.toList());
        return objList;
    }

    public static <T> List<T> castList(List list, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        //if (obj instanceof List<?>) {
        for (Object o : (List<?>) list) {
            result.add(clazz.cast(o));
        }
        //}
        return result;
    }

    public static List<List<Object>> castListToObject(List<List<String>> lists) {
        List<List<Object>> listObj = new ArrayList<>();
        for (List<?> list : lists) {
            listObj.add(castList(list, Object.class));
        }
        return listObj;
    }

    public static <T> List<List<T>> castListToT(List<List<?>> lists, Class tklass) {
        List<List<T>> listObj = new ArrayList<>();
        for (List<?> list : lists) {
            listObj.add(castList(list, tklass));
        }
        return listObj;
    }

    public static String getAsString(List<String> line, int i) {
        if (line == null || i >= line.size()) {
            return "";
        }
        String var = line.get(i);
        if (var == null) {
            return "";
        }
        return var.toString();
    }

    public static Integer getAsInteger(List<String> line, int i) {
        if (line == null || i >= line.size()) {
            return 0;
        }
        String var = line.get(i);
        if (var == null) {
            return 0;
        }
        try {
            return Integer.parseInt(var.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Long getAsLong(List<String> line, int i) {
        if (line == null || i >= line.size()) {
            return 0L;
        }
        String var = line.get(i);
        if (var == null) {
            return 0L;
        }
        try {
            return Long.parseLong(var.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static Boolean getAsBoolean(List<String> line, int i) {
        if (line == null || i >= line.size()) {
            return false;
        }
        String var = line.get(i);
        if (var == null) {
            return false;
        }
        try {
            return Boolean.parseBoolean(var.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static BigDecimal getAsDecimal(List<String> line, int i) {
        if (line == null || i >= line.size()) {
            return new BigDecimal("0");
        }
        String var = line.get(i);
        if (var == null) {
            return new BigDecimal("0");
        }
        try {
            return new BigDecimal(var.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new BigDecimal("0");
        }
    }


    public static Date getAsDate(List<String> line, int i, String fmt) {
        if (line == null || i >= line.size()) {
            return null;
        }
        String var = line.get(i);
        if (var == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(fmt).parse(line.get(i));
        } catch (ParseException e) {
            return null;
        }
    }

    public static String decimalToString(BigDecimal decimal) {
        if (decimal == null) {
            return "";
        }
        decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return decimal.toString();
    }

    public static String floatToString(Float f) {
        //return String.format("%,.2f", f);
        return String.format("%.2f", f);

    }

    public static String doubleToString(Double d) {
        return String.format("%.2f", d);
    }

//    DecimalFormat df=new DecimalFormat("#.##");
//    df.setRoundingMode(RoundingMode.UP);
//    double d = 2.456534;
//    System.out.println("Double upto 2 decimal places: "+df.format(d))

//    NumberFormat nf= NumberFormat.getInstance();
//    nf.setMaximumFractionDigits(2);
//    System.out.println("Double d1 upto 2 decimal places: " +nf.format(d1));


}
