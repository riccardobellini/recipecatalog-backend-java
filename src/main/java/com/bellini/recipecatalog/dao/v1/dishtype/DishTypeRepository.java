package com.bellini.recipecatalog.dao.v1.dishtype;

import java.util.Collection;

import com.bellini.recipecatalog.model.v1.DishType;

public interface DishTypeRepository {

    Iterable<DishType> getAll();

    void create(DishType dt);

    Collection<DishType> get(String name);

    /**
     * 
     * @param id
     * @return null if no element with id is found
     */
    DishType get(Long id);

    DishType update(Long id, DishType dt);
	
}
