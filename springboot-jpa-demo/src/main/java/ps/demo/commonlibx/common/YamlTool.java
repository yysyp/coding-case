package ps.demo.commonlibx.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class YamlTool {

    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    static {
        mapper.findAndRegisterModules();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //Set to ignore exists in json but not in java properties
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static <T> T readObject(File file, Class<T> klass) {
        try {
            return mapper.readValue(file, klass);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T readObject(File file, TypeReference<T> tr) {
        try {
            return mapper.readValue(file, tr);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> List<T> readListOfObjects(File file, Class<T> klass) {
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, klass);
        List<T> list = null;
        try {
            list = mapper.readValue(file, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <K, V> Map<K, V> readMapOfObjects(File file, Class<K> klassk, Class<V> klassv) {
        MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, klassk, klassv);
        try {
            return mapper.readValue(file, mapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String writeObjectAsString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static void writeObject(File file, Object obj) {
        try {
            mapper.writeValue(file, obj);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
