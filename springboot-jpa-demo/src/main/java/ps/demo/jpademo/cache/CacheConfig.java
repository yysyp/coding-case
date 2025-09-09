package ps.demo.jpademo.cache;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "cache.memory")
public class CacheConfig {

    private long maxSize = 10000;
    private long maxDataSize = 100 * 1024 * 1024; // 100MB
    private String evictionPolicy = "LRU";

    @Bean
    public GenericCache<Object> genericCache() {
        InMemoryGenericCache<Object> cache = new InMemoryGenericCache<>();
        cache.setMaxSize(maxSize);
        cache.setMaxDataSize(maxDataSize);
        cache.setEvictionPolicy(EvictionPolicy.valueOf(evictionPolicy.toUpperCase()));
        return cache;
    }

    // Getter和Setter
    public long getMaxSize() { return maxSize; }
    public void setMaxSize(long maxSize) { this.maxSize = maxSize; }

    public long getMaxDataSize() { return maxDataSize; }
    public void setMaxDataSize(long maxDataSize) { this.maxDataSize = maxDataSize; }

    public String getEvictionPolicy() { return evictionPolicy; }
    public void setEvictionPolicy(String evictionPolicy) { this.evictionPolicy = evictionPolicy; }
}