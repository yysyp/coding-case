package com.poc.mpt.repository;


import com.poc.mpt.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Book entities.
 * Provides CRUD operations and custom queries for books.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    /**
     * Find a book by its ISBN.
     *
     * @param isbn The ISBN to search for
     * @return Optional containing the book if found
     */
    Optional<Book> findByIsbn(String isbn);
    
    /**
     * Find books by author.
     *
     * @param author The author to search for
     * @return List of books by the specified author
     */
    List<Book> findByAuthor(String author);
    
    /**
     * Find books by title containing the specified text.
     *
     * @param title The text to search for in titles
     * @return List of books matching the criteria
     */
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Find books published in a specific year.
     *
     * @param year The publication year
     * @return List of books published in the specified year
     */
    List<Book> findByPublicationYear(Integer year);
    
    /**
     * Find books by author and publication year.
     *
     * @param author The author to search for
     * @param year The publication year
     * @return List of books matching both criteria
     */
    @Query("SELECT b FROM Book b WHERE b.author = :author AND b.publicationYear = :year")
    List<Book> findByAuthorAndPublicationYear(@Param("author") String author, @Param("year") Integer year);
}
