package ps.poc.page.controller;


import ps.poc.page.converter.BookConverter;
import ps.poc.page.dto.BookRequest;
import ps.poc.page.dto.BookResponse;
import ps.poc.page.entity.Book;
import ps.poc.page.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing books.
 * Provides endpoints for CRUD operations on books.
 */
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {
    
    private final BookService bookService;
    private final BookConverter bookConverter;
    
    @Autowired
    public BookController(BookService bookService, BookConverter bookConverter) {
        this.bookService = bookService;
        this.bookConverter = bookConverter;
    }
    
    /**
     * Creates a new book.
     *
     * @param request The book details
     * @return The created book
     */
    @Operation(summary = "Create a new book", description = "Creates a new book record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<BookResponse> createBook(
            @Parameter(description = "Book details") 
            @Valid @RequestBody BookRequest request) {
        
        Book book = bookService.createBook(request, "api-user");
        BookResponse response = bookConverter.toResponse(book);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Retrieves all books.
     *
     * @return List of all books
     */
    @Operation(summary = "Get all books", description = "Retrieves all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved books",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        List<BookResponse> responses = books.stream()
                .map(bookConverter::toResponse)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    
    /**
     * Retrieves a book by ID.
     *
     * @param id The book ID
     * @return The requested book
     */
    @Operation(summary = "Get book by ID", description = "Retrieves a specific book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved book",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(description = "ID of the book to retrieve") 
            @PathVariable Long id) {
        
        Book book = bookService.getBookById(id);
        BookResponse response = bookConverter.toResponse(book);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Updates an existing book.
     *
     * @param id      The book ID
     * @param request The updated book details
     * @return The updated book
     */
    @Operation(summary = "Update book", description = "Updates an existing book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @Parameter(description = "ID of the book to update") 
            @PathVariable Long id,
            @Parameter(description = "Updated book details") 
            @Valid @RequestBody BookRequest request) {
        
        Book book = bookService.updateBook(id, request, "api-user");
        BookResponse response = bookConverter.toResponse(book);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Deletes a book by ID.
     *
     * @param id The book ID
     * @return Confirmation of deletion
     */
    @Operation(summary = "Delete book", description = "Deletes a book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID of the book to delete") 
            @PathVariable Long id) {
        
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /**
     * Retrieves a book by ISBN.
     *
     * @param isbn The book ISBN
     * @return The requested book
     */
    @Operation(summary = "Get book by ISBN", description = "Retrieves a specific book by its ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved book",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(
            @Parameter(description = "ISBN of the book to retrieve") 
            @PathVariable String isbn) {
        
        Book book = bookService.getBookByIsbn(isbn);
        BookResponse response = bookConverter.toResponse(book);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Retrieves books by author.
     *
     * @param author The author name
     * @return List of books by the author
     */
    @Operation(summary = "Get books by author", description = "Retrieves all books by a specific author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved books",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookResponse>> getBooksByAuthor(
            @Parameter(description = "Author name to search for") 
            @PathVariable String author) {
        
        List<Book> books = bookService.getBooksByAuthor(author);
        List<BookResponse> responses = books.stream()
                .map(bookConverter::toResponse)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    
    /**
     * Retrieves books by title.
     *
     * @param title The title text to search for
     * @return List of books matching the title
     */
    @Operation(summary = "Get books by title", description = "Retrieves all books containing the specified text in their title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved books",
                    content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/title/{title}")
    public ResponseEntity<List<BookResponse>> getBooksByTitle(
            @Parameter(description = "Title text to search for") 
            @PathVariable String title) {
        
        List<Book> books = bookService.getBooksByTitle(title);
        List<BookResponse> responses = books.stream()
                .map(bookConverter::toResponse)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
