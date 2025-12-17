// PageResponse.java
package ps.poc.page.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.List;

@Schema(description = "Paginated response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    
    @Schema(description = "List of data items")
    private List<T> content;
    
    @Schema(description = "Current page number (0-based)")
    private int page;
    
    @Schema(description = "Page size")
    private int size;
    
    @Schema(description = "Total number of elements")
    private long totalElements;
    
    @Schema(description = "Total number of pages")
    private int totalPages;
    
    @Schema(description = "Whether this is the first page")
    private boolean first;
    
    @Schema(description = "Whether this is the last page")
    private boolean last;
    
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast()
        );
    }
}
