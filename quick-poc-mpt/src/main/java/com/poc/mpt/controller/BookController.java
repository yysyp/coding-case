// BookController.java
package com.poc.mpt.controller;

import com.poc.mpt.common.GenericApiResponse;
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
import com.poc.mpt.dto.BookRequest;
import com.poc.mpt.dto.BookResponse;
import com.poc.mpt.dto.PageResponse;
import com.poc.mpt.service.BookService;

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
        summary = "Create a new book",
        description = "Creates a new book entry in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Book created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<GenericApiResponse<BookResponse>> createBook(
        @Parameter(description = "Book creation request") 
        @Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.createBook(request, "system");
        return new ResponseEntity<>(GenericApiResponse.success(response), HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Get paginated list of books",
        description = "Retrieves a paginated list of all books with sorting options"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved books",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<GenericApiResponse<PageResponse<BookResponse>>> getBooks(
        @Parameter(description = "Page number (0-based)", example = "0") 
        @RequestParam(defaultValue = "0") int page,
        
        @Parameter(description = "Page size", example = "10") 
        @RequestParam(defaultValue = "10") int size,
        
        @Parameter(description = "Sort field (e.g., id, title, author)", example = "id") 
        @RequestParam(defaultValue = "id") String sortBy,
        
        @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC") 
        @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        PageResponse<BookResponse> response = bookService.getBooks(pageable);
        return ResponseEntity.ok(GenericApiResponse.success(response));
    }
    
    @Operation(
        summary = "Get book by ID",
        description = "Retrieves a specific book by its unique identifier"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GenericApiResponse<BookResponse>> getBookById(
        @Parameter(description = "Book ID", example = "1") 
        @PathVariable Long id) {
        BookResponse response = bookService.getBookById(id);
        return ResponseEntity.ok(GenericApiResponse.success(response));
    }
    
    @Operation(
        summary = "Update book",
        description = "Updates an existing book with new information"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book updated successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GenericApiResponse<BookResponse>> updateBook(
        @Parameter(description = "Book ID", example = "1") 
        @PathVariable Long id,
        
        @Parameter(description = "Book update request") 
        @Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.updateBook(id, request, "system");
        return ResponseEntity.ok(GenericApiResponse.success(response));
    }
    
    @Operation(
        summary = "Delete book",
        description = "Deletes a book by its unique identifier"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
        @Parameter(description = "Book ID", example = "1") 
        @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(
        summary = "Search books with pagination",
        description = "Searches books by title and/or author with pagination support"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GenericApiResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<GenericApiResponse<PageResponse<BookResponse>>> searchBooks(
        @Parameter(description = "Title keyword") 
        @RequestParam(required = false) String title,
        
        @Parameter(description = "Author keyword") 
        @RequestParam(required = false) String author,
        
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
        
        PageResponse<BookResponse> response = bookService.searchBooks(title, author, pageable);
        return ResponseEntity.ok(GenericApiResponse.success(response));
    }
}
