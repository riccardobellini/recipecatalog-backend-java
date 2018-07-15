package com.bellini.recipecatalog.dao.v1.dishtype;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bellini.recipecatalog.model.v1.DishType;

public interface DishTypeCrudRepository extends PagingAndSortingRepository<DishType, Long> {

//    Collection<DishType> findAll();
    Collection<DishType> findByNameIgnoreCase(String name);
    
    Collection<DishType> findByNameIgnoreCaseContaining(String name);
    
}
