// BookRepository.java
package ps.demo.pg2.repository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ps.demo.pg2.entity.Book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

    }

    public void save(Book book) {
        namedParameterJdbcTemplate.update(
                "INSERT INTO books (title, author, isbn, description, price, created_at, updated_at, created_by, updated_by) VALUES (:title, :author, :isbn, :description, :price, :created_at, :updated_at, :created_by, :updated_by)",
                new MapSqlParameterSource()
                        .addValue("title", book.getTitle())
                        .addValue("author", book.getAuthor())
                        .addValue("isbn", book.getIsbn())
                        .addValue("description", book.getDescription())
                        .addValue("price", book.getPrice())
                        .addValue("created_at", LocalDateTime.now())
                        .addValue("updated_at", LocalDateTime.now())
                        .addValue("created_by", book.getCreatedBy())
                        .addValue("updated_by", book.getUpdatedBy())
        );
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(
                namedParameterJdbcTemplate.queryForObject(
                "SELECT * FROM books WHERE isbn = :isbn", Map.of("isbn", isbn), new BeanPropertyRowMapper<>(Book.class)
        ));
    }

    public Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM books WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (StringUtils.isNotBlank(title)) {
            queryBuilder.append(" AND LOWER(title) LIKE LOWER(CONCAT('%', :title, '%'))");
            params.addValue("title", title);
        }

        String countQuery = "select count(*) from (" + queryBuilder.toString() + ") as t";
        Long totalCount = namedParameterJdbcTemplate.queryForObject(countQuery, params, Long.class);
        if (totalCount == null) {
            totalCount = 0L;
        }

        queryBuilder.append(" ORDER BY id");
        queryBuilder.append(" LIMIT :limit OFFSET :offset");
        params.addValue("limit", pageable.getPageSize());
        params.addValue("offset", pageable.getOffset());


        List<Book> books = namedParameterJdbcTemplate.query(
                queryBuilder.toString(),
                params,
                new BeanPropertyRowMapper<>(Book.class)
        );

        return new PageImpl<>(books, pageable, totalCount);

    }

}
