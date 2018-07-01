package com.bellini.recipecatalog.service.v1.dishtype;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.model.v1.DishType;

@Service
public interface DishTypeService {

    Iterable<DishType> getAll();

    void create(DishType dt);

    Collection<DishType> get(String name);

    DishType get(Long id);

    DishType update(Long id, DishType dt);
}
