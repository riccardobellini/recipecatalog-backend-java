package com.bellini.recipecatalog.dao.v1.dishtype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.DishType;

@Repository
public class DishTypeDAO implements DishTypeRepository {
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	public Collection<DishType> getAll() {
		Collection<DishType> result = new ArrayList<>();
		String selectSql = allSelectSQL();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(selectSql)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					DishType dt = new DishType();
					
					dt.setId(rs.getLong("ID"));
					dt.setName(rs.getString("NAME"));
					
					LocalDateTime cldt = rs.getObject("CREATION_TIME", LocalDateTime.class);
					if (cldt != null) {
						dt.setCreationTime(cldt.toInstant(ZoneOffset.UTC));
					}
					
					LocalDateTime mldt = rs.getObject("LAST_MODIFICATION_TIME", LocalDateTime.class);
					if (mldt != null) {
						dt.setLastModificationTime(mldt.toInstant(ZoneOffset.UTC));
					}
					
					result.add(dt);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // TODO
		}
		return result;
	}

	private String allSelectSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ID, NAME, CREATION_TIME, LAST_MODIFICATION_TIME ");
		sb.append("FROM DISHTYPE");
		return sb.toString();
	}

	@Override
	public int create(DishType dt) {
		String insertSql = createInsertSQL();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(insertSql)) {
			int i = 1;
			stmt.setString(i++, dt.getName());
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // TODO
		}
		return 0;
	}
	
	private String createInsertSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO DISHTYPE ");
		sb.append(" (NAME, CREATION_TIME, LAST_MODIFICATION_TIME) ");
		sb.append(" VALUES ");
		sb.append("(?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
		return sb.toString();
	}

	@Override
	public Collection<DishType> get(String name) {
		Collection<DishType> result = new ArrayList<>();
		String selectSql = getByNameSelectSQL();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(selectSql)) {
			String searchFor = "%" + name.toLowerCase() + "%";
			stmt.setString(1, searchFor);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					DishType dt = new DishType();
					
					dt.setId(rs.getLong("ID"));
					dt.setName(rs.getString("NAME"));
					
					LocalDateTime cldt = rs.getObject("CREATION_TIME", LocalDateTime.class);
					if (cldt != null) {
						dt.setCreationTime(cldt.toInstant(ZoneOffset.UTC));
					}
					
					LocalDateTime mldt = rs.getObject("LAST_MODIFICATION_TIME", LocalDateTime.class);
					if (mldt != null) {
						dt.setLastModificationTime(mldt.toInstant(ZoneOffset.UTC));
					}
					
					result.add(dt);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // TODO
		}
		return result;
	}
	
	private String getByNameSelectSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ID, NAME, CREATION_TIME, LAST_MODIFICATION_TIME ");
		sb.append("FROM DISHTYPE ");
		sb.append("WHERE LOWER(NAME) LIKE ?");
		return sb.toString();
	}

	@Override
	public DishType get(Long id) {
		String selectSql = singleSelectSQL();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(selectSql)) {
			stmt.setLong(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					DishType dt = new DishType();
					
					dt.setId(rs.getLong("ID"));
					dt.setName(rs.getString("NAME"));
					
					LocalDateTime cldt = rs.getObject("CREATION_TIME", LocalDateTime.class);
					if (cldt != null) {
						dt.setCreationTime(cldt.toInstant(ZoneOffset.UTC));
					}
					
					LocalDateTime mldt = rs.getObject("LAST_MODIFICATION_TIME", LocalDateTime.class);
					if (mldt != null) {
						dt.setLastModificationTime(mldt.toInstant(ZoneOffset.UTC));
					}
					
					return dt;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // TODO
		}
		return null;
	}

	private String singleSelectSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ID, NAME, CREATION_TIME, LAST_MODIFICATION_TIME ");
		sb.append("FROM DISHTYPE ");
		sb.append("WHERE ID = ?");
		return sb.toString();
	}
	
	@Override
	public int update(Long id, DishType dt) {
		String updateSql = updateSQL();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(updateSql)) {
			int i = 1;
			stmt.setString(i++, dt.getName());
			stmt.setLong(i++, id);
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // TODO
		}
		return 0;
	}
	
	private String updateSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE DISHTYPE ");
		sb.append("SET NAME = ?, ");
		sb.append("LAST_MODIFICATION_TIME = UTC_TIMESTAMP() ");
		sb.append("WHERE ID = ?");
		return sb.toString();
	}
	
	@Override
	public DishType getByExactName(String name) {
		String selectSql = getByExactNameSelectSQL();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(selectSql)) {
			stmt.setString(1, name);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					DishType dt = new DishType();
					
					dt.setId(rs.getLong("ID"));
					dt.setName(rs.getString("NAME"));
					
					LocalDateTime cldt = rs.getObject("CREATION_TIME", LocalDateTime.class);
					if (cldt != null) {
						dt.setCreationTime(cldt.toInstant(ZoneOffset.UTC));
					}
					
					LocalDateTime mldt = rs.getObject("LAST_MODIFICATION_TIME", LocalDateTime.class);
					if (mldt != null) {
						dt.setLastModificationTime(mldt.toInstant(ZoneOffset.UTC));
					}
					
					return dt;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // TODO
		}
		return null;
	}
	
	private String getByExactNameSelectSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ID, NAME, CREATION_TIME, LAST_MODIFICATION_TIME ");
		sb.append("FROM DISHTYPE ");
		sb.append("WHERE LOWER(NAME) = ?");
		return sb.toString();
	}

}
