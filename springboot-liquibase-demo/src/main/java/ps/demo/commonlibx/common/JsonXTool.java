package ps.demo.commonlibx.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import com.jayway.jsonpath.JsonPath;

@Slf4j
public class JsonXTool {

    private JsonXTool() {}

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.findAndRegisterModules();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //Set to ignore exists in json but not in java properties
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private enum JsonOper {
        GET,
        SET,
        DEL
    }

    public static String object2JsonString(Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof String) {
            return o.toString();
        }

        String s = null;

        try {
            s = mapper.writeValueAsString(o);
        } catch (Exception e) {
            log.info("--->> error object2Json err={}", e.getMessage());
        }
        return s;
    }

    public static <T> List<String> listObject2ListJson(List<T> objects) {
        if (objects == null) {
            return null;
        }

        List<String> lists = new ArrayList<String>();
        for (T t : objects) {
            lists.add(JsonXTool.object2JsonString(t));
        }

        return lists;
    }

    public static <T> List<T> listJson2ListObject(List<String> jsons, Class<T> c) {
        if (jsons == null) {
            return null;
        }

        List<T> ts = new ArrayList<T>();
        for (String j : jsons) {
            ts.add(JsonXTool.jsonString2Object(j, c));
        }

        return ts;
    }

    public static <T> T jsonString2Object(String json, Class<T> c) {
        if (org.springframework.util.StringUtils.hasLength(json) == false) {
            return null;
        }

        T t = null;
        try {
            t = mapper.readValue(json, c);
        } catch (Exception e) {
            log.info("--->> error json2Object err={}", e.getMessage());
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonString2Object(String json, TypeReference<T> tr) {
        if (org.springframework.util.StringUtils.hasLength(json) == false) {
            return null;
        }

        T t = null;
        try {
            t = (T) mapper.readValue(json, tr);
        } catch (Exception e) {
            log.info("--->> error json2Object TypeReference err={}", e.getMessage());
        }
        return (T) t;
    }

    public static String simpleMap2jsonString(Map<String, Object> simpleMap) {
        return object2JsonString(simpleMap);
    }
    public static Map<String, Object> json2SimpleMap(String json) {
        if (org.apache.commons.lang3.StringUtils.isBlank(json)) {
            return new TreeMap<String, Object>();
        }
        TypeReference typeReference = new TypeReference<Map<String, Object>>() {
        };
        return (Map<String, Object>) JsonXTool.jsonString2Object(json, typeReference);
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

    /**
     * @Deplicated  use
     * @param jsonObj
     * @param field
     * @param tClass
     * @return
     * @param <T>
     */
    public static <T> T getField(Object jsonObj, String field, Class<T> tClass) {
        Object object = processEle(jsonObj, getPath(field), JsonOper.GET, null);
        return tClass.cast(object);
    }

    public static <T> T getFieldByXpath(Object jsonObj, String xpath) {
        if (xpath == null) {
            return null;
        }
        if (!xpath.startsWith("$.")) {
            xpath = "$." + xpath;
        }
        return JsonPath.read(jsonObj+"", xpath.trim());
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
