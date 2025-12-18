package ps.demo.pg2;

import cn.hutool.core.date.DateField;
import cn.hutool.core.util.RandomUtil;
import ps.demo.pg2.common.SettingTool;
import ps.demo.pg2.entity.Book;
import ps.demo.pg2.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class Pocdemopagination2Application {

    public static void main(String[] args) {
        System.setProperty("JASYPT_ENCRYPTOR_PASS", SettingTool.getConfigByKey("JASYPT_ENCRYPTOR_PASS"));
        SpringApplication application = new SpringApplication(Pocdemopagination2Application.class);
        application.run(args);
    }

    @Profile({"default", "dev"})
    @Bean
    public CommandLineRunner demo(ApplicationContext ctx) {
        System.out.println("username=" + SettingTool.getConfigByKey("username"));
        System.out.println("Initializing data...");
        initializeBookData(ctx);
        System.out.println("Data initialization completed.");
        System.out.println("--->>poc-demo-pagination2: Pocdemopagination2Application.demo()");
        return (args) -> {

            Environment environment = ctx.getBean(Environment.class);
            System.out.println("--->>poc-demo-pagination2: user.dir = " + environment.getProperty("user.dir"));

            System.out.println("--->>poc-demo-pagination2: CommandLineRunner.demo() End");
            System.out.println("--->>poc-demo-pagination2 swagger ui: http://localhost:29235/swagger-ui.html");

        };
    }

    private void initializeBookData(ApplicationContext ctx) {
        BookRepository bookRepository = ctx.getBean(BookRepository.class);


        String [] users = {"admin", "system", "user1", "user2"};
        for (int i = 1; i <= 59; i++) {
            Book book = new Book();
            book.setTitle("Book Title " + i);
            book.setAuthor("Author " + i);
            book.setIsbn("ISBN-" + String.format("%05d", i));
            book.setDescription("Description for book " + i);
            book.setPrice(new BigDecimal(String.format("%.2f", (i * 10.5))));
            //book.setCreatedAt(RandomUtil.randomDate(null, DateField.YEAR, 2000, 2020).toLocalDateTime());
            //book.setUpdatedAt(book.getCreatedAt());
            book.setCreatedBy(users[i % users.length]);
            book.setUpdatedBy(users[i % users.length]);
            bookRepository.save(book);
        }

        System.out.println("--->>poc-demo-pagination: Generated 25 book records");
    }

}

