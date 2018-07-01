package com.bellini.recipecatalog.dao.v1.dishtype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.DishType;

@Repository
public class DishTypeDAO implements DishTypeRepository {

	@Override
	public Iterable<DishType> getAll() {
		// TODO Auto-generated method stub
		return null;
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
