package ps.demo.zglj.common;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
public class ReflectionTool {
    /**
     *
     */
    public static <T> Class<T> getSuperGenericType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     *
     */
    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     *
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        Field field;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                log.error("--->>err message="+e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     *
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);
        assert field != null;
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error("--->>err message="+e.getMessage(), e);
        }
    }

    /**
     *
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        assert field != null;
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error("--->>err message="+e.getMessage(), e);
        }
        return null;
    }
}
