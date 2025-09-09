package ps.demo.jpademo.cache;


/**
 * 缓存淘汰策略
 */
public enum EvictionPolicy {
    /**
     * 最近最少使用
     */
    LRU,

    /**
     * 最不经常使用
     */
    LFU,

    /**
     * 先进先出
     */
    FIFO,

    /**
     * 随机淘汰
     */
    RANDOM,

    /**
     * 基于时间的淘汰
     */
    TTL
}
