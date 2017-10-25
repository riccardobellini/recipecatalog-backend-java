package com.bellini.recipecatalog.repository;

import com.bellini.recipecatalog.model.v1.DishType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DishTypeRepository extends CrudRepository<DishType, Long> {

    List<DishType> findByNameIgnoreCase(String name);

}
