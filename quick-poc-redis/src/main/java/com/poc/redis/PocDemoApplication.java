package com.poc.redis;

import com.poc.redis.entity.User;
import com.poc.redis.repository.UserRepository;
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
    public CommandLineRunner demo(ApplicationContext ctx, UserRepository userRepository) {
        System.out.println("--->>PocDemoApplication.demo()");
        return (args) -> {


            Environment environment = ctx.getBean(Environment.class);
            System.out.println("--->>user.dir = " + environment.getProperty("user.dir"));

            userRepository.save(new User("1", "user1", "user1@gmail.com", 20));
            userRepository.save(new User("2", "user2", "user2@gmail.com", 22));
            userRepository.save(new User("3", "user3", "user3@gmail.com", 33));


            System.out.println("--->>All users:");
            userRepository.findAll().forEach(System.out::println);
            System.out.println("--->>CommandLineRunner.demo() End");
        };
    }

}
