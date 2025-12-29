// BookResponse.java
package com.poc.mpt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Book response")
@Getter
@Setter
public class BookResponse {
    
    @Schema(description = "Book ID")
    private Long id;
    
    @Schema(description = "Book title")
    private String title;
    
    @Schema(description = "Book author")
    private String author;
    
    @Schema(description = "Book ISBN")
    private String isbn;
    
    @Schema(description = "Book description")
    private String description;
    
    @Schema(description = "Book price")
    private BigDecimal price;
    
    @Schema(description = "Publication date")
    private Instant publicationDate;
    
    @Schema(description = "Creation timestamp")
    private Instant createdAt;
    
    @Schema(description = "Creator")
    private String createdBy;
    
    @Schema(description = "Last update timestamp")
    private Instant updatedAt;
    
    @Schema(description = "Last updater")
    private String updatedBy;
}
