package com.bellini.recipecatalog.dao.v1.ingredient;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.Ingredient;

@Repository
public class IngredientRepositoryImpl implements IngredientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Ingredient> findByNameIgnoreCase(String name) {
        return jdbcTemplate.query(byNameIgnoreCaseSelectSQL(), (stmt) -> {
            stmt.setString(1, name);
        }, defaultMapper());
    }

    private String byNameIgnoreCaseSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT i.ID, i.NAME, i.CREATION_TIME, i.LAST_MODIFICATION_TIME ");
        sb.append("FROM INGREDIENT i ");
        sb.append("WHERE LOWER(i.NAME) = LOWER(?)");
        return sb.toString();
    }

    @Override
    public Page<Ingredient> findByNameIgnoreCaseContaining(String name, Pageable page) {
        final String sought = "%" + name + "%";
        List<Ingredient> result = jdbcTemplate.query(byNameIgnoreCaseContainingSelectSQL(false), (stmt) -> {
            stmt.setString(1, sought);
            stmt.setInt(2, page.getPageNumber() * page.getPageSize());
            stmt.setInt(3, page.getPageSize());
        }, defaultMapper());
        Long count = getRowCountByNameIgnoreCaseContaining(name);
        return new PageImpl<>(result, PageRequest.of(page.getPageNumber(), page.getPageSize()), count);
    }

    private Long getRowCountByNameIgnoreCaseContaining(String name) {
        final String sought = "%" + name + "%";
        return jdbcTemplate.queryForObject(byNameIgnoreCaseContainingSelectSQL(true), Long.class, sought);
    }

    private String byNameIgnoreCaseContainingSelectSQL(boolean count) {
        StringBuilder sb = new StringBuilder();
        if (count) {
            sb.append("SELECT COUNT(*) AS total ");
        } else {
            sb.append("SELECT i.ID, i.NAME, i.CREATION_TIME, i.LAST_MODIFICATION_TIME ");
        }
        sb.append("FROM INGREDIENT i ");
        sb.append("WHERE LOWER(i.NAME) LIKE LOWER(?) ");
        if (!count) {
            sb.append("LIMIT ?, ?");
        }
        return sb.toString();
    }

    @Override
    public Page<Ingredient> findAll(Pageable page) {
        List<Ingredient> result = jdbcTemplate.query(allSelectSQL(), (stmt) -> {
            stmt.setInt(1, page.getPageNumber() * page.getPageSize());
            stmt.setInt(2, page.getPageSize());
        }, defaultMapper());
        Long count = getRowCount();
        return new PageImpl<>(result, PageRequest.of(page.getPageNumber(), page.getPageSize()), count);
    }

    private String allSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT i.ID, i.NAME, i.CREATION_TIME, i.LAST_MODIFICATION_TIME ");
        sb.append("FROM INGREDIENT i ");
        sb.append("ORDER BY i.NAME ASC ");
        sb.append("LIMIT ?, ?");
        return sb.toString();
    }

    @Override
    public Ingredient save(Ingredient ingr) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int changed = jdbcTemplate.update((conn) -> {
            PreparedStatement stmt = conn.prepareStatement(insertSQL(), Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, ingr.getName());
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
        sb.append("INSERT INTO INGREDIENT ");
        sb.append("(NAME, CREATION_TIME, LAST_MODIFICATION_TIME) VALUES ");
        sb.append("(?, UTC_TIMESTAMP(), UTC_TIMESTAMP())");
        return sb.toString();
    }

    @Override
    public Optional<Ingredient> findById(Long id) {
        List<Ingredient> ingrList = jdbcTemplate.query(byIdSelectSQL(), (stmt) -> {
            stmt.setLong(1, id);
        }, defaultMapper());
        return !ingrList.isEmpty() ? Optional.of(ingrList.get(0)) : Optional.empty();
    }

    private String byIdSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT i.ID, i.NAME, i.CREATION_TIME, i.LAST_MODIFICATION_TIME ");
        sb.append("FROM INGREDIENT i ");
        sb.append("WHERE i.ID = ?");
        return sb.toString();
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub

    }

    private RowMapper<Ingredient> defaultMapper() {
        return (rs, row) -> {
            Ingredient ingr = new Ingredient();
            ingr.setId(rs.getLong("ID"));
            ingr.setName(rs.getString("NAME"));
            Timestamp creationTimestamp = rs.getTimestamp("CREATION_TIME");
            if (creationTimestamp != null) {
                ingr.setCreationTime(Instant.ofEpochMilli(creationTimestamp.getTime()));
            }
            Timestamp lastModificationTimestamp = rs.getTimestamp("LAST_MODIFICATION_TIME");
            if (lastModificationTimestamp != null) {
                ingr.setLastModificationTime(Instant.ofEpochMilli(lastModificationTimestamp.getTime()));
            }
            return ingr;
        };
    }

    private Long getRowCount() {
        return jdbcTemplate.queryForObject(countSQL(), Long.class);
    }

    private String countSQL() {
        return "SELECT COUNT(*) AS total FROM INGREDIENT";
    }

}
