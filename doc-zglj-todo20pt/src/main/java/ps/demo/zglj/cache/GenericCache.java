package ps.demo.zglj.cache;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 通用缓存接口
 * @param <V> 值类型
 */
public interface GenericCache<V> {

    /**
     * 存储缓存条目（永不过期）
     */
    boolean put(String key, V value);

    /**
     * 存储缓存条目（带过期时间）
     */
    boolean put(String key, V value, long ttl, TimeUnit timeUnit);

    /**
     * 存储缓存条目（带过期时间和大小）
     */
    boolean put(String key, V value, long ttl, TimeUnit timeUnit, long size);

    /**
     * 获取缓存条目
     */
    Optional<V> get(String key);

    /**
     * 获取缓存条目（带元数据）
     */
    Optional<CacheEntry<V>> getEntry(String key);

    /**
     * 移除缓存条目
     */
    boolean remove(String key);

    /**
     * 检查是否包含键
     */
    boolean contains(String key);

    /**
     * 获取所有键
     */
    List<String> keys();

    /**
     * 获取缓存大小（条目数）
     */
    int size();

    /**
     * 获取缓存数据大小（字节）
     */
    long dataSize();

    /**
     * 清理所有缓存
     */
    void clear();

    /**
     * 清理过期条目
     */
    int cleanup();

    /**
     * 获取缓存统计信息
     */
    CacheStats getStats();

    /**
     * 设置最大缓存大小
     */
    void setMaxSize(long maxSize);

    /**
     * 设置淘汰策略
     */
    void setEvictionPolicy(EvictionPolicy policy);

    /**
     * 获取当前淘汰策略
     */
    EvictionPolicy getEvictionPolicy();

    /**
     * 手动触发淘汰
     */
    int evict(int count);
}
