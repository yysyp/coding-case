package ps.demo.commonlibx.common;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Component
public class MapperTool {

    private static Mapper dozer = DozerBeanMapperBuilder.buildDefault();

    /**
     * Convert from x to y
     *
     * @param x
     * @param y
     * @param <X>
     * @param <Y>
     * @return
     */
    public static <X, Y> Y convert(X x, Y y) {
        if (x == null) {
            return null;
        }
        dozer.map(x, y);
        return y;
    }

    public static <T> T convert(Object s, Class<T> klasst) {
        if (s == null) {
            return null;
        }
        return dozer.map(s, klasst);
    }

    public static <T> List<T> convert(Collection srcList, Class<T> destinationClass) {
        if (CollectionUtils.isEmpty(srcList)) {
            return new ArrayList<>();
        }

        List<T> targetList = new ArrayList<>();
        srcList.forEach(e -> targetList.add(convert(e, destinationClass)));
        return targetList;
    }

    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return null;
        }
        return dozer.map(bean, Map.class);
        //return oMapper.convertValue(bean, Map.class);
    }

    public static <T> T toBean(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        return dozer.map(map, clazz);
        //return oMapper.convertValue(map, clazz);
    }


    public static <X> X createInstance(Class<?> cls) {
        X obj = null;
        try {
            obj = (X) cls.newInstance();
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    public static boolean setProperty(Object bean, String key, Object value) {
        try {
            BeanUtils.setProperty(bean, key, value);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static void removeNonQueryKey(Map<String, Object> queryMap) {
        queryMap.remove("current");
        queryMap.remove("size");
        queryMap.remove("orderBys");
        queryMap.remove("key");

    }

    public static Map<String, Object> beanToDbNameMap(Map<String, Object> beanNameMap) {
        if (beanNameMap == null) {
            return null;
        }
        Map<String, Object> dbNameMap = new HashMap();
        for (Map.Entry<String, Object> entry : beanNameMap.entrySet()) {
            if (entry.getValue() != null) {
                dbNameMap.put(StringTool.toDbName(entry.getKey()), entry.getValue());
            }
        }
        return dbNameMap;
    }

    public static Map<String, Object> dbToBeanNameMap(Map<String, Object> dbNameMap) {
        if (dbNameMap == null) {
            return null;
        }
        Map<String, Object> beanNameMap = new HashMap();
        for (Map.Entry<String, Object> entry : dbNameMap.entrySet()) {
            if (entry.getValue() != null) {
                beanNameMap.put(StringTool.toJavaName(entry.getKey()), entry.getValue());
            }
        }
        return beanNameMap;
    }

    public static Class<?> getActualArgumentType(Class<?> clazz, int index) {
        Class<?> entitiClass = null;
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > index) {
                entitiClass = (Class<?>) actualTypeArguments[index];
            }
        }
        return entitiClass;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /**
     * List<PlatformStScEntity> distinctList = list.stream().filter(StreamUtil.distinctByKeys(
     * PlatformStScEntity::getPlatformCode, PlatformStScEntity::getPlatformStCode))
     * .collect(Collectors.toList());
     *
     * @param keyExtractors
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t -> {
            final List<?> keys = Arrays.stream(keyExtractors).map(ke -> ke.apply(t)).collect(Collectors.toList());
            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }

    public static void copyProperties(Object source, Object target) {
        convert(source, target);
        //BeanUtils.copyProperties(source, target);
    }

    public static <T> List<T> copyAndConvertItems(Collection srcList, Class<T> targetItemType) {
        return convert(srcList, targetItemType);
    }
}
