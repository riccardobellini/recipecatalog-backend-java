package com.bellini.recipecatalog.dao.v1.dishtype;

import java.util.Collection;

import com.bellini.recipecatalog.model.v1.DishType;

public interface DishTypeRepository {

	Collection<DishType> getAll();

    void create(DishType dt);

    Collection<DishType> get(String name);

    /**
     * 
     * @param id
     * @return null if no element with id is found
     */
    DishType get(Long id);

    /**
     * 
     * @param id
     * @param dt
     * @return number of records updated
     */
    int update(Long id, DishType dt);
    
    /**
     * Retrieve a dish type given its name
     * @param name
     * @return the dishtype or null if not found with the provided name
     */
    DishType getByExactName(String name);
	
}
