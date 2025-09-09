package ps.demo.jpademo.cache;


import lombok.Data;
import java.time.LocalDateTime;

/**
 * 缓存条目
 * @param <V> 值类型
 */
@Data
public class CacheEntry<V> {
    /**
     * 缓存键
     */
    private String key;

    /**
     * 缓存值
     */
    private V value;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后访问时间
     */
    private LocalDateTime lastAccessTime;

    /**
     * 过期时间（null表示永不过期）
     */
    private LocalDateTime expireTime;

    /**
     * 访问次数
     */
    private long accessCount;

    /**
     * 条目大小（用于基于大小的淘汰）
     */
    private long size;

    public CacheEntry(String key, V value) {
        this.key = key;
        this.value = value;
        this.createTime = LocalDateTime.now();
        this.lastAccessTime = LocalDateTime.now();
        this.accessCount = 0;
        this.size = 1; // 默认大小
    }

    /**
     * 检查是否过期
     */
    public boolean isExpired() {
        return expireTime != null && LocalDateTime.now().isAfter(expireTime);
    }

    /**
     * 检查是否有效
     */
    public boolean isValid() {
        return !isExpired();
    }

    /**
     * 更新访问时间
     */
    public void updateAccess() {
        this.lastAccessTime = LocalDateTime.now();
        this.accessCount++;
    }

    /**
     * 获取剩余有效时间（秒）
     */
    public Long getRemainingTime() {
        if (expireTime == null) {
            return null;
        }
        return java.time.Duration.between(LocalDateTime.now(), expireTime).getSeconds();
    }
}
