package ps.demo.lqbs.test;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import ps.demo.commonlibx.common.SettingTool;

import java.util.function.Consumer;

@Slf4j
public class MongoDBQueryExample {

    public static void main(String[] args) {

        String username = SettingTool.getConfigByKey("username");
        String password = SettingTool.getConfigByKey("password");

        // MongoDB 连接字符串 (格式: mongodb://用户名:密码@主机:端口/数据库名)
        String connectionString = "mongodb://"+username+":"+password+"@"+"localhost:27017";
        log.info("ConnectionString: {}", connectionString);

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            // 获取数据库
            MongoDatabase database = mongoClient.getDatabase("testdb");

            // 获取集合(相当于关系型数据库的表)
            MongoCollection<Document> collection = database.getCollection("users");

            // 示例1: 查询所有文档
            System.out.println("所有用户:");
            collection.find().forEach((Consumer<Document>) document -> {
                System.out.println(document.toJson());
            });

            // 示例2: 带条件查询 (age > 25)
            System.out.println("\n年龄大于25的用户:");
            collection.find(Filters.gt("age", 25))
                    .forEach((Consumer<Document>) document -> {
                        System.out.println(document.toJson());
                    });

            // 示例3: 多条件查询 (age > 25 且 name = "张三")
            System.out.println("\n年龄大于25且姓名为张三的用户:");
            collection.find(Filters.and(
                    Filters.gt("age", 25),
                    Filters.eq("name", "张三")
            )).forEach((Consumer<Document>) document -> {
                System.out.println(document.toJson());
            });

            // 示例4: 查询第一个匹配的文档
            Document firstUser = collection.find(Filters.eq("name", "李四")).first();
            if (firstUser != null) {
                System.out.println("\n第一个姓名为李四的用户:");
                System.out.println(firstUser.toJson());
            }
        }
    }
}