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
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.Recipe;
import com.bellini.recipecatalog.model.v1.RecipeSearchCriteria;

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

    @Override
    public Page<Recipe> findByTitleIgnoreCaseContaining(String title, Pageable page) {
        final String sought = "%" + title + "%";
        List<Recipe> result = jdbcTemplate.query(byTitleIgnoreCaseContainingSelectSQL(false), (stmt) -> {
            int i = 1;
            stmt.setString(i++, sought);
            stmt.setInt(i++, page.getPageNumber() * page.getPageSize());
            stmt.setInt(i++, page.getPageSize());
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
        if (count) {
            sb.append("SELECT COUNT(*) ");
        } else {
            sb.append("SELECT rc.ID, rc.TITLE, rc.IMAGE_KEY, CREATION_TIME, LAST_MODIFICATION_TIME ");
        }
        sb.append("FROM RECIPE rc ");
        sb.append("WHERE LOWER(rc.TITLE) LIKE LOWER(?) ");
        if (!count) {
            sb.append("ORDER BY rc.TITLE ASC ");
            sb.append("LIMIT ?, ?");
        }
        return sb.toString();
    }

    @Override
    public Page<Recipe> search(RecipeSearchCriteria searchCriteria, Pageable page) {
        List<Recipe> result = jdbcTemplate.query(searchSelectSQL(searchCriteria, false), searchStatementSetter(searchCriteria, page, false), defaultMapper());
        Long count = getRowCountSearch(searchCriteria);
        return new PageImpl<>(result, page, count);
    }

    private Long getRowCountSearch(RecipeSearchCriteria searchCriteria) {
        return jdbcTemplate.query(searchSelectSQL(
                searchCriteria, true),
                searchStatementSetter(searchCriteria, null, true),
                (rs) -> {
                    return rs.getLong(1);
                });
    }

    private PreparedStatementSetter searchStatementSetter(RecipeSearchCriteria searchCriteria, Pageable page, boolean count) {
        return (stmt) -> {
            int i = 1;
            if (searchCriteria.isByIngredient()) {
                stmt.setString(i++, "%" + searchCriteria.getIngredient() + "%");
            }
            if (searchCriteria.isByDishtype()) {
                stmt.setString(i++, "%" + searchCriteria.getDishtype() + "%");
            }
            if (searchCriteria.isByBook()) {
                stmt.setString(i++, "%" + searchCriteria.getBook() + "%");
            }
            if (searchCriteria.isByTitle()) {
                stmt.setString(i++, "%" + searchCriteria.getTitleQuery() + "%");
            }
            if (!count) {
                stmt.setLong(i++, page.getOffset());
                stmt.setInt(i++, page.getPageSize());
            }
        };
    }

    private String searchSelectSQL(RecipeSearchCriteria searchCriteria, boolean count) {
        /*
         * SELECT DISTINCT rec.ID, rec.TITLE, rec.IMAGE_KEY, rec.CREATION_TIME, rec.LAST_MODIFICATION_TIME
         * FROM RECIPE rec
         * JOIN INGREDIENT_RECIPE ingrec ON ingrec.ID_RECIPE = rec.ID
         * JOIN INGREDIENT ing ON ingrec.ID_INGREDIENT = ing.ID
         * JOIN DISHTYPE_RECIPE dtrec ON dtrec.ID_RECIPE = rec.ID
         * JOIN DISHTYPE dt ON dtrec.ID_DISHTYPE = dt.ID
         * JOIN BOOK_RECIPE bookrec ON bookrec.ID_RECIPE = rec.ID
         * JOIN BOOK book ON bookrec.ID_BOOK = book.ID
         */
        StringBuilder sb = new StringBuilder();
        if (!count) {
            sb.append("SELECT DISTINCT rec.ID, rec.TITLE, rec.IMAGE_KEY, rec.CREATION_TIME, rec.LAST_MODIFICATION_TIME ");
        } else {
            sb.append("SELECT COUNT(DISTINCT rec.ID) ");
        }
        sb.append("FROM RECIPE rec ");
        // include only needed joins
        if (searchCriteria.isByIngredient()) {
            sb.append("JOIN INGREDIENT_RECIPE ingrec ON ingrec.ID_RECIPE = rec.ID ");
            sb.append("JOIN INGREDIENT ing ON ingrec.ID_INGREDIENT = ing.ID ");
        }
        if (searchCriteria.isByDishtype()) {
            sb.append("JOIN DISHTYPE_RECIPE dtrec ON dtrec.ID_RECIPE = rec.ID ");
            sb.append("JOIN DISHTYPE dt ON dtrec.ID_DISHTYPE = dt.ID ");
        }
        if (searchCriteria.isByBook()) {
            sb.append("JOIN BOOK_RECIPE bookrec ON bookrec.ID_RECIPE = rec.ID ");
            sb.append("JOIN BOOK book ON bookrec.ID_BOOK = book.ID ");
        }
        boolean hasWhere = false;
        if (searchCriteria.isByIngredient()) {
            sb.append("WHERE LOWER(ing.NAME) LIKE LOWER(?) ");
            hasWhere = true;
        }
        if (searchCriteria.isByDishtype()) {
            sb.append(hasWhere ? "AND " : "WHERE ");
            sb.append("LOWER(ing.NAME) LIKE LOWER(?) ");
            hasWhere = true;
        }
        if (searchCriteria.isByBook()) {
            sb.append(hasWhere ? "AND " : "WHERE ");
            sb.append("LOWER(book.TITLE) LIKE LOWER(?) ");
            hasWhere = true;
        }
        if (searchCriteria.isByTitle()) {
            sb.append(hasWhere ? "AND " : "WHERE ");
            sb.append("LOWER(rec.TITLE) LIKE LOWER(?) ");
        }
        if (!count) {
            sb.append("ORDER BY rec.TITLE ASC ");
            sb.append("LIMIT ?, ?");
        }
        return sb.toString();
    }

}
