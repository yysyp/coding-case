package ps.demo.cache.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating or updating a book.
 */
@Schema(description = "Request object for book creation or update operations")
public class BookRequest {
    
    @NotBlank(message = "Title is required")
    @Schema(description = "Title of the book", example = "Effective Java")
    private String title;
    
    @NotBlank(message = "Author is required")
    @Schema(description = "Author of the book", example = "Joshua Bloch")
    private String author;
    
    @Schema(description = "ISBN of the book", example = "978-0134685991")
    private String isbn;
    
    @NotNull(message = "Publication year is required")
    @Schema(description = "Publication year of the book", example = "2017")
    private Integer publicationYear;

    // Constructors
    public BookRequest() {}

    public BookRequest(String title, String author, String isbn, Integer publicationYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
}
