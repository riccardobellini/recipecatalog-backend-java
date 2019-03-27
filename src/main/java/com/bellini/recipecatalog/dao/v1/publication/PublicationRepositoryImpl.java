package com.bellini.recipecatalog.dao.v1.publication;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.Publication;

@Repository
public class PublicationRepositoryImpl implements PublicationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Publication save(Publication pub) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int changed = jdbcTemplate.update((conn) -> {
            PreparedStatement stmt = conn.prepareStatement(insertSQL(), Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, pub.getVolume());
            stmt.setInt(2, pub.getYear());
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
        sb.append("INSERT INTO PUBLICATION ");
        sb.append("(VOLUME, YEAR, CREATION_TIME, LAST_MODIFICATION_TIME) ");
        sb.append("VALUES (?, ?, UTC_TIMESTAMP(), UTC_TIMESTAMP())");
        return sb.toString();
    }

    @Override
    public Optional<Publication> findByVolumeAndYear(Integer volume, Integer year) {
        List<Publication> result = jdbcTemplate.query(byVolumeAndYearSelectSQL(), (stmt) -> {
            stmt.setInt(1, volume);
            stmt.setInt(2, year);
        }, defaultMapper());
        return !result.isEmpty() ? Optional.of(result.get(0)) : Optional.empty();
    }

    private String byVolumeAndYearSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT pub.ID, pub.VOLUME, pub.YEAR, pub.CREATION_TIME, pub.LAST_MODIFICATION_TIME ");
        sb.append("FROM PUBLICATION pub ");
        sb.append("WHERE pub.VOLUME = ? AND pub.YEAR = ?");
        return sb.toString();
    }

    @Override
    public Optional<Publication> findById(Long id) {
        List<Publication> pubList = jdbcTemplate.query(byIdSelectSQL(), (stmt) -> {
            stmt.setLong(1, id);
        }, defaultMapper());
        return !pubList.isEmpty() ? Optional.of(pubList.get(0)) : Optional.empty();
    }

    private String byIdSelectSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT pub.ID, pub.VOLUME, pub.YEAR, pub.CREATION_TIME, pub.LAST_MODIFICATION_TIME ");
        sb.append("FROM PUBLICATION pub ");
        sb.append("WHERE pub.ID = ?");
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
        sb.append("DELETE FROM PUBLICATION ");
        sb.append("WHERE ID = ?");
        return sb.toString();
    }

    private RowMapper<Publication> defaultMapper() {
        return (rs, row) -> {
            Publication pub = new Publication();
            pub.setId(rs.getLong("ID"));
            pub.setVolume(rs.getInt("VOLUME"));
            pub.setYear(rs.getInt("YEAR"));
            Timestamp creationTimestamp = rs.getTimestamp("CREATION_TIME");
            if (creationTimestamp != null) {
                pub.setCreationTime(Instant.ofEpochMilli(creationTimestamp.getTime()));
            }
            Timestamp lastModificationTimestamp = rs.getTimestamp("LAST_MODIFICATION_TIME");
            if (lastModificationTimestamp != null) {
                pub.setLastModificationTime(Instant.ofEpochMilli(lastModificationTimestamp.getTime()));
            }
            return pub;
        };
    }

}