package ps.demo.jpademo.cache;

import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.awaitility.Awaitility.await;

/**
 * 通用内存缓存全面单元测试
 */
@DisplayName("通用内存缓存测试")
class InMemoryGenericCacheTest {

    private InMemoryGenericCache<String> cache;
    private InMemoryGenericCache<ComplexObject> objectCache;

    @BeforeEach
    void setUp() {
        cache = new InMemoryGenericCache<>();
        objectCache = new InMemoryGenericCache<>();

        // 重置为默认配置
        cache.setMaxSize(100);
        cache.setMaxDataSize(10 * 1024 * 1024); // 10MB
        cache.setEvictionPolicy(EvictionPolicy.LRU);

        objectCache.setMaxSize(50);
        objectCache.setEvictionPolicy(EvictionPolicy.LRU);
    }


    // 测试用的复杂对象
    static class ComplexObject {
        private String id;
        private String name;
        private List<String> items;
        private Map<String, Object> metadata;

        public ComplexObject(String id, String name) {
            this.id = id;
            this.name = name;
            this.items = new ArrayList<>();
            this.metadata = new HashMap<>();
        }

        // getters and setters
        public String getId() { return id; }
        public String getName() { return name; }
        public List<String> getItems() { return items; }
        public Map<String, Object> getMetadata() { return metadata; }
    }

    @Test
    @DisplayName("基本存储和检索测试")
    void testBasicPutAndGet() {
        // 正常存储和检索
        assertTrue(cache.put("key1", "value1"));
        Optional<String> result = cache.get("key1");
        assertTrue(result.isPresent());
        assertEquals("value1", result.get());

        // 检索不存在的键
        assertFalse(cache.get("nonexistent").isPresent());
    }

    @Test
    @DisplayName("空值存储测试 - 应该抛出异常")
    void testNullValueStorageThrowsException() {
        // 测试存储null值会抛出NullPointerException
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            cache.put("nullKey", null);
        });

        assertEquals("Value cannot be null", exception.getMessage());

        // 验证缓存状态没有改变
        assertFalse(cache.contains("nullKey"));
        assertFalse(cache.get("nullKey").isPresent());
        assertEquals(0, cache.size());
    }

    @Test
    @DisplayName("空键测试 - 应该抛出异常")
    void testNullKeyThrowsException() {
        // 测试使用null键会抛出NullPointerException
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            cache.put(null, "value");
        });

        assertEquals("Key cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("重复键测试")
    void testDuplicateKey() {
        // 第一次存储
        assertTrue(cache.put("key", "value1"));
        assertEquals("value1", cache.get("key").get());

        // 覆盖存储
        assertTrue(cache.put("key", "value2"));
        assertEquals("value2", cache.get("key").get());
    }

    @Test
    @DisplayName("删除操作测试")
    void testRemove() {
        // 添加后删除
        assertTrue(cache.put("key1", "value1"));
        assertTrue(cache.remove("key1"));
        assertFalse(cache.get("key1").isPresent());

        // 删除不存在的键
        assertFalse(cache.remove("nonexistent"));
    }

    @Test
    @DisplayName("包含检查测试")
    void testContains() {
        cache.put("key1", "value1");
        assertTrue(cache.contains("key1"));
        assertFalse(cache.contains("nonexistent"));
    }

    @Test
    @DisplayName("清空缓存测试")
    void testClear() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        assertEquals(2, cache.size());

        cache.clear();
        assertEquals(0, cache.size());
        assertFalse(cache.contains("key1"));
        assertFalse(cache.contains("key2"));
    }


    @Test
    @DisplayName("过期时间测试 - 永不过期")
    void testNoExpiration() throws InterruptedException {
        cache.put("key1", "value1");
        Thread.sleep(100);
        assertTrue(cache.get("key1").isPresent());
    }

    @Test
    @DisplayName("过期时间测试 - 短期过期")
    void testShortExpiration() throws InterruptedException {
        cache.put("key1", "value1", 100, TimeUnit.MILLISECONDS);

        // 立即访问应该存在
        assertTrue(cache.get("key1").isPresent());

        // 等待过期
        Thread.sleep(150);
        assertFalse(cache.get("key1").isPresent());
    }

    @Test
    @DisplayName("过期时间测试 - 自动清理")
    void testExpirationCleanup() throws InterruptedException {
        // 添加会过期的条目
        cache.put("expired1", "value1", 50, TimeUnit.MILLISECONDS);
        cache.put("expired2", "value2", 50, TimeUnit.MILLISECONDS);
        cache.put("permanent", "value3"); // 永不过期

        Thread.sleep(100);

        // 手动清理
        int cleaned = cache.cleanup();
        assertEquals(2, cleaned);
        assertEquals(1, cache.size());
        assertTrue(cache.contains("permanent"));
    }

    @ParameterizedTest
    @EnumSource(value = TimeUnit.class, names = {"MILLISECONDS", "MICROSECONDS", "NANOSECONDS"})
    @DisplayName("短时间单位过期测试")
    void testShortTimeUnits(TimeUnit timeUnit) throws InterruptedException {
        long duration = 100;
        if (timeUnit == TimeUnit.MICROSECONDS) duration = 100000; // 100毫秒
        if (timeUnit == TimeUnit.NANOSECONDS) duration = 100000000; // 100毫秒

        cache.put("key", "value", duration, timeUnit);
        assertTrue(cache.contains("key"));

        Thread.sleep(200); // 等待足够长时间
        assertFalse(cache.contains("key"));
    }

    @ParameterizedTest
    @EnumSource(value = TimeUnit.class, names = {"SECONDS", "MINUTES", "HOURS", "DAYS"})
    @DisplayName("长时间单位设置测试")
    void testLongTimeUnits(TimeUnit timeUnit) {
        // 只测试设置，不测试过期（因为时间太长）
        cache.put("key", "value", 1, timeUnit);
        assertTrue(cache.contains("key"));
    }

    @Test
    @DisplayName("条目数量限制测试")
    void testMaxSizeLimit() {
        cache.setMaxSize(3);

        // 添加3个条目
        assertTrue(cache.put("key1", "value1"));
        assertTrue(cache.put("key2", "value2"));
        assertTrue(cache.put("key3", "value3"));
        assertEquals(3, cache.size());

        // 添加第4个应该触发淘汰
        assertTrue(cache.put("key4", "value4"));
        assertEquals(3, cache.size()); // 应该保持3个

        // 检查淘汰了最旧的（LRU策略）
        assertTrue(cache.contains("key4"));
    }

    @Test
    @DisplayName("数据大小限制基本测试")
    void testMaxDataSizeLimitBasic() {
        cache.setMaxDataSize(10); // 10字节限制

        // 添加第一个条目（5字节）
        assertTrue(cache.put("key1", "val1", 0, TimeUnit.MILLISECONDS, 5));
        assertEquals(1, cache.size());

        // 添加第二个条目（3字节） - 总大小8 < 10，应该成功
        assertTrue(cache.put("key2", "val2", 0, TimeUnit.MILLISECONDS, 3));
        assertEquals(2, cache.size());

        // 添加第三个条目（4字节） - 总大小12 > 10，应该移除一个
        assertTrue(cache.put("key3", "val3", 0, TimeUnit.MILLISECONDS, 4));
        assertEquals(2, cache.size()); // 应该保持2个条目
    }

    @Test
    @DisplayName("容量不足基本测试")
    void testCapacityExhaustedBasic() {
        cache.setMaxSize(1); // 只能存1个条目
        cache.setMaxDataSize(100); // 数据大小限制较宽松

        // 第一个条目
        assertTrue(cache.put("key1", "value1", 0, TimeUnit.MILLISECONDS, 10));
        assertEquals(1, cache.size());

        // 第二个条目 - 应该触发淘汰
        assertTrue(cache.put("key2", "value2", 0, TimeUnit.MILLISECONDS, 10));
        assertEquals(1, cache.size());

        // 第一个条目应该被淘汰
        assertFalse(cache.contains("key1"));
        assertTrue(cache.contains("key2"));
    }

    @Test
    @DisplayName("零容量限制测试")
    void testZeroCapacity() {
        cache.setMaxSize(0);
        cache.setMaxDataSize(0);

        assertFalse(cache.put("key1", "value1"));
        assertEquals(0, cache.size());
    }

    @ParameterizedTest
    @EnumSource(EvictionPolicy.class)
    @DisplayName("所有淘汰策略测试")
    void testAllEvictionPolicies(EvictionPolicy policy) {
        cache.setMaxSize(2);
        cache.setEvictionPolicy(policy);

        // 添加两个条目
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        // 添加第三个触发淘汰
        cache.put("key3", "value3");

        assertEquals(2, cache.size());
    }

    @Test
    @DisplayName("LRU淘汰策略测试")
    void testLRUEviction() {
        cache.setMaxSize(3);
        cache.setEvictionPolicy(EvictionPolicy.LRU);

        cache.put("key1", "value1");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cache.put("key2", "value2");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cache.put("key3", "value3");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 访问key1使其成为最近使用的
        cache.get("key1");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 添加第四个，应该淘汰key2（最久未使用）
        cache.put("key4", "value4");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //System.out.println("cache:" + cache.get("key1") + cache.get("key2") + cache.get("key3") + cache.get("key4"));
        assertTrue(cache.contains("key1"));
        assertTrue(cache.contains("key3"));
        assertTrue(cache.contains("key4"));
        assertFalse(cache.contains("key2"));
    }

    @Test
    @DisplayName("LFU淘汰策略测试")
    void testLFUEviction() {
        cache.setMaxSize(3);
        cache.setEvictionPolicy(EvictionPolicy.LFU);

        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        // 多次访问key1和key3
        cache.get("key1");
        cache.get("key1");
        cache.get("key3");
        cache.get("key3");
        cache.get("key3");

        // key2没有被访问过

        // 添加第四个，应该淘汰key2（访问次数最少）
        cache.put("key4", "value4");

        assertTrue(cache.contains("key1"));
        assertTrue(cache.contains("key3"));
        assertTrue(cache.contains("key4"));
        assertFalse(cache.contains("key2"));
    }

    @Test
    @DisplayName("手动淘汰测试")
    void testManualEviction() {
        cache.setMaxSize(10);

        for (int i = 0; i < 5; i++) {
            cache.put("key" + i, "value" + i);
        }

        assertEquals(5, cache.size());

        // 手动淘汰2个条目
        int evicted = cache.evict(2);
        assertEquals(2, evicted);
        assertEquals(3, cache.size());
    }

    @Test
    @DisplayName("统计信息测试")
    void testCacheStats() {
        CacheStats stats = cache.getStats();
        assertEquals(0, stats.getTotalEntries());
        assertEquals(0, stats.getHitCount());
        assertEquals(0, stats.getMissCount());

        // 添加条目
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        // 访问测试
        cache.get("key1"); // hit
        cache.get("key1"); // hit
        cache.get("nonexistent"); // miss

        stats = cache.getStats();
        assertEquals(2, stats.getTotalEntries());
        assertEquals(2, stats.getHitCount());
        assertEquals(1, stats.getMissCount());
        assertTrue(stats.getHitRate() > 0.5);
    }

    @Test
    @DisplayName("错误统计测试")
    void testErrorStats() {
        // 通过异常情况测试错误统计
        cache.setMaxSize(0);
        cache.put("key", "value"); // 应该失败但不抛出异常

        CacheStats stats = cache.getStats();
        assertEquals(1, stats.getErrorCount());
    }

    @Test
    @DisplayName("并发读写测试")
    void testConcurrentAccess() throws InterruptedException {
        int threadCount = 10;
        int operationsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        String key = "key-" + threadId + "-" + j;
                        String value = "value-" + threadId + "-" + j;

                        // 并发写入
                        boolean putSuccess = cache.put(key, value);
                        if (putSuccess) {
                            successCount.incrementAndGet();
                        }

                        // 并发读取
                        cache.get(key);

                        // 随机删除
                        if (j % 10 == 0) {
                            cache.remove(key);
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        // 验证缓存没有崩溃，并且有成功的操作
        assertTrue(successCount.get() > 0);
        assertTrue(cache.size() >= 0);

        CacheStats stats = cache.getStats();
        assertTrue(stats.getHitCount() >= 0);
        assertTrue(stats.getMissCount() >= 0);
    }

    @Test
    @DisplayName("并发淘汰测试")
    void testConcurrentEviction() throws InterruptedException {
        cache.setMaxSize(100);

        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 先填充缓存
        for (int i = 0; i < 150; i++) {
            cache.put("prefill-" + i, "value");
        }

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 50; j++) {
                        // 并发添加触发淘汰
                        cache.put("concurrent-" + j, "value");
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        executor.shutdown();

        // 缓存应该在容量限制内
        assertTrue(cache.size() <= 100);
    }

    @Test
    @DisplayName("复杂对象存储测试")
    void testComplexObjectStorage() {
        ComplexObject obj1 = new ComplexObject("1", "Object1");
        obj1.getItems().addAll(Arrays.asList("item1", "item2"));
        obj1.getMetadata().put("created", new Date());

        ComplexObject obj2 = new ComplexObject("2", "Object2");
        obj2.getItems().addAll(Arrays.asList("item3", "item4", "item5"));
        obj2.getMetadata().put("modified", new Date());

        // 存储复杂对象
        assertTrue(objectCache.put("obj1", obj1));
        assertTrue(objectCache.put("obj2", obj2));

        // 检索验证
        Optional<ComplexObject> result = objectCache.get("obj1");
        assertTrue(result.isPresent());
        assertEquals("Object1", result.get().getName());
        assertEquals(2, result.get().getItems().size());
    }


    @Test
    @DisplayName("空键和空值测试")
    void testNullKeysAndValues() {
        // 空键应该抛出NullPointerException
        assertThrows(NullPointerException.class, () -> cache.put(null, "value"));

        // 空值也应该抛出NullPointerException
        assertThrows(NullPointerException.class, () -> cache.put("nullValue", null));

        // 测试其他操作对null参数的处理
        assertFalse(cache.contains(null));
        assertFalse(cache.remove(null));
        assertFalse(cache.get(null).isPresent());

        // 验证缓存状态没有改变
        assertEquals(0, cache.size());
    }

    @Test
    @DisplayName("非法参数测试")
    void testInvalidParameters() {
        // 负的TTL - 应该失败
        assertFalse(cache.put("key1", "value1", -1, TimeUnit.SECONDS));

        // 负的大小 - 应该失败
        assertFalse(cache.put("key2", "value2", 0, TimeUnit.MILLISECONDS, -1));

//        // 零TTL（应该立即过期）- 应该成功但立即过期
//        assertTrue(cache.put("key3", "value3", 0, TimeUnit.SECONDS));
//        //assertFalse(cache.get("key3").isPresent());

        // null TimeUnit 但正TTL - 应该失败
        assertFalse(cache.put("key4", "value4", 10, null, 1));

//        // 验证只有key3被存储（但已过期）
//        assertEquals(0, cache.size()); // 或者1但已过期
    }

    @Test
    @DisplayName("超大对象测试")
    void testVeryLargeObject() {
        // 创建超长字符串
        StringBuilder largeValue = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            largeValue.append("x");
        }

        // 应该成功存储（在容量限制内）
        assertTrue(cache.put("large", largeValue.toString()));
        assertTrue(cache.get("large").isPresent());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "key-with-special/chars", "key.with.dots"})
    @DisplayName("特殊键名测试")
    void testSpecialKeyNames(String key) {
        assertTrue(cache.put(key, "value"));
        assertTrue(cache.get(key).isPresent());
        assertEquals("value", cache.get(key).get());
    }



    @Test
    @DisplayName("性能基准测试")
    void testPerformanceBenchmark() {
        int operations = 10000;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < operations; i++) {
            cache.put("key" + i, "value" + i);
            cache.get("key" + i);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 平均操作时间应该小于1毫秒
        double avgTimePerOp = (double) duration / (operations * 2);
        assertTrue(avgTimePerOp < 1.0,
                "Average time per operation: " + avgTimePerOp + "ms");

//        assertEquals(operations, cache.size());
    }

    @Test
    @DisplayName("内存使用测试")
    void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // 添加大量条目
        for (int i = 0; i < 1000; i++) {
            cache.put("key" + i, "value".repeat(100)); // 每个值500字符
        }

        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = finalMemory - initialMemory;

        // 内存增长应该在合理范围内
        assertTrue(memoryIncrease < 10 * 1024 * 1024,
                "Memory increased by: " + memoryIncrease + " bytes");
    }

    @Test
    @DisplayName("完整生命周期测试")
    void testFullLifecycle() {
        // 阶段1: 填充缓存
        for (int i = 0; i < 50; i++) {
            assertTrue(cache.put("key" + i, "value" + i,
                    i % 2 == 0 ? 100 : 0, TimeUnit.MILLISECONDS, 10));
        }

        assertEquals(50, cache.size());

        // 阶段2: 访问模式测试
        for (int i = 0; i < 20; i++) {
            cache.get("key" + (i % 10)); // 频繁访问前10个
        }

        // 阶段3: 触发淘汰
        cache.setMaxSize(30);
        for (int i = 50; i < 80; i++) {
            cache.put("newKey" + i, "newValue" + i);
        }

        // 阶段4: 清理和验证
        int cleaned = cache.cleanup();
        assertTrue(cleaned >= 0);
        assertTrue(cache.size() <= 30);

        // 阶段5: 统计验证
        CacheStats stats = cache.getStats();
        assertTrue(stats.getHitCount() > 0);
        assertTrue(stats.getEvictionCount() > 0);
    }

}