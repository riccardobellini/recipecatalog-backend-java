package com.bellini.recipecatalog.repository;

import com.bellini.recipecatalog.model.v1.DishType;
import org.springframework.data.repository.CrudRepository;

public interface DishTypeRepository extends CrudRepository<DishType, Long> {
}
