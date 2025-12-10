package com.poc;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.CoreOptions;
import org.apache.flink.runtime.testutils.MiniClusterResource;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.test.util.MiniClusterWithClientResource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class PaimonEmbeddedTest {

    private static MiniClusterResource miniClusterResource;

    @BeforeClass
    public static void setUp() throws Exception {
        // 配置 MiniCluster
        Configuration config = new Configuration();
        config.set(CoreOptions.DEFAULT_PARALLELISM, 1);

        miniClusterResource = new MiniClusterResource(
            new MiniClusterResourceConfiguration.Builder()
                .setNumberSlotsPerTaskManager(2)
                .setNumberTaskManagers(1)
                .setConfiguration(config)
                .build()
        );

        miniClusterResource.before(); // 启动 MiniCluster
        System.out.println("✅ Flink MiniCluster started");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (miniClusterResource != null) {
            miniClusterResource.after(); // 关闭 MiniCluster
            System.out.println("🛑 Flink MiniCluster stopped");
        }
    }

    @Test
    public void testWriteToPaimonWithMiniCluster() throws Exception {
        // 获取与 MiniCluster 关联的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING); // 改为流模式
        env.enableCheckpointing(5000);

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        String warehousePath = "file:///" + System.getProperty("user.dir") + "/target/paimon_warehouse";

        // 创建 Paimon Catalog
        tEnv.executeSql(
                "CREATE CATALOG paimon_catalog WITH (" +
                        "  'type' = 'paimon'," +
                        "  'warehouse' = '" + warehousePath + "'" +
                        ")"
        );

        tEnv.useCatalog("paimon_catalog");
        tEnv.executeSql("CREATE DATABASE IF NOT EXISTS test_db");
        tEnv.useDatabase("test_db");

        // 创建带主键的表
        tEnv.executeSql(
                "CREATE TABLE users (" +
                        "  id BIGINT," +
                        "  name STRING," +
                        "  PRIMARY KEY (id) NOT ENFORCED" +
                        ") WITH (" +
                        "  'bucket' = '2'" +
                        ")"
        );

        // 插入数据（UPSERT）
        TableResult result = tEnv.executeSql(
                "INSERT INTO users VALUES (1, 'Alice'), (2, 'Bob'), (1, 'Alice Updated')"
        );

        // 等待作业完成（可选）
        result.await();

        System.out.println("✅ Data written to Paimon table at: " + warehousePath);
        System.out.println("📁 Check directory: ./target/paimon_warehouse/test_db/users/");
    }

}