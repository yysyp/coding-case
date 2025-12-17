package ps.demo.cache.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Book entity representing a book in the library system.
 * Contains all necessary information about a book including audit fields.
 */
@Entity
@Table(name = "books")
@Schema(description = "Book entity representing a book in the library system")
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;
    
    @Column(name = "title", nullable = false)
    @Schema(description = "Title of the book", example = "Effective Java")
    private String title;
    
    @Column(name = "author", nullable = false)
    @Schema(description = "Author of the book", example = "Joshua Bloch")
    private String author;
    
    @Column(name = "isbn", unique = true)
    @Schema(description = "ISBN of the book", example = "978-0134685991")
    private String isbn;
    
    @Column(name = "publication_year")
    @Schema(description = "Publication year of the book", example = "2017")
    private Integer publicationYear;
    
    @Column(name = "created_at")
    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;
    
    @Column(name = "created_by")
    @Schema(description = "User who created the record")
    private String createdBy;
    
    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;
    
    @Column(name = "updated_by")
    @Schema(description = "User who last updated the record")
    private String updatedBy;

    // Constructors
    public Book() {}
    
    public Book(String title, String author, String isbn, Integer publicationYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
