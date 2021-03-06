package com.bellini.recipecatalog.dao.v1.dishtype;

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

import com.bellini.recipecatalog.model.v1.DishType;

@Repository
public class DishTypeRepositoryImpl implements DishTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<DishType> findByNameIgnoreCase(String name) {
        return jdbcTemplate.query(byNameIgnoreCaseSelectSQL(), (stmt) -> {
            stmt.setString(1, name);
        }, defaultMapper());
    }

    private String byNameIgnoreCaseSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT dt.ID, dt.NAME, dt.CREATION_TIME, dt.LAST_MODIFICATION_TIME ");
        sb.append("FROM DISHTYPE dt ");
        sb.append("WHERE LOWER(dt.NAME) = LOWER(?)");
        return sb.toString();
    }

    @Override
    public Page<DishType> findByNameIgnoreCaseContaining(String name, Pageable page) {
        List<DishType> result = jdbcTemplate.query(byNameIgnoreCaseContainingSelectSQL(false), (stmt) -> {
            stmt.setString(1, "%" + name + "%");
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
            sb.append("SELECT dt.ID, dt.NAME, dt.CREATION_TIME, dt.LAST_MODIFICATION_TIME ");
        }
        sb.append("FROM DISHTYPE dt ");
        sb.append("WHERE LOWER(dt.NAME) LIKE LOWER(?) ");
        if (!count) {
            sb.append("ORDER BY dt.NAME ASC ");
            sb.append("LIMIT ?, ?");
        }
        return sb.toString();
    }

    @Override
    public Page<DishType> findAll(Pageable pageRequest) {
        List<DishType> result = jdbcTemplate.query(allSelectSQL(), (stmt) -> {
            stmt.setInt(1, pageRequest.getPageNumber() * pageRequest.getPageSize());
            stmt.setInt(2, pageRequest.getPageSize());
        }, defaultMapper());
        Long count = getRowCount();
        return new PageImpl<>(result, PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize()), count);
    }

    private Long getRowCount() {
        return jdbcTemplate.queryForObject(countSQL(), Long.class);
    }

    private String allSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT dt.ID, dt.NAME, dt.CREATION_TIME, dt.LAST_MODIFICATION_TIME ");
        sb.append("FROM DISHTYPE dt ");
        sb.append("ORDER BY dt.NAME ASC ");
        sb.append("LIMIT ?, ?");
        return sb.toString();
    }

    @Override
    public DishType save(DishType dt) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int changed = jdbcTemplate.update((conn) -> {
            PreparedStatement stmt = conn.prepareStatement(insertSQL(), Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, dt.getName());
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
        sb.append("INSERT INTO DISHTYPE ");
        sb.append("(NAME, CREATION_TIME, LAST_MODIFICATION_TIME) VALUES ");
        sb.append("(?, UTC_TIMESTAMP(3), UTC_TIMESTAMP(3))");
        return sb.toString();
    }

    @Override
    public DishType save(Long id, DishType dt) {
        int changed = jdbcTemplate.update((conn) -> {
            PreparedStatement stmt = conn.prepareStatement(updateSQL());
            int i = 1;
            stmt.setString(i++, dt.getName());
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
        sb.append("UPDATE DISHTYPE ");
        sb.append("SET NAME = ?, LAST_MODIFICATION_TIME = UTC_TIMESTAMP(3) ");
        sb.append("WHERE ID = ?");
        return sb.toString();
    }

    @Override
    public Optional<DishType> findById(Long id) {
        List<DishType> dtList = jdbcTemplate.query(byIdSelectSQL(), (stmt) -> {
            stmt.setLong(1, id);
        }, defaultMapper());
        return !dtList.isEmpty() ? Optional.of(dtList.get(0)) : Optional.empty();
    }

    private String byIdSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT dt.ID, dt.NAME, dt.CREATION_TIME, dt.LAST_MODIFICATION_TIME ");
        sb.append("FROM DISHTYPE dt ");
        sb.append("WHERE dt.ID = ?");
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
        sb.append("DELETE FROM DISHTYPE ");
        sb.append("WHERE ID = ?");
        return sb.toString();
    }

    private RowMapper<DishType> defaultMapper() {
        return (rs, row) -> {
            DishType dt = new DishType();
            dt.setId(rs.getLong("ID"));
            dt.setName(rs.getString("NAME"));
            Timestamp creationTimestamp = rs.getTimestamp("CREATION_TIME");
            if (creationTimestamp != null) {
                dt.setCreationTime(Instant.ofEpochMilli(creationTimestamp.getTime()));
            }
            Timestamp lastModificationTimestamp = rs.getTimestamp("LAST_MODIFICATION_TIME");
            if (lastModificationTimestamp != null) {
                dt.setLastModificationTime(Instant.ofEpochMilli(lastModificationTimestamp.getTime()));
            }
            return dt;
        };
    }

    private String countSQL() {
        return "SELECT COUNT(*) AS total FROM DISHTYPE";
    }

    @Override
    public Collection<DishType> findByRecipeId(Long recipeId) {
        return jdbcTemplate.query(byRecipeIdSelectSQL(), (stmt) -> {
            stmt.setLong(1, recipeId);
        }, defaultMapper());
    }

    private String byRecipeIdSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT dt.ID, dt.NAME, dt.CREATION_TIME, dt.LAST_MODIFICATION_TIME ");
        sb.append("FROM DISHTYPE dt JOIN DISHTYPE_RECIPE dtr ON dt.ID = dtr.ID_DISHTYPE ");
        sb.append("WHERE dtr.ID_RECIPE = ? ");
        sb.append("ORDER BY dt.NAME ASC");
        return sb.toString();
    }

    @Override
    public void attachToRecipe(Long dtId, Long recId) {
        jdbcTemplate.update((conn) -> {
            PreparedStatement stmt = conn.prepareStatement(dishtypeRecipeInsertSQL());
            stmt.setLong(1, recId);
            stmt.setLong(2, dtId);
            return stmt;
        });
    }

    private String dishtypeRecipeInsertSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO DISHTYPE_RECIPE ");
        sb.append("(ID_RECIPE, ID_DISHTYPE) ");
        sb.append("VALUES ");
        sb.append("(?, ?)");
        return sb.toString();
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(countSQL(), Integer.class);
    }

}
