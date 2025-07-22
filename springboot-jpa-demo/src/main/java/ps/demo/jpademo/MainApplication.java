package ps.demo.jpademo;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.math3.random.RandomGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import ps.demo.jpademo.entity.Book;
import ps.demo.jpademo.entity.Role;
import ps.demo.jpademo.repository.BookRepository;
import ps.demo.jpademo.repository.RoleRepository;
import ps.demo.jpademo.service.RoleService;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@SpringBootApplication
@EnableEncryptableProperties
public class MainApplication {

    public static void main(String[] args) {

        SpringApplication.run(MainApplication.class, args);
    }

    // Spring runs CommandLineRunner bean when Spring Boot App starts
    @Profile("dev")
    @Bean
    public CommandLineRunner demo(ApplicationContext ctx, BookRepository bookRepository, RoleRepository roleRepository, RoleService roleService) {
        return (args) -> {

//            Environment environment = ctx.getBean(Environment.class);
//            String value = environment.getProperty("encpwdtest.test1");
//            System.out.println("---value ===>>" + value);
//            value = environment.getProperty("testvar");
//            System.out.println("---value ===>>" + value);

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            int i = 1;
            for (String beanName : beanNames) {
                log.info("Bean [] name: {}", i++, beanName);
            }

            bookRepository.save(new Book("A Guide to the Bodhisattva Way of Life1", "Santideva", new BigDecimal("15.41"), LocalDate.of(2023, 8, 31)));
            bookRepository.save(new Book("The Life-Changing Magic of Tidying Up2", "Marie Kondo", new BigDecimal("9.69"), LocalDate.of(2023, 7, 31)));
            bookRepository.save(new Book("Refactoring: Improving the Design of Existing Code3", "Martin Fowler", new BigDecimal("47.99"), LocalDate.of(2023, 6, 10)));

            for (int x = 4; x < 1008; x++) {
                bookRepository.save(new Book("book name " + x, "author_"+x, new BigDecimal(SecureRandom.getInstanceStrong().nextDouble(2, 101)), LocalDate.of(SecureRandom.getInstanceStrong().nextInt(1990, 2025), SecureRandom.getInstanceStrong().nextInt(1, 13), SecureRandom.getInstanceStrong().nextInt(1, 29))));
            }

            // find book by ID
            Optional<Book> optionalBook = bookRepository.findById(1L);
            optionalBook.ifPresent(obj -> {
                log.info("Book found with findById(1L):");
                log.info("--------------------------------");
                log.info(obj.toString());
                log.info("\n");
            });

            // find book by published date after
            log.info("Book found with findByPublishedDateAfter(), after 2023/7/1");
            log.info("--------------------------------------------");
            bookRepository.findByPublishedDateAfter(LocalDate.of(2023, 7, 1)).forEach(b -> {
                log.info(b.toString());
                log.info("\n");
            });

            // initiate role data

            roleRepository.save(Role.builder().roleName("root").description("root").createdTime(LocalDateTime.now()).createdBy("sys").build());
            roleRepository.save(Role.builder().roleName("a").parentId(1L).description("a").createdTime(LocalDateTime.now()).createdBy("sys").build());
            roleRepository.save(Role.builder().roleName("b").parentId(1L).description("b").createdTime(LocalDateTime.now()).createdBy("sys").build());
            roleRepository.save(Role.builder().roleName("a1").parentId(2L).description("a1").createdTime(LocalDateTime.now()).createdBy("sys").build());
            roleRepository.save(Role.builder().roleName("a2").parentId(2L).description("a2").createdTime(LocalDateTime.now()).createdBy("sys").build());
            roleRepository.save(Role.builder().roleName("b1").parentId(3L).description("b1").createdTime(LocalDateTime.now()).createdBy("sys").build());


            log.info("Print out role list1: ");
            List<Role> list1 = roleRepository.findAllChildRoles(1L);
            list1.forEach((e) -> {
                log.info("role: {}", e);
            });

            log.info("Print out role list2: ");
            List<Role> list2 = roleRepository.findAllChildRoles(2L);
            list2.forEach((e) -> {
                log.info("role: {}", e);
            });

            //list2.get(0).setRoleName("haha test~");
            roleService.testAutoSaveModification();
            roleService.testNotAutoSaveModification(list2.get(0));

        };
    }

}
