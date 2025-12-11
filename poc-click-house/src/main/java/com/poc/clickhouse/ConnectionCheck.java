package com.poc.clickhouse;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.clickhouse.jdbc.ClickHouseDataSource;
import com.poc.clickhouse.common.SettingTool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionCheck {

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "default");
        properties.setProperty("password", SettingTool.getConfigByKey("password"));
        DataSource dataSource = new ClickHouseDataSource(
                "jdbc:clickhouse://127.0.0.1:8123/default?compress=1&compress_algorithm=gzip", properties
        );

        Entity entity = Db.use(dataSource).queryOne("select 1");
        System.out.println(entity);
    }

}
