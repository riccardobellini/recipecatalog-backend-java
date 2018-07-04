package com.bellini.recipecatalog.dao.v1.dishtype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.DishType;

@Repository
public class DishTypeDAO implements DishTypeRepository {
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	public Iterable<DishType> getAll() {
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
	public void create(DishType dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<DishType> get(String name) {
		DishType dt = new DishType();
		dt.setId(1l);
		dt.setName(name);
		List<DishType> dtList = new ArrayList<>();
		dtList.add(dt);
		return dtList;
	}

	@Override
	public DishType get(Long id) {
		DishType dt = new DishType();
		dt.setId(id);
		dt.setName("Test dish type");
		return dt;
	}

	@Override
	public DishType update(Long id, DishType dt) {
		// TODO Auto-generated method stub
		return null;
	}

}
