package pslab;

//import com.sun.org.apache.regexp.internal.RE;
import org.apache.poi.ss.formula.functions.T;
import ps.demo.util.json.JSONException;
import ps.demo.util.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

public class TfUtil {
    public static TfUtil instance = new TfUtil();

    private TfUtil() {
    }

    public static TfUtil getInstance() {
        return instance;
    }

    public boolean isNum(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            return false;
        }
        String type = jsonObject.getString("type");
        if (type == null) {
            return false;
        }
        type = type.trim();
        return //type.equalsIgnoreCase("Boolean")
                type.equalsIgnoreCase("Integer")
                        || type.equalsIgnoreCase("Long")
                        || type.equalsIgnoreCase("Float")
                        || type.equalsIgnoreCase("Double")
                        || type.equalsIgnoreCase("BigDecimal");

    }

    public boolean isBool(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            return false;
        }
        String type = jsonObject.getString("type");
        if (type == null) {
            return false;
        }
        type = type.trim();
        return type.equalsIgnoreCase("Boolean");
    }

    public boolean isDate(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            return false;
        }
        String type = jsonObject.getString("type");
        if (type == null) {
            return false;
        }
        type = type.trim();
        return type.equalsIgnoreCase("Date")
                || type.equalsIgnoreCase("LocalDate")
                || type.equalsIgnoreCase("LocalDateTime")
                || type.equalsIgnoreCase("Timestamp")
                || type.equalsIgnoreCase("Instant");
    }

    public String maxlength(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            return "";
        }
        if (!jsonObject.has("maxlength")) {
            return "";
        }
        return "maxlength=\"" + jsonObject.getString("maxlength") + "\"";
    }

    public String required(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            return "";
        }
        if (!jsonObject.has("nullable")) {
            return "";
        }
        String nullable = jsonObject.getString("nullable");
        if (nullable.trim().equalsIgnoreCase("No")) {
            return "required=\"required\"";
        }
        return "";
    }

    public String formItem(JSONObject jsonObject) throws JSONException {
        return formItem(jsonObject, false);
    }

    public String formItem(JSONObject jsonObject, boolean readOnly) throws JSONException {
        if (jsonObject == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        String name = jsonObject.getString("name");

        String readOnlySet = readOnly? "readonly=\"readonly\"":"";
        if (isBool(jsonObject)) {
            buffer.append("<input type=\"checkbox\" id=\""+name+"\" name=\""+name+"\" "+readOnlySet+" th:checked=\"*{"+name+"}\" />");
            return buffer.toString();
        }
        int maxlength = 50;
        if (jsonObject.has("maxlength")) {
            maxlength = jsonObject.getInt("maxlength");
        }
        String required = required(jsonObject);
        if(isNum(jsonObject)) {
            buffer.append("<input type=\"number\" id=\""+name+"\" name=\""+name+"\" "+readOnlySet+" "+required+" maxlength=\""+maxlength+"\" th:value=\"*{"+name+"}\" />");
            return buffer.toString();
        }
        if (isDate(jsonObject)) {
            buffer.append("<input type=\"text\" id=\""+name+"\" name=\""+name+"\" "+readOnlySet+" "+required+" th:value=\"*{#dates.format("+name+", 'yyyy-MM-dd HH:mm:ss')}\" />");
            return buffer.toString();
        }
        if (maxlength > 50) {
            buffer.append("<textarea type=\"text\" id=\""+name+"\" name=\""+name+"\" "+readOnlySet+" rows=\"10\" cols=\"50\" "+required+" maxlength=\""+maxlength+"\" th:text=\"*{"+name+"}\" ></textarea>");
            return buffer.toString();
        }
        buffer.append("<input type=\"text\" id=\""+name+"\" name=\""+name+"\" "+readOnlySet+" "+required+" maxlength=\""+maxlength+"\" th:value=\"*{"+name+"}\" />");
        return buffer.toString();
    }


}
