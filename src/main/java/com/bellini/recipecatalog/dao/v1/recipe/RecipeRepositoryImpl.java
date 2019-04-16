package com.bellini.recipecatalog.dao.v1.recipe;

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
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.dao.v1.book.BookRepository;
import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.dao.v1.ingredient.IngredientRepository;
import com.bellini.recipecatalog.model.v1.Book;
import com.bellini.recipecatalog.model.v1.Recipe;

@Repository
public class RecipeRepositoryImpl implements RecipeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DishTypeRepository dishTypeRepo;

    @Autowired
    private IngredientRepository ingredientRepo;

    @Autowired
    private BookRepository bookRepo;

    @Override
    public Page<Recipe> findAll(Pageable page) {
        List<Recipe> result = jdbcTemplate.query(allSelectSQL(), (stmt) -> {
            stmt.setInt(1, page.getPageNumber() * page.getPageSize());
            stmt.setInt(2, page.getPageSize());
        }, defaultMapper());
        Long count = getRowCount();
        for (Recipe rec : result) {
            rec.setDishtypes(dishTypeRepo.findByRecipeId(rec.getId()));
            rec.setIngredients(ingredientRepo.findByRecipeId(rec.getId()));
            Optional<Book> optBook = bookRepo.findByRecipeId(rec.getId());
            if (optBook.isPresent()) {
                rec.setBook(optBook.get());
            }
        }
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
