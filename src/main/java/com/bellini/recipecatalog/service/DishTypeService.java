package com.bellini.recipecatalog.service;

import java.util.Collection;

import com.bellini.recipecatalog.model.v1.DishType;


public interface DishTypeService {

    Iterable<DishType> getAll();

    void create(DishType dt);

    Collection<DishType> get(String name);

    DishType get(Long id);

    DishType update(Long id, DishType dt);
}
