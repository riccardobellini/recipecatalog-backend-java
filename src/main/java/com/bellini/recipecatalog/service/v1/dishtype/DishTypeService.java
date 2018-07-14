package com.bellini.recipecatalog.service.v1.dishtype;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.model.common.pagination.PaginationInfo;
import com.bellini.recipecatalog.model.v1.DishType;

@Service
public interface DishTypeService {

    Collection<DishType> getAll(PaginationInfo pgInfo);

    DishType create(DishType dt);

    Collection<DishType> get(String name, PaginationInfo pgInfo);

    DishType get(Long id);

    DishType update(Long id, DishType dt);
    
    void delete(Long id);
}
