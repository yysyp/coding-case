package ps.demo.lqbs.test;

import redis.clients.jedis.Jedis;

public class JedisExample {
    public static void main(String[] args) {
        // 创建连接，默认连接本地6379端口
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            // 认证(如果有密码)
            // jedis.auth("yourpassword");

            // 测试连接
            System.out.println("连接成功: " + jedis.ping());

            // 设置键值
            jedis.set("mykey", "Hello Redis");

            // 获取值
            String value = jedis.get("mykey");
            System.out.println("获取的值: " + value);

            // 其他操作
            jedis.lpush("mylist", "item1", "item2", "item3");
            System.out.println("列表内容: " + jedis.lrange("mylist", 0, -1));
        }
    }
}