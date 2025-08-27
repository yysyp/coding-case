package ps.demo.commonlibx.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import ps.demo.commonlibx.ReflectionTool;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class BeanTool {

    public static List<Object> convertToListObject(List<? extends Object> subTypeList) {
        return new ArrayList<>(subTypeList);
    }

    public static List<String> getBeanProperties(Object obj) {
        List<String> propertyNames = new ArrayList<>();
        try {
            BeanInfo bi = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] pds = bi.getPropertyDescriptors();
            for (int i=0; i<pds.length; i++) {
                // Get property name
                String propName = pds[i].getName();
                propertyNames.add(propName);
                // Get the value of prop1
//                Expression expr = new Expression(o, "getProp1", new Object[0]);
//                expr.execute();
//                String s = (String)expr.getValue();
            }
        } catch (java.beans.IntrospectionException e) {
            log.error("ignore "+e.getMessage(), e);
        }
        return propertyNames;
    }

    public static Object getValue(Object bean, String property) {
        try {
            return ReflectionTool.getFieldValue(bean, property);
            //BeanUtils.getProperty(bean, property);
        } catch (Exception e) {
            log.error("ignore "+e.getMessage(), e);
        }
        return null;
    }

    public static void setValue(Object bean, String property, Object value) {
        try {
            ReflectionTool.setFieldValue(bean, property, value);
            //BeanUtils.setProperty(bean, property, value);
        } catch (Exception e) {
            log.error("ignore "+e.getMessage(), e);
        }
    }


    public static Object clone(Object object) {
        try {
            Object result = BeanUtils.instantiateClass(object.getClass());
            BeanUtils.copyProperties(object, result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }


//    public static Object deepCopy(Object from) {
//        Object obj = null;
//        try {
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ObjectOutputStream out = new ObjectOutputStream(bos);
//            out.writeObject(from);
//            out.flush();
//            out.close();
//
//
//            ObjectInputStream in = new ObjectInputStream(
//                    new ByteArrayInputStream(bos.toByteArray()));
//            obj = in.readObject();
//        } catch(IOException e) {
//            e.printStackTrace();
//        } catch(ClassNotFoundException e2) {
//            e2.printStackTrace();
//        }
//        return obj;
//    }


    public static void copyProperties(Object source, Object target) {
        MapperTool.convert(source, target);
        //BeanUtils.copyProperties(source, target);
    }

    public static <T> List<T> copyAndConvertItems(Collection srcList, Class<T> targetItemType) {
        return MapperTool.convert(srcList, targetItemType);
    }

}
