package ps.demo.jpademo.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BookDto {

    private Long id;
    private String name;
    private String author;
    private BigDecimal price;
    private LocalDate publishDate;

    // for JPA only, no use
    public BookDto() {
    }

    public BookDto(String name, String author, BigDecimal price, LocalDate publishDate) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", publishDate=" + publishDate +
                '}' +
                "\n";
    }

}