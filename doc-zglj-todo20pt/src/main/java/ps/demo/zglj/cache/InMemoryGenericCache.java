package ps.demo.zglj.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * 通用内存缓存实现
 * 支持多种淘汰策略，容量限制，自动清理
 */
@Slf4j
@Component
public class InMemoryGenericCache<V> implements GenericCache<V> {

    // 主缓存存储
    private final Map<String, CacheEntry<V>> cache = new ConcurrentHashMap<>();

    // 读写锁确保并发安全
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // 缓存配置
    private long maxSize = 10000; // 默认最大条目数
    private long maxDataSize = 100 * 1024 * 1024; // 默认100MB
    private EvictionPolicy evictionPolicy = EvictionPolicy.LRU;

    // 统计信息
    private final CacheStats stats = new CacheStats();
    private long totalDataSize = 0;

    @Override
    public boolean put(String key, V value) {
        return putInternal(key, value, null, 1);
    }

    @Override
    public boolean put(String key, V value, long ttl, TimeUnit timeUnit) {
        // 参数验证
        if (ttl < 0) {
            log.warn("Invalid TTL: {}", ttl);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return false;
        }

        if (timeUnit == null && ttl > 0) {
            log.warn("TimeUnit is null but TTL is positive: {}", ttl);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return false;
        }
        LocalDateTime expireTime = LocalDateTime.now().plusNanos(timeUnit.toNanos(ttl));
        return putInternal(key, value, expireTime, 1);
    }

    @Override
    public boolean put(String key, V value, long ttl, TimeUnit timeUnit, long size) {
        // 参数验证
        if (ttl < 0) {
            log.warn("Invalid TTL: {}", ttl);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return false;
        }

        if (size <= 0) {
            log.warn("Invalid size: {}", size);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return false;
        }

        if (timeUnit == null && ttl > 0) {
            log.warn("TimeUnit is null but TTL is positive: {}", ttl);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return false;
        }

        LocalDateTime expireTime = null;
        if (timeUnit != null && ttl > 0) {
            expireTime = LocalDateTime.now().plusNanos(timeUnit.toNanos(ttl));
        }

        return putInternal(key, value, expireTime, size);
    }

    private boolean putInternal(String key, V value, LocalDateTime expireTime, long entrySize) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(value, "Value cannot be null");

        try {
            lock.writeLock().lock();

            // 检查是否超过容量限制
            boolean needsEviction = cache.size() >= maxSize || totalDataSize + entrySize > maxDataSize;
            System.out.println("Needs eviction: " + needsEviction);
            System.out.println("Cache size: " + cache.size() + ", Max size: " + maxSize);
            System.out.println("Total data size: " + totalDataSize + ", Entry size: " + entrySize + ", Max data size: " + maxDataSize);

            if (needsEviction) {
                int evicted = evict(1);
                System.out.println("Evicted count: " + evicted);
                if (evicted == 0) {
                    // 无法淘汰任何条目
                    log.warn("Cache is full and eviction failed, cannot put key: {}", key);
                    stats.setErrorCount(stats.getErrorCount() + 1);
                    return false;
                }
                // 重新检查容量
                if (cache.size() >= maxSize || totalDataSize + entrySize > maxDataSize) {
                    log.warn("Still over capacity after eviction");
                    stats.setErrorCount(stats.getErrorCount() + 1);
                    return false;
                }
            }

            CacheEntry<V> entry = new CacheEntry<>(key, value);
            entry.setExpireTime(expireTime);
            entry.setSize(entrySize);

            // 如果键已存在，先移除旧条目
            if (cache.containsKey(key)) {
                removeInternal(key);
            }

            cache.put(key, entry);
            totalDataSize += entrySize;

            updateStats();
            log.debug("Cache put successful - key: {}, size: {}", key, entrySize);
            return true;

        } catch (Exception e) {
            log.error("Error putting cache entry for key: {}", key, e);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<V> get(String key) {
        try {
            lock.readLock().lock();
            CacheEntry<V> entry = cache.get(key);

            if (entry == null) {
                stats.setMissCount(stats.getMissCount() + 1);
                updateStats();
                return Optional.empty();
            }

            if (entry.isExpired()) {
                // 异步清理过期条目
                scheduleCleanup();
                stats.setMissCount(stats.getMissCount() + 1);
                updateStats();
                return Optional.empty();
            }

            entry.updateAccess();
            stats.setHitCount(stats.getHitCount() + 1);
            updateStats();

            return Optional.of(entry.getValue());

        } catch (Exception e) {
            log.error("Error getting cache entry for key: {}", key, e);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return Optional.empty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<CacheEntry<V>> getEntry(String key) {
        try {
            lock.readLock().lock();
            CacheEntry<V> entry = cache.get(key);

            if (entry == null || entry.isExpired()) {
                stats.setMissCount(stats.getMissCount() + 1);
                updateStats();
                return Optional.empty();
            }

            entry.updateAccess();
            stats.setHitCount(stats.getHitCount() + 1);
            updateStats();

            return Optional.of(entry);

        } catch (Exception e) {
            log.error("Error getting cache entry for key: {}", key, e);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return Optional.empty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean remove(String key) {
        try {
            lock.writeLock().lock();
            return removeInternal(key);
        } catch (Exception e) {
            log.error("Error removing cache entry for key: {}", key, e);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean removeInternal(String key) {
        CacheEntry<V> removed = cache.remove(key);
        if (removed != null) {
            totalDataSize -= removed.getSize();
            updateStats();
            log.debug("Cache remove successful - key: {}", key);
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(String key) {
        try {
            Optional<CacheEntry<V>> entry = getEntry(key);
            return entry.isPresent();
        } catch (Exception e) {
            log.error("Error checking cache contains key: {}", key, e);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return false;
        }
    }

    @Override
    public List<String> keys() {
        try {
            lock.readLock().lock();
            return cache.values().stream()
                    .filter(entry -> !entry.isExpired())
                    .map(CacheEntry::getKey)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting cache keys", e);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return Collections.emptyList();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        try {
            lock.readLock().lock();
            return (int) cache.values().stream()
                    .filter(entry -> !entry.isExpired())
                    .count();
        } catch (Exception e) {
            log.error("Error getting cache size", e);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return 0;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public long dataSize() {
        try {
            lock.readLock().lock();
            return totalDataSize;
        } catch (Exception e) {
            log.error("Error getting cache data size", e);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return 0;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.writeLock().lock();
            cache.clear();
            totalDataSize = 0;
            updateStats();
            log.info("Cache cleared successfully");
        } catch (Exception e) {
            log.error("Error clearing cache", e);
            stats.setErrorCount(stats.getErrorCount() + 1);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int cleanup() {
        try {
            lock.writeLock().lock();
            int cleanedCount = 0;
            Iterator<Map.Entry<String, CacheEntry<V>>> iterator = cache.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, CacheEntry<V>> entry = iterator.next();
                if (entry.getValue().isExpired()) {
                    totalDataSize -= entry.getValue().getSize();
                    iterator.remove();
                    cleanedCount++;
                }
            }

            if (cleanedCount > 0) {
                log.info("Cleaned up {} expired cache entries", cleanedCount);
            }

            updateStats();
            return cleanedCount;
        } catch (Exception e) {
            log.error("Error during cache cleanup", e);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return 0;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public CacheStats getStats() {
        try {
            lock.readLock().lock();
            stats.updateRates();
            return stats;
        } catch (Exception e) {
            log.error("Error getting cache stats", e);
            return new CacheStats();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void setMaxSize(long maxSize) {
        try {
            lock.writeLock().lock();
            this.maxSize = maxSize;
            log.info("Cache max size set to: {}", maxSize);
        } catch (Exception e) {
            log.error("Error setting max size", e);
            stats.setErrorCount(stats.getErrorCount() + 1);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setEvictionPolicy(EvictionPolicy policy) {
        try {
            lock.writeLock().lock();
            this.evictionPolicy = policy;
            log.info("Cache eviction policy set to: {}", policy);
        } catch (Exception e) {
            log.error("Error setting eviction policy", e);
            stats.setErrorCount(stats.getErrorCount() + 1);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public EvictionPolicy getEvictionPolicy() {
        return evictionPolicy;
    }

    @Override
    public int evict(int count) {
        try {
            lock.writeLock().lock();
            if (cache.isEmpty()) {
                return 0;
            }

            List<CacheEntry<V>> entriesToEvict = selectEntriesToEvict(count);
            int evictedCount = 0;

            for (CacheEntry<V> entry : entriesToEvict) {
                if (removeInternal(entry.getKey())) {
                    evictedCount++;
                    stats.setEvictionCount(stats.getEvictionCount() + 1);
                }
            }

            log.debug("Evicted {} cache entries using policy: {}", evictedCount, evictionPolicy);
            return evictedCount;

        } catch (Exception e) {
            log.error("Error during cache eviction", e);
            stats.setErrorCount(stats.getErrorCount() + 1);
            return 0;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 根据淘汰策略选择要淘汰的条目
     */
    private List<CacheEntry<V>> selectEntriesToEvict(int count) {
        List<CacheEntry<V>> allEntries = new ArrayList<>(cache.values());

        switch (evictionPolicy) {
            case LRU:
                return allEntries.stream()
                        .filter(entry -> !entry.isExpired())
                        .sorted(Comparator.comparing(CacheEntry::getLastAccessTime))
                        .limit(count)
                        .collect(Collectors.toList());

            case LFU:
                return allEntries.stream()
                        .filter(entry -> !entry.isExpired())
                        .sorted(Comparator.comparingLong(CacheEntry::getAccessCount))
                        .limit(count)
                        .collect(Collectors.toList());

            case FIFO:
                return allEntries.stream()
                        .filter(entry -> !entry.isExpired())
                        .sorted(Comparator.comparing(CacheEntry::getCreateTime))
                        .limit(count)
                        .collect(Collectors.toList());

            case TTL:
                return allEntries.stream()
                        .filter(entry -> entry.getExpireTime() != null)
                        .sorted(Comparator.comparing(CacheEntry::getExpireTime))
                        .limit(count)
                        .collect(Collectors.toList());

            case RANDOM:
                Collections.shuffle(allEntries);
                return allEntries.stream()
                        .filter(entry -> !entry.isExpired())
                        .limit(count)
                        .collect(Collectors.toList());

            default:
                return allEntries.stream()
                        .filter(entry -> !entry.isExpired())
                        .limit(count)
                        .collect(Collectors.toList());
        }
    }

    /**
     * 定期清理过期条目（每分钟执行一次）
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void scheduledCleanup() {
        try {
            int cleaned = cleanup();
            if (cleaned > 0) {
                log.debug("Scheduled cleanup removed {} expired entries", cleaned);
            }
        } catch (Exception e) {
            log.error("Error during scheduled cleanup", e);
            stats.setErrorCount(stats.getErrorCount() + 1);
        }
    }

    /**
     * 异步安排清理任务
     */
    private void scheduleCleanup() {
        try {
            new Thread(this::cleanup).start();
        } catch (Exception e) {
            log.warn("Failed to schedule async cleanup", e);
        }
    }

    /**
     * 更新统计信息
     */
    private void updateStats() {
        try {
            long total = cache.size();
            long active = cache.values().stream()
                    .filter(entry -> !entry.isExpired())
                    .count();
            long expired = total - active;

            stats.setTotalEntries(total);
            stats.setActiveEntries(active);
            stats.setExpiredEntries(expired);
            stats.setCacheSize(totalDataSize);
            stats.setMaxCacheSize(maxDataSize);
            stats.updateRates();
        } catch (Exception e) {
            log.warn("Error updating cache stats", e);
        }
    }

    /**
     * 设置最大数据大小
     */
    public void setMaxDataSize(long maxDataSize) {
        try {
            lock.writeLock().lock();
            this.maxDataSize = maxDataSize;
            log.info("Cache max data size set to: {} bytes", maxDataSize);
        } catch (Exception e) {
            log.error("Error setting max data size", e);
            stats.setErrorCount(stats.getErrorCount() + 1);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
