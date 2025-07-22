package ps.demo.jpademo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String author;
    private BigDecimal price;
    private LocalDate publishDate;

    // for JPA only, no use

    public Book(String name, String author, BigDecimal price, LocalDate publishDate) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.publishDate = publishDate;
    }


}