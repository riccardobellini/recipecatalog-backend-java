package com.bellini.recipecatalog.dao.v1.book;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.Book;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Book> findByTitleIgnoreCase(String title) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Book> findAll(Pageable page) {
        List<Book> result = jdbcTemplate.query(allSelectSQL(), (stmt) -> {
            stmt.setInt(1, page.getPageNumber() * page.getPageSize());
            stmt.setInt(2, page.getPageSize());
        }, defaultMapper());
        Long count = getRowCount();
        return new PageImpl<>(result, page, count);
    }

    private String allSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT bk.ID, bk.TITLE, bk.CREATION_TIME, bk.LAST_MODIFICATION_TIME ");
        sb.append("FROM BOOK bk ");
        sb.append("ORDER BY bk.TITLE ASC ");
        sb.append("LIMIT ?, ?");
        return sb.toString();
    }

    @Override
    public Book save(Book book) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Book> findById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public Page<Book> findByTitleIgnoreCaseContaining(String title, Pageable page) {
        // TODO Auto-generated method stub
        return null;
    }

    private Long getRowCount() {
        return jdbcTemplate.queryForObject(countSQL(), Long.class);
    }

    private String countSQL() {
        return "SELECT COUNT(*) AS total FROM BOOK";
    }

    private RowMapper<Book> defaultMapper() {
        return (rs, row) -> {
            Book book = new Book();
            book.setId(rs.getLong("ID"));
            book.setTitle(rs.getString("TITLE"));
            Timestamp creationTimestamp = rs.getTimestamp("CREATION_TIME");
            if (creationTimestamp != null) {
                book.setCreationTime(Instant.ofEpochMilli(creationTimestamp.getTime()));
            }
            Timestamp lastModificationTimestamp = rs.getTimestamp("LAST_MODIFICATION_TIME");
            if (lastModificationTimestamp != null) {
                book.setLastModificationTime(Instant.ofEpochMilli(lastModificationTimestamp.getTime()));
            }
            return book;
        };
    }
}
