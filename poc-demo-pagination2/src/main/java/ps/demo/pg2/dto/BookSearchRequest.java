package ps.demo.pg2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Book search request")
public class BookSearchRequest {
    
    @Schema(description = "Title keyword", example = "Java")
    private String title;
    
    @Schema(description = "Page number (0-based)", example = "0")
    private int page = 0;
    
    @Schema(description = "Page size", example = "10")
    private int size = 10;
    
    @Schema(description = "Sort field", example = "id")
    private String sortBy = "id";
    
    @Schema(description = "Sort direction", example = "ASC")
    private String sortDirection = "ASC";
}