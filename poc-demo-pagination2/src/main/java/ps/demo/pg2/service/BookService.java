// BookService.java
package ps.demo.pg2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.demo.pg2.converter.BookConverter;
import ps.demo.pg2.dto.BookRequest;
import ps.demo.pg2.dto.BookResponse;
import ps.demo.pg2.dto.PageResponse;
import ps.demo.pg2.entity.Book;
import ps.demo.pg2.exception.BookNotFoundException;
import ps.demo.pg2.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    private final BookConverter bookConverter;
    
    @Autowired
    public BookService(BookRepository bookRepository, BookConverter bookConverter) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
    }
    
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        return bookConverter.toResponse(book);
    }
    
    public BookResponse createBook(BookRequest request, String currentUser) {
        Book book = bookConverter.toEntity(request, currentUser);
        Book savedBook = bookRepository.save(book);
        return bookConverter.toResponse(savedBook);
    }
    
    public BookResponse updateBook(Long id, BookRequest request, String currentUser) {
        Book existingBook = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        
        Book updatedBook = bookConverter.toEntity(existingBook, request, currentUser);
        Book savedBook = bookRepository.save(updatedBook);
        return bookConverter.toResponse(savedBook);
    }
    
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        bookRepository.delete(book);
    }

    // BookService.java 中的相关方法
    @Transactional(readOnly = true)
    public PageResponse<BookResponse> getBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        List<BookResponse> responses = bookPage.getContent().stream()
                .map(bookConverter::toResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                responses,
                bookPage.getNumber(),
                bookPage.getSize(),
                bookPage.getTotalElements(),
                bookPage.getTotalPages(),
                bookPage.isFirst(),
                bookPage.isLast()
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<BookResponse> searchBooks(String title, String author, Pageable pageable) {
        Page<Book> bookPage;

        if (title != null && author != null) {
            bookPage = bookRepository.findByTitleAndAuthorContainingIgnoreCase(title, author, pageable);
        } else if (title != null) {
            bookPage = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (author != null) {
            bookPage = bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
        } else {
            bookPage = bookRepository.findAll(pageable);
        }

        List<BookResponse> responses = bookPage.getContent().stream()
                .map(bookConverter::toResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                responses,
                bookPage.getNumber(),
                bookPage.getSize(),
                bookPage.getTotalElements(),
                bookPage.getTotalPages(),
                bookPage.isFirst(),
                bookPage.isLast()
        );
    }

}
