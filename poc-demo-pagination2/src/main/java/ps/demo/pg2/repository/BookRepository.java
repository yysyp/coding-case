// BookRepository.java
package ps.demo.pg2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ps.demo.pg2.entity.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    Optional<Book> findByIsbn(String isbn);

    @Query(value = "SELECT * FROM books WHERE LOWER(title) LIKE LOWER(CONCAT('%', :title, '%'))",
            countQuery = "SELECT COUNT(*) FROM books WHERE LOWER(title) LIKE LOWER(CONCAT('%', :title, '%'))",
            nativeQuery = true)
    Page<Book> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);


    @Query(value = "SELECT * FROM books WHERE LOWER(author) LIKE LOWER(CONCAT('%', :author, '%'))",
            countQuery = "SELECT COUNT(*) FROM books WHERE LOWER(author) LIKE LOWER(CONCAT('%', :author, '%'))",
            nativeQuery = true)
    Page<Book> findByAuthorContainingIgnoreCase(@Param("author") String author, Pageable pageable);

    @Query(value = "SELECT * FROM books b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))",
            countQuery = "SELECT COUNT(*) FROM books b WHERE " +
                    "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
                    "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))",
            nativeQuery = true)
    Page<Book> findByTitleAndAuthorContainingIgnoreCase(
            @Param("title") String title,
            @Param("author") String author,
            Pageable pageable
    );
}
