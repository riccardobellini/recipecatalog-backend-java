package com.bellini.recipecatalog.service;

import com.bellini.recipecatalog.model.v1.DishType;


public interface DishTypeService {

    Iterable<DishType> getAll();

    void create(DishType dt);
}
