package com.bellini.recipecatalog.dao.v1.book;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.Book;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Book> findByTitleIgnoreCase(String title) {
        return jdbcTemplate.query(byTitleIgnoreCaseSelectSQL(), (stmt) -> {
            stmt.setString(1, title);
        }, defaultMapper());
    }

    private String byTitleIgnoreCaseSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT bk.ID, bk.TITLE, bk.CREATION_TIME, bk.LAST_MODIFICATION_TIME ");
        sb.append("FROM BOOK bk ");
        sb.append("WHERE LOWER(bk.TITLE) = LOWER(?)");
        return sb.toString();
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
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int changed = jdbcTemplate.update((conn) -> {
            PreparedStatement stmt = conn.prepareStatement(insertSQL(), Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, book.getTitle());
            return stmt;
        }, keyHolder);
        if (changed == 1) {
            long newId = keyHolder.getKey().longValue();
            return findById(newId).get();
        }
        return null;
    }

    private String insertSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO BOOK ");
        sb.append("(TITLE, CREATION_TIME, LAST_MODIFICATION_TIME) VALUES ");
        sb.append("(?, UTC_TIMESTAMP(3), UTC_TIMESTAMP(3))");
        return sb.toString();
    }

    @Override
    public Book save(Long id, Book book) {
        int changed = jdbcTemplate.update((conn) -> {
            PreparedStatement stmt = conn.prepareStatement(updateSQL());
            int i = 1;
            stmt.setString(i++, book.getTitle());
            stmt.setLong(i++, id);
            return stmt;
        });
        if (changed == 1) {
            return findById(id).get();
        }
        return null;
    }

    private String updateSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE BOOK ");
        sb.append("SET TITLE = ?, LAST_MODIFICATION_TIME = UTC_TIMESTAMP(3) ");
        sb.append("WHERE ID = ?");
        return sb.toString();
    }

    @Override
    public Optional<Book> findById(Long id) {
        List<Book> result = jdbcTemplate.query(byIdSelectSQL(), (stmt) -> {
            stmt.setLong(1, id);
        }, defaultMapper());
        return !result.isEmpty() ? Optional.of(result.get(0)) : Optional.empty();
    }

    private String byIdSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT bk.ID, bk.TITLE, bk.CREATION_TIME, bk.LAST_MODIFICATION_TIME ");
        sb.append("FROM BOOK bk ");
        sb.append("WHERE bk.ID = ?");
        return sb.toString();
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(byIdDeleteSQL(), (stmt) -> {
            stmt.setLong(1, id);
        });
    }

    private String byIdDeleteSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM BOOK ");
        sb.append("WHERE ID = ?");
        return sb.toString();
    }

    @Override
    public Page<Book> findByTitleIgnoreCaseContaining(String title, Pageable page) {
        List<Book> result = jdbcTemplate.query(byTitleIgnoreCaseContainingSelectSQL(false), (stmt) -> {
            stmt.setString(1, "%" + title + "%");
            stmt.setInt(2, page.getPageNumber() * page.getPageSize());
            stmt.setInt(3, page.getPageSize());
        }, defaultMapper());
        Long count = getRowCountByTitleIgnoreCaseContaining(title);
        return new PageImpl<>(result, page, count);
    }

    private Long getRowCountByTitleIgnoreCaseContaining(String title) {
        final String sought = "%" + title + "%";
        return jdbcTemplate.queryForObject(byTitleIgnoreCaseContainingSelectSQL(true), Long.class, sought);
    }

    private String byTitleIgnoreCaseContainingSelectSQL(boolean count) {
        StringBuilder sb = new StringBuilder();
        if (!count) {
            sb.append("SELECT bk.ID, bk.TITLE, bk.CREATION_TIME, bk.LAST_MODIFICATION_TIME ");
        } else {
            sb.append("SELECT COUNT(*) ");
        }
        sb.append("FROM BOOK bk ");
        sb.append("WHERE LOWER(bk.TITLE) LIKE LOWER(?) ");
        if (!count) {
            sb.append("ORDER BY bk.TITLE ASC ");
            sb.append("LIMIT ?, ?");
        }
        return sb.toString();
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
