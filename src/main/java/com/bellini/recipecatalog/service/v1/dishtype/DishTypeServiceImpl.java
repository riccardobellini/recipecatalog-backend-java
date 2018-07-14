package com.bellini.recipecatalog.service.v1.dishtype;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.exception.dishtype.DuplicateDishTypeException;
import com.bellini.recipecatalog.model.v1.DishType;

@Service
public class DishTypeServiceImpl implements DishTypeService {
	
	@Autowired
	private DishTypeRepository repo;

	@Override
	public Collection<DishType> getAll() {
		return repo.getAll();
	}

	@Override
	public DishType create(DishType dt) {
	    if (repo.getByExactName(dt.getName()) != null) {
	        throw new DuplicateDishTypeException(dt);
	    }
		long id = repo.create(dt);
		return repo.get(id);
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
		if (id == null) {
			throw new IllegalArgumentException("Invalid id");
		}
		if (dt == null || StringUtils.isEmpty(dt.getName())) {
			throw new IllegalArgumentException("Invalid dishtype");
		}
		// search for name conflict
		DishType sought = repo.getByExactName(dt.getName());
		if (sought == null || sought.getId() == id) {
			int updated = repo.update(id, dt);
			if (updated == 1) {
				return repo.get(id);
			}
			throw new RuntimeException("No record updated!");
		}
		throw new DuplicateDishTypeException(dt);
	}

}
