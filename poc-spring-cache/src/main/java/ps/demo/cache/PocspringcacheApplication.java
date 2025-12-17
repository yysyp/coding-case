package ps.demo.cache;

import org.springframework.cache.annotation.EnableCaching;
import ps.demo.cache.common.SettingTool;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@EnableCaching
@SpringBootApplication
public class PocspringcacheApplication {

    public static void main(String[] args) {
        System.setProperty("JASYPT_ENCRYPTOR_PASS", SettingTool.getConfigByKey("JASYPT_ENCRYPTOR_PASS"));
        SpringApplication application = new SpringApplication(PocspringcacheApplication.class);
        application.run(args);
    }

    @Profile({"default", "dev"})
    @Bean
    public CommandLineRunner demo(ApplicationContext ctx) {
        System.out.println("username=" + SettingTool.getConfigByKey("username"));

        System.out.println("--->>poc-spring-cache: PocspringcacheApplication.demo()");
        return (args) -> {

            Environment environment = ctx.getBean(Environment.class);
            System.out.println("--->>poc-spring-cache: user.dir = " + environment.getProperty("user.dir"));

            System.out.println("--->>poc-spring-cache: CommandLineRunner.demo() End");

        };
    }
}

