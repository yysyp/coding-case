package ps.demo.commonlibx.common;

import com.google.gson.Gson;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultSetTool {

    private ResultSetTool() {}


    public static List<Object> convertToGsonObjList(ResultSet resultSet) throws SQLException {
        List<Object> list = new ArrayList<>();

        int totalCol = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            Object obj = constructGson();
            for (int i = 0; i < totalCol; i++) {
                JsonToolX.setField(obj, resultSet.getMetaData().getColumnLabel(i + 1)
                , processObjectVal(resultSet.getObject(i + 1)));
            }
            list.add(obj);
        }

        return list;
    }

    public static List<Map<String, Object>> convertToMapList(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> mapList = new ArrayList<>();
        int totalCol = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> map = new HashedMap();
            for (int i = 0; i < totalCol; i ++) {
                map.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));
                mapList.add(map);
            }
        }
        return mapList;
    }

    private static Object constructGson() {
        return new Gson().fromJson("{}", Object.class);
    }

    private static Object processObjectVal(Object obj) {
        if (obj == null) {
            return "NULL";
        }
        return obj instanceof String && StringUtils.isEmpty(obj.toString()) ? "EMPTY" : obj;
    }

}
