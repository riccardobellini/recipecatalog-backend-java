package com.bellini.recipecatalog.dao.v1.recipe;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
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
        Long recipeId = storeRecipe(recipe);
        if (recipeId != null) {
            return findById(recipeId).get();
        }
        return null;
    }

    private Long storeRecipe(Recipe recipe) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int changed = jdbcTemplate.update((conn) -> {
            PreparedStatement stmt = conn.prepareStatement(recipeInsertSQL(), Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, recipe.getTitle());
            return stmt;
        }, keyHolder);
        if (changed == 1) {
            long newId = keyHolder.getKey().longValue();
            return newId;
        }
        return null;
    }

    private String recipeInsertSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO RECIPE ");
        sb.append("(TITLE, CREATION_TIME, LAST_MODIFICATION_TIME) ");
        sb.append("VALUES ");
        sb.append("(?, UTC_TIMESTAMP(3), UTC_TIMESTAMP(3))");
        return sb.toString();
    }

    @Override
    public Recipe save(Long id, Recipe recipe) {
        int changed = jdbcTemplate.update((conn) -> {
            PreparedStatement stmt = conn.prepareStatement(recipeUpdateSQL());
            stmt.setString(1, recipe.getTitle());
            stmt.setLong(2, id);
            return stmt;
        });
        if (changed == 1) {
            return findById(id).get();
        }
        return null;
    }

    private String recipeUpdateSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE RECIPE ");
        sb.append("SET TITLE = ?, LAST_MODIFICATION_TIME = UTC_TIMESTAMP(3) ");
        sb.append("WHERE ID = ?");
        return sb.toString();
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
        sb.append("WHERE rc.ID = ?");
        return sb.toString();
    }

    private RowMapper<Recipe> defaultMapper() {
        return (rs, row) -> {
            Recipe recipe = new Recipe();
            recipe.setId(rs.getLong("ID"));
            recipe.setTitle(rs.getString("TITLE"));
            Timestamp creationTimestamp = rs.getTimestamp("CREATION_TIME");
            if (creationTimestamp != null) {
                recipe.setCreationTime(Instant.ofEpochMilli(creationTimestamp.getTime()));
            }
            Timestamp lastModificationTimestamp = rs.getTimestamp("LAST_MODIFICATION_TIME");
            if (lastModificationTimestamp != null) {
                recipe.setLastModificationTime(Instant.ofEpochMilli(lastModificationTimestamp.getTime()));
            }
            return recipe;
        };
    }

}
