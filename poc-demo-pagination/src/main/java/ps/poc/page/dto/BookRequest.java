// BookRequest.java
package ps.poc.page.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Book creation/update request")
@Getter
@Setter
public class BookRequest {
    
    @Schema(description = "Book title", example = "Effective Java")
    @NotBlank(message = "Title is required")
    private String title;
    
    @Schema(description = "Book author", example = "Joshua Bloch")
    @NotBlank(message = "Author is required")
    private String author;
    
    @Schema(description = "Book ISBN", example = "978-0134685991")
    @NotBlank(message = "ISBN is required")
    private String isbn;
    
    @Schema(description = "Book description", example = "The Definitive Guide to Java Programming")
    private String description;
    
    @Schema(description = "Book price", example = "45.99")
    private Double price;
    
    @Schema(description = "Publication date", example = "2017-12-20")
    private java.time.LocalDateTime publicationDate;
}
