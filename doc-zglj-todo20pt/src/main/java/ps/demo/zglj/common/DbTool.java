package ps.demo.zglj.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * MyDbUtils 工具类
 */
@Slf4j
public class DbTool {




    private static DataSource dataSource;

    public static void setDataSource(DataSource dataSource) {
        DbTool.dataSource = dataSource;
    }

    public static DataSource getDataSourceSslMode(String settingFileName, String group) {
        String url = SettingTool.getConfigByGroupAndKey(settingFileName, group, "url");
        String username = SettingTool.getConfigByGroupAndKey(settingFileName, group, "username");
        String password = SettingTool.getConfigByGroupAndKey(settingFileName, group, "password");
        String driver = SettingTool.getConfigByGroupAndKey(settingFileName, group, "driver");
        String sslrootcert = SettingTool.getConfigByGroupAndKey(settingFileName, group, "sslrootcert");
        String sslcert = SettingTool.getConfigByGroupAndKey(settingFileName, group, "sslcert");
        String sslkey = SettingTool.getConfigByGroupAndKey(settingFileName, group, "sslkey");
        final DriverManagerDataSource dataSource1 = new DriverManagerDataSource();
        dataSource1.setUrl(url);
        dataSource1.setUsername(username);
        dataSource1.setPassword(password);
        dataSource1.setDriverClassName(driver);

        Properties props = new Properties();
        props.setProperty("ssl", "true");
        props.setProperty("sslmode", "verify-ca");
        props.setProperty("sslrootcert", sslrootcert); //.pem
        props.setProperty("sslcert", sslcert); //.pem
        props.setProperty("sslkey", sslkey); //.pk8
        dataSource1.setConnectionProperties(props);
        dataSource = dataSource1;

        return dataSource;
    }

    /**
     *
     */
    public static Connection getConnection() {
        if (dataSource != null) {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                log.error("--->>err message="+e.getMessage(), e);
            }
        }
        Connection connection = null;
        Properties properties = new Properties();
        InputStream in = DbTool.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(in);
            String url = properties.get("url").toString();
            String username = properties.get("username").toString();
            String password = properties.get("password").toString();
            String driver = properties.get("driver").toString();
            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setUrl(url);
            driverManagerDataSource.setUsername(username);
            driverManagerDataSource.setPassword(password);
            driverManagerDataSource.setDriverClassName(driver);

            dataSource = driverManagerDataSource;
            connection = dataSource.getConnection();
        } catch (Exception e) {
            log.error("--->>err message="+e.getMessage(), e);

        }
//        try {
//            properties.load(in);
//            String url = properties.get("url").toString();
//            String username = properties.get("username").toString();
//            String password = properties.get("password").toString();
//            String driver = properties.get("driver").toString();
//            Class.forName(driver);
//            connection = DriverManager.getConnection(url, username, password);
//        } catch (Exception e) {
//            log.error("--->>err message="+e.getMessage(), e);
//
//        }
        return connection;
    }

    public static Connection getConnection(String settingFileName, String group) {
        Connection connection = null;
        Properties properties = new Properties();
        try {
            String url = SettingTool.getConfigByGroupAndKey(settingFileName, group, "url");
            String username = SettingTool.getConfigByGroupAndKey(settingFileName, group, "username");
            String password = SettingTool.getConfigByGroupAndKey(settingFileName, group, "password");
            String driver = SettingTool.getConfigByGroupAndKey(settingFileName, group, "driver");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            log.error("--->>err message="+e.getMessage(), e);

        }
        return connection;
    }

    /**
     *
     */
    public static void release(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            log.error("--->>err message="+e.getMessage(), e);
        }
    }

    /**
     *
     */
    public static Map query(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData;
        try {
            connection = DbTool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            Map<String, Object> map = new HashMap<>(16);
            if (resultSet.next()) {
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    String columnname = resultSetMetaData.getColumnLabel(i + 1);
                    Object obj = resultSet.getObject(i + 1);
                    map.put(columnname, obj);
                }
            }
            return map;
        } catch (Exception e) {
            log.error("--->>err message="+e.getMessage(), e);
        } finally {
            DbTool.release(connection, preparedStatement, resultSet);

        }
        return null;
    }

    /**
     *
     */
    public static <T> T query(Class<T> classes, String sql, Object... args) {
        T object = null;
        try {
            Map<String, Object> map = query(sql, args);
            if (map != null && map.size() > 0) {
                object = classes.newInstance();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String fieldName = entry.getKey();
                    Object value = entry.getValue();
                    ReflectionTool.setFieldValue(object, fieldName, value);
                }
            }
        } catch (Exception e) {
            log.error("--->>err message="+e.getMessage(), e);
        }
        return object;
    }

    /**
     *
     */
    public static List<Map> list(String sql, Object... args) {
        List<Map> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData;
        try {
            connection = DbTool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            Map<String, Object> map = new HashMap<>(16);
            while (resultSet.next()) {
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    String columnname = resultSetMetaData.getColumnLabel(i + 1);
                    Object obj = resultSet.getObject(i + 1);
                    map.put(columnname, obj);
                }
                list.add(map);
            }
        } catch (Exception e) {
            log.error("--->>err message="+e.getMessage(), e);
        } finally {
            DbTool.release(connection, preparedStatement, resultSet);

        }
        return list;
    }

    /**
     *
     */
    public static <T> List<T> list(Class<T> classes, String sql, Object... args) {
        List<T> objects = new ArrayList<>();
        try {
            List<Map> list = list(sql, args);
            if (list != null && list.size() > 0) {
                for (Map<String, Object> map : list) {
                    T object = null;
                    if (list != null && list.size() > 0) {
                        object = classes.newInstance();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            String fieldName = entry.getKey();
                            Object value = entry.getValue();
                            ReflectionTool.setFieldValue(object, fieldName, value);
                        }
                    }
                    objects.add(object);
                }
            }
        } catch (Exception e) {
            log.error("--->>err message="+e.getMessage(), e);
        }
        return objects;
    }

    /**
     *
     */
    public static boolean save(String sql, Object... args) {
        boolean state = true;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DbTool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.execute();
        } catch (Exception e) {
            state = false;
            log.error("--->>err message="+e.getMessage(), e);
        } finally {
            DbTool.release(connection, preparedStatement, resultSet);
        }
        return state;
    }

    /**
     *
     */
    public static boolean remove(String sql, Object... args) {
        boolean state = true;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DbTool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.execute();
        } catch (Exception e) {
            state = false;
            log.error("--->>err message="+e.getMessage(), e);
        } finally {
            DbTool.release(connection, preparedStatement, resultSet);
        }
        return state;
    }

    /**
     *
     */
    public static boolean update(String sql, Object... args) {
        boolean state = true;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DbTool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.execute();
        } catch (Exception e) {
            state = false;
            log.error("--->>err message="+e.getMessage(), e);
        } finally {
            DbTool.release(connection, preparedStatement, resultSet);
        }
        return state;
    }

}
