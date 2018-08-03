package com.bellini.recipecatalog.dao.v1.dishtype;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.DishType;

public interface DishTypeCustomRepository {

    Page<DishType> findByNameIgnoreCaseContaining(String name, Pageable page);
}
