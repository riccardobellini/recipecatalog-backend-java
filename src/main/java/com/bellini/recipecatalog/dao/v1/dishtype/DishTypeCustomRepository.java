package com.bellini.recipecatalog.dao.v1.dishtype;

import java.util.Collection;

import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.DishType;

public interface DishTypeCustomRepository {

    Collection<DishType> findByNameIgnoreCaseContaining(String name, Pageable page);
}
