package ps.demo.jpademo.cache;


import lombok.Data;

/**
 * 缓存统计信息
 */
@Data
public class CacheStats {
    /**
     * 总条目数
     */
    private long totalEntries;

    /**
     * 有效条目数
     */
    private long activeEntries;

    /**
     * 过期条目数
     */
    private long expiredEntries;

    /**
     * 命中次数
     */
    private long hitCount;

    /**
     * 未命中次数
     */
    private long missCount;

    /**
     * 缓存命中率
     */
    private double hitRate;

    /**
     * 缓存未命中率
     */
    private double missRate;

    /**
     * 缓存大小（字节）
     */
    private long cacheSize;

    /**
     * 最大缓存大小
     */
    private long maxCacheSize;

    /**
     * 缓存使用率
     */
    private double usageRate;

    /**
     * 淘汰的条目数量
     */
    private long evictionCount;

    /**
     * 异常次数
     */
    private long errorCount;

    public CacheStats() {
        updateRates();
    }

    public void updateRates() {
        long totalAccess = hitCount + missCount;
        this.hitRate = totalAccess > 0 ? (double) hitCount / totalAccess : 0.0;
        this.missRate = totalAccess > 0 ? (double) missCount / totalAccess : 0.0;
        this.usageRate = maxCacheSize > 0 ? (double) cacheSize / maxCacheSize : 0.0;
    }
}
