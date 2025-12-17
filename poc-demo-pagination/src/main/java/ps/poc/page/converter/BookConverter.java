// BookConverter.java
package ps.poc.page.converter;

import ps.poc.page.entity.Book;
import ps.poc.page.dto.BookRequest;
import ps.poc.page.dto.BookResponse;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class BookConverter {
    
    public Book toEntity(BookRequest request, String currentUser) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setPublicationDate(request.getPublicationDate());
        
        LocalDateTime now = LocalDateTime.now();
        book.setCreatedAt(now);
        book.setCreatedBy(currentUser);
        book.setUpdatedAt(now);
        book.setUpdatedBy(currentUser);
        
        return book;
    }
    
    public Book toEntity(Book book, BookRequest request, String currentUser) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setPublicationDate(request.getPublicationDate());
        book.setUpdatedBy(currentUser);
        // updatedAt will be set by @PreUpdate
        
        return book;
    }
    
    public BookResponse toResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setDescription(book.getDescription());
        response.setPrice(book.getPrice());
        response.setPublicationDate(book.getPublicationDate());
        response.setCreatedAt(book.getCreatedAt());
        response.setCreatedBy(book.getCreatedBy());
        response.setUpdatedAt(book.getUpdatedAt());
        response.setUpdatedBy(book.getUpdatedBy());
        return response;
    }
}
