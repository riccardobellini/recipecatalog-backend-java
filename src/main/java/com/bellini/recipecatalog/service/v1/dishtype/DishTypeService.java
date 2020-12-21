package com.bellini.recipecatalog.service.v1.dishtype;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.model.v1.DishType;

@Service
public interface DishTypeService {

    Page<DishType> getAll(Pageable pageable);

    DishType create(DishType dt);

    Page<DishType> get(String name, Pageable pageable);

    DishType get(Long id);

    DishType update(Long id, DishType dt);

    void delete(Long id);

    int getCount();
}
