package ps.poc.page.converter;


import ps.poc.page.dto.BookResponse;
import ps.poc.page.entity.Book;
import org.springframework.stereotype.Component;

/**
 * Converter utility for mapping between Book entity and DTO objects.
 * Handles conversion in both directions while maintaining audit fields.
 */
@Component
public class BookConverter {

    /**
     * Converts a Book entity to BookResponse DTO.
     *
     * @param book The entity to convert
     * @return The converted DTO
     */
    public BookResponse toResponse(Book book) {
        if (book == null) {
            return null;
        }

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getCreatedAt(),
                book.getCreatedBy(),
                book.getUpdatedAt(),
                book.getUpdatedBy()
        );
    }
}
