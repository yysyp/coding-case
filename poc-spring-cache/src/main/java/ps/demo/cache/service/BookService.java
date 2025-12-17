package ps.demo.cache.service;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import ps.demo.cache.dto.BookRequest;
import ps.demo.cache.entity.Book;
import ps.demo.cache.exception.BookNotFoundException;
import ps.demo.cache.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing Book entities.
 * Handles business logic and coordination between repository and controller layers.
 */
@Service
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    /**
     * Creates a new book.
     *
     * @param request The book details
     * @param currentUser The user performing the operation
     * @return The created book
     */
    @CacheEvict(value = "books", allEntries = true)

    public Book createBook(BookRequest request, String currentUser) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublicationYear(request.getPublicationYear());
        
        // Set audit fields
        LocalDateTime now = LocalDateTime.now();
        book.setCreatedAt(now);
        book.setCreatedBy(currentUser);
        book.setUpdatedAt(now);
        book.setUpdatedBy(currentUser);
        
        return bookRepository.save(book);
    }
    
    /**
     * Retrieves all books.
     *
     * @return List of all books
     */
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    /**
     * Retrieves a book by its ID.
     *
     * @param id The ID of the book to retrieve
     * @return The requested book
     * @throws BookNotFoundException if book with given ID is not found
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "#id", unless = "#result == null")
    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
        return book.get();
    }
    
    /**
     * Updates an existing book.
     *
     * @param id The ID of the book to update
     * @param request The updated book details
     * @param currentUser The user performing the operation
     * @return The updated book
     * @throws BookNotFoundException if book with given ID is not found
     */
    @CacheEvict(value = "books", key = "#id")

    public Book updateBook(Long id, BookRequest request, String currentUser) {
        Book existingBook = getBookById(id);
        
        existingBook.setTitle(request.getTitle());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setIsbn(request.getIsbn());
        existingBook.setPublicationYear(request.getPublicationYear());
        existingBook.setUpdatedBy(currentUser);
        // updatedAt will be set automatically by @PreUpdate
        
        return bookRepository.save(existingBook);
    }
    
    /**
     * Deletes a book by its ID.
     *
     * @param id The ID of the book to delete
     * @throws BookNotFoundException if book with given ID is not found
     */
    @CacheEvict(value = "books", key = "#id")

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
    
    /**
     * Finds a book by its ISBN.
     *
     * @param isbn The ISBN to search for
     * @return The book with the specified ISBN
     * @throws BookNotFoundException if book with given ISBN is not found
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "#isbn")
    public Book getBookByIsbn(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isEmpty()) {
            throw new BookNotFoundException("Book not found with ISBN: " + isbn);
        }
        return book.get();
    }
    
    /**
     * Finds books by author.
     *
     * @param author The author to search for
     * @return List of books by the specified author
     */
    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
    
    /**
     * Finds books by title containing the specified text.
     *
     * @param title The text to search for in titles
     * @return List of books matching the criteria
     */
    @Transactional(readOnly = true)
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
}
