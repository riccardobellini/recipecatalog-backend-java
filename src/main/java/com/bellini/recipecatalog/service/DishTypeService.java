package com.bellini.recipecatalog.service;

import com.bellini.recipecatalog.model.v1.DishType;

import java.util.List;


public interface DishTypeService {

    Iterable<DishType> getAll();

    void create(DishType dt);

    List<DishType> get(String name);
}
