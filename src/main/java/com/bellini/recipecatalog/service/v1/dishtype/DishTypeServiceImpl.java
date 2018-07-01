package com.bellini.recipecatalog.service.v1.dishtype;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.model.v1.DishType;

@Service
public class DishTypeServiceImpl implements DishTypeService {
	
	@Autowired
	private DishTypeRepository repo;

	@Override
	public Iterable<DishType> getAll() {
		return repo.getAll();
	}

	@Override
	public void create(DishType dt) {
		repo.create(dt);
	}

	@Override
	public Collection<DishType> get(String name) {
		return repo.get(name);
	}

	@Override
	public DishType get(Long id) {
		return repo.get(id);
	}

	@Override
	public DishType update(Long id, DishType dt) {
		return repo.update(id, dt);
	}

}
