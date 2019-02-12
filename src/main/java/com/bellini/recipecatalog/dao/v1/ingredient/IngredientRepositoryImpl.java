package com.bellini.recipecatalog.dao.v1.ingredient;

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
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.Ingredient;

@Repository
public class IngredientRepositoryImpl implements IngredientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Ingredient> findByNameIgnoreCase(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Ingredient> findByNameIgnoreCaseContaining(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Ingredient> findByNameIgnoreCaseContaining(String name, Pageable page) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Ingredient> findById(Long id) {
        // TODO Auto-generated method stub
        return null;
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
