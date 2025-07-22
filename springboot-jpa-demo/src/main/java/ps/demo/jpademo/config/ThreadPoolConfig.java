package ps.demo.jpademo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class ThreadPoolConfig {


    @Value("${threadpool corePoolSize:#{T(java.lang.Runtime).getRuntime().availableProcessors()}}")
    private int corePoolSize;
    @Value("${threadpool.maxPoolSize:#{T(java.lang.Runtime).getRuntime().availableProcessors() * 2}}")
    private int maxPoolSize;
    @Value("${threadpool.queueCapacity: 50}")
    private int queueCapacity;
    @Value("${ threadpool.keepALiveSeconds: 60}")
    private int keepAliveSeconds;
    @Value("${threadpool.awaitTerminationSeconds:60}")
    private int awaitTerminationSeconds;
    @Value("${threadpool.threadNamePrefix: p-thread- }")
    private String threadNamePrefix;

    /**
     * @return the configured ThreadPoolTaskExecutor
     */
    @Bean(name = "taskExecutor")
    @ConditionalOnMissingBean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        log.info("Initializing ThreadPoolTaskExecutor with corePoolSize={}, maxPoolSize={}, queueCapacity={}",
                corePoolSize, maxPoolSize, queueCapacity);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setTaskDecorator(new MdcTaskDecorator());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        executor.initialize();
        log.info("ThreadPoolTaskExecutor initialized successfully");
        return executor;
    }

}
