package ps.poc.paimon;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class PocDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PocDemoApplication.class, args);
    }

    @Profile({"default", "dev"})
    @Bean
    public CommandLineRunner demo(ApplicationContext ctx) {
        System.out.println("--->>poc-paimon-rw: PocDemoApplication.demo()");
        return (args) -> {

            Environment environment = ctx.getBean(Environment.class);
            System.out.println("--->>poc-paimon-rw: user.dir = " + environment.getProperty("user.dir"));

            System.out.println("--->>poc-paimon-rw: CommandLineRunner.demo() End");

        };
    }
}

