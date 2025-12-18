package ps.demo.pg2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Book search request")
public class BookSearchRequest {
    
    @Schema(description = "List of title keywords", example = "[\"Java\", \"Spring\"]")
    private List<String> titles;
    
    @Schema(description = "Page number (0-based)", example = "0")
    private int page = 0;
    
    @Schema(description = "Page size", example = "10")
    private int size = 10;
    
    @Schema(description = "Sort field", example = "id")
    private String sortBy = "id";
    
    @Schema(description = "Sort direction", example = "ASC")
    private String sortDirection = "ASC";
}