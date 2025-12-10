package com.poc.mpt;

import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.CoreOptions;
import org.apache.flink.runtime.testutils.MiniClusterResource;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.test.util.MiniClusterWithClientResource;

import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;


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
        env.setRuntimeMode(RuntimeExecutionMode.BATCH); // 改为流模式
        env.enableCheckpointing(5000);

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        String warehousePath = "file:///" + System.getProperty("user.dir") + "/target/paimon_warehouse";

        System.out.println("===>>warehousePath: " + warehousePath);
        if (Files.exists(Paths.get(System.getProperty("user.dir") + "/target/paimon_warehouse"))) {
            System.out.println("===>>warehousePath clean up: " + System.getProperty("user.dir") + "/target/paimon_warehouse");
            FileUtils.deleteDirectory(Paths.get(System.getProperty("user.dir") + "/target/paimon_warehouse").toFile());
        }

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


        System.out.println("===>>Print out the users table from paimon");
        TableResult selectResult = tEnv.executeSql("SELECT * FROM users");


// 可选：等待查询完成
        try {
            selectResult.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Query execution interrupted", e);
        }

        selectResult.print();

        // 遍历并打印结果
//        try (CloseableIterator<Row> iterator = selectResult.collect()) {
//            while (iterator.hasNext()) {
//                Row row = iterator.next();
//                System.out.println("Row: " + row);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error while iterating results", e);
//        } finally {
//            // 确保 TableResult 被正确关闭
//            if (selectResult != null) {
//                try {
//                    selectResult.getJobClient().ifPresent(client -> {
//                        try {
//                            client.cancel().get();
//                        } catch (Exception e) {
//                            // 忽略取消异常
//                        }
//                    });
//                } catch (Exception e) {
//                    // 忽略关闭异常
//                }
//            }
//        }

        // 在测试方法最后添加
        if (env != null) {
            env.close();
        }

    }

}