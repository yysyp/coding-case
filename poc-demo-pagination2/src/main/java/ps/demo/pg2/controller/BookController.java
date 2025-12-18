// BookController.java
package ps.demo.pg2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ps.demo.pg2.dto.BookRequest;
import ps.demo.pg2.dto.BookResponse;
import ps.demo.pg2.dto.BookSearchRequest;
import ps.demo.pg2.dto.PageResponse;
import ps.demo.pg2.service.BookService;

@Tag(name = "Book Management", description = "APIs for managing books with pagination support")
@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    
    private final BookService bookService;
    
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @Operation(
            summary = "Search books with pagination",
            description = "Searches books by title with pagination support"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<PageResponse<BookResponse>> searchBooks(
            @Parameter(description = "Title keyword")
            @RequestParam(required = false) String title,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort field", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        PageResponse<BookResponse> response = bookService.getBooksByTitalInPage(title, pageable);
        return ResponseEntity.ok(response);
    }


    
    @Operation(
        summary = "Search books with pagination",
        description = "Searches books by title with pagination support using POST method"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/search")
    public ResponseEntity<PageResponse<BookResponse>> searchBooks(
        @Parameter(description = "Book search request")
        @Valid @RequestBody BookSearchRequest request) {
        
        Sort.Direction direction = Sort.Direction.fromString(request.getSortDirection().toUpperCase());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(direction, request.getSortBy()));
        
        PageResponse<BookResponse> response = bookService.getBooksByTitalInPage(request.getTitle(), pageable);
        return ResponseEntity.ok(response);
    }
}
