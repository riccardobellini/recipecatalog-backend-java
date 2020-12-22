package com.bellini.recipecatalog.dao.v1.publication;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.Publication;

@Repository
public class PublicationRepositoryImpl implements PublicationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Publication> defaultMapper() {
        return (rs, row) -> {
            Publication pub = new Publication();
            pub.setVolume(rs.getInt("PROG"));
            pub.setMonth(rs.getInt("MONTH"));
            pub.setYear(rs.getInt("YEAR"));
            return pub;
        };
    }

    @Override
    public Optional<Publication> findByRecipeId(Long id) {
        List<Publication> result = jdbcTemplate.query(byRecipeIdSelectSQL(), (stmt) -> {
            stmt.setLong(1, id);
        }, defaultMapper());
        return !result.isEmpty() ? Optional.of(result.get(0)) : Optional.empty();
    }

    private String byRecipeIdSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT br.MONTH, br.PROG, br.YEAR ");
        sb.append("FROM BOOK_RECIPE br ");
        sb.append("WHERE br.ID_RECIPE = ?");
        return sb.toString();
    }

    @Override
    public void attachToRecipe(Long recId, Publication pub) {
        jdbcTemplate.update((conn) -> {
            PreparedStatement stmt = conn.prepareStatement(publicationRecipeUpdateSQL());
            stmt.setInt(1, pub.getVolume());
            stmt.setInt(2, pub.getMonth());
            stmt.setInt(3, pub.getYear());
            stmt.setLong(4, recId);
            return stmt;
        });
    }

    private String publicationRecipeUpdateSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE BOOK_RECIPE ");
        sb.append("SET PROG = ?, MONTH = ?, YEAR = ? ");
        sb.append("WHERE ID_RECIPE = ?");
        return sb.toString();
    }

}
