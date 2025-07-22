package ps.demo.commonlibx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CommonApplication implements ApplicationRunner {

    public static void main(String[] args) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        //long freeMemory = Runtime.getRuntime().freeMemory();
        log.info("--->>maxMemory={}m, totalMemory={}m, usedMemory={}m",
                maxMemory / 1024 / 1024, totalMemory / 1024 / 1024,
                (maxMemory - totalMemory) / 1024 / 1024);
        log.info("System.getenv() = {}", System.getenv());
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("Available processors = {}", processors);
        SpringApplication.run(CommonApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("ApplicationArguments is: {}", args);
    }

}
