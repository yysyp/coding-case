package ps.demo.cache.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Response DTO for book queries.
 */
@Schema(description = "Response object for book queries")
public class BookResponse {
    
    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;
    
    @Schema(description = "Title of the book", example = "Effective Java")
    private String title;
    
    @Schema(description = "Author of the book", example = "Joshua Bloch")
    private String author;
    
    @Schema(description = "ISBN of the book", example = "978-0134685991")
    private String isbn;
    
    @Schema(description = "Publication year of the book", example = "2017")
    private Integer publicationYear;
    
    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;
    
    @Schema(description = "User who created the record")
    private String createdBy;
    
    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;
    
    @Schema(description = "User who last updated the record")
    private String updatedBy;

    // Constructors
    public BookResponse() {}

    public BookResponse(Long id, String title, String author, String isbn, 
                       Integer publicationYear, LocalDateTime createdAt, String createdBy,
                       LocalDateTime updatedAt, String updatedBy) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
