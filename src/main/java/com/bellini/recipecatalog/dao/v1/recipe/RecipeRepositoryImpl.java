package com.bellini.recipecatalog.dao.v1.recipe;

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

import com.bellini.recipecatalog.dao.v1.book.BookRepository;
import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.dao.v1.ingredient.IngredientRepository;
import com.bellini.recipecatalog.dao.v1.publication.PublicationRepository;
import com.bellini.recipecatalog.model.v1.Book;
import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.model.v1.Ingredient;
import com.bellini.recipecatalog.model.v1.Publication;
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

    @Autowired
    private PublicationRepository pubRepo;

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
            Optional<Publication> optPub = pubRepo.findByRecipeId(rec.getId());
            if (optPub.isPresent()) {
                rec.setPublication(optPub.get());
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
        // TODO implement transaction
        // first, store recipe
        Long recipeId = storeRecipe(recipe);
        // store other elements only if recipe id is valid
        if (recipeId != null) {
            // check list of ingredients
            Collection<Ingredient> ingredients = recipe.getIngredients();
            for (Ingredient ingr : ingredients) {
                ingredientRepo.attachToRecipe(ingr.getId(), recipeId);
            }
            Collection<DishType> dishtypes = recipe.getDishtypes();
            for (DishType dt : dishtypes) {
                dishTypeRepo.attachToRecipe(dt.getId(), recipeId);
            }
            Book book = recipe.getBook();
            if (book != null) {
                bookRepo.attachToRecipe(book.getId(), recipeId);
            }
            Publication publication = recipe.getPublication();
            if (publication != null) {
                pubRepo.attachToRecipe(publication.getId(), recipeId);
            }

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
        sb.append("(?, UTC_TIMESTAMP(3), UTC_TIMESTAMP(3) ");
        return sb.toString();
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        List<Recipe> result = jdbcTemplate.query(byIdSelectSQL(), (stmt) -> {
            stmt.setLong(1, id);
        }, defaultMapper());
        if (!result.isEmpty()) {
            Recipe rec = result.get(0);
            rec.setDishtypes(dishTypeRepo.findByRecipeId(rec.getId()));
            rec.setIngredients(ingredientRepo.findByRecipeId(rec.getId()));
            Optional<Book> optBook = bookRepo.findByRecipeId(rec.getId());
            if (optBook.isPresent()) {
                rec.setBook(optBook.get());
            }
            Optional<Publication> optPub = pubRepo.findByRecipeId(rec.getId());
            if (optPub.isPresent()) {
                rec.setPublication(optPub.get());
            }
            return Optional.of(rec);
        }
        return Optional.empty();
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
