package com.bellini.recipecatalog.dao.v1.recipe;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.Recipe;

@Repository
public class RecipeRepositoryImpl implements RecipeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<Recipe> findAll(Pageable page) {
        List<Recipe> result = jdbcTemplate.query(allSelectSQL(), (stmt) -> {
            stmt.setInt(1, page.getPageNumber() * page.getPageSize());
            stmt.setInt(2, page.getPageSize());
        }, defaultMapper());
        Long count = getRowCount();
        return new PageImpl<>(result, page, count);
    }

    private Long getRowCount() {
        return jdbcTemplate.queryForObject(countSQL(), Long.class);
    }

    private String countSQL() {
        return "SELECT COUNT(*) AS total FROM RECIPE";
    }

    private String allSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT rc.ID, rc.TITLE, rc.IMAGE_KEY, CREATION_TIME, LAST_MODIFICATION_TIME ");
        sb.append("FROM RECIPE rc ");
        sb.append("ORDER BY rc.TITLE ASC ");
        sb.append("LIMIT ?, ?");
        return sb.toString();
    }

    @Override
    public Recipe save(Recipe recipe) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        List<Recipe> result = jdbcTemplate.query(byIdSelectSQL(), (stmt) -> {
            stmt.setLong(1, id);
        }, defaultMapper());
        return !result.isEmpty() ? Optional.of(result.get(0)) : Optional.empty();
    }

    private String byIdSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT rc.ID, rc.TITLE, rc.IMAGE_KEY, CREATION_TIME, LAST_MODIFICATION_TIME ");
        sb.append("FROM RECIPE rc ");
        sb.append("WHERE ic.ID = ?");
        return sb.toString();
    }

    private RowMapper<Recipe> defaultMapper() {
        return (rs, row) -> {
            Recipe recipe = new Recipe();
            // TODO
            return recipe;
        };
    }

}
