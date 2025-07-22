package ps.demo.commonlibx.common;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JsonToolX {

    private JsonToolX() {}

    private enum JsonOper {
        GET,
        SET,
        DEL
    }

    public static Object parseJsonFromString(String jsonStr) {
        return new Gson().fromJson(jsonStr, Object.class);
    }

    public static Object parseJsonFromFile(File file) throws FileNotFoundException {
        return new Gson().fromJson(new FileReader(file), Object.class);
    }

    public static boolean isValidJson(String jsonStr) {
        try {
            new Gson().fromJson(jsonStr, Object.class);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    public static <T> T getField(Object jsonObj, String field, Class<T> tClass) {
        Object object = processEle(jsonObj, getPath(field), JsonOper.GET, null);
        return tClass.cast(object);
    }

    public static <T> Object setField(Object jsonObj, String field, T value) {
        return processEle(jsonObj, getPath(field), JsonOper.SET, value);
    }

    public static Object delField(Object jsonObj, String field) {
        return processEle(jsonObj, getPath(field), JsonOper.DEL, null);
    }

    private static Map<String, Object> convertToMap(Object jsonObj) {
        if (jsonObj instanceof Map) {
            return (Map<String, Object>) jsonObj;
        }
        throw new IllegalArgumentException("Not able to convert to map");
    }

    private static List<Object> convertToList(Object jsonObj) {
        if (jsonObj instanceof List) {
            return (List<Object>) jsonObj;
        }
        throw new IllegalArgumentException("Not able to convert to list");
    }

    private static <T> Object updateInMap(Object jsonObj, String field, T val) {
        if (ObjectUtils.isEmpty(val)) {
            return convertToMap(jsonObj).remove(field);
        } else {
            return convertToMap(jsonObj).put(field, val);
        }
    }

    private static <T> Object updateInList(Object jsonObj, String field, T val) {
        if (ObjectUtils.isEmpty(val)) {
            return convertToList(jsonObj).remove(Integer.parseInt(field));
        } else {
            return convertToList(jsonObj).set(Integer.parseInt(field), val);
        }
    }

    private static <T> Object updateJsonField(Object jsonObj, String field, T val) {
        if (jsonObj instanceof List) {
            return updateInList(jsonObj, field, val);
        } else if (jsonObj instanceof Map) {
            return updateInMap(jsonObj, field, val);
        } else {
            throw new IllegalArgumentException("Can't update non-List, non-Map field");
        }
    }

    private static <T> Object processEle(Object jsonObj, List<String> path, JsonOper oper, T newVal) {
        Object value;
        if (path.isEmpty()) {
            return jsonObj;
        } else if (oper != JsonOper.GET && path.size() == 1) {
            value = updateJsonField(jsonObj, path.get(0), (oper == JsonOper.DEL ? null : newVal));
        } else {
            String path1 = path.remove(0);
            Object child;
            if (jsonObj instanceof Map) {
                child = convertToMap(jsonObj).get(path1);
            } else if (jsonObj instanceof List) {
                child = convertToList(jsonObj).get(Integer.parseInt(path1));
            } else {
                throw new IllegalArgumentException("Only support list or map");
            }
            value = processEle(child, path, oper, newVal);
        }
        return value;
    }

    private static List<String> getPath(String path) {
        List<String> result = new ArrayList<>();
        if (StringUtils.isBlank(path)) {
            return result;
        }
        String [] split = path.split("\\.");
        if (split.length == 0) {
            result.add(path);
        } else {
            result.addAll(Arrays.asList(split));
        }
        return result;
    }



}
