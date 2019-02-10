package com.bellini.recipecatalog.dao.v1.dishtype;

import java.util.Collection;
import java.util.Optional;

import com.bellini.recipecatalog.model.v1.DishType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface DishTypeRepository {

    Collection<DishType> findByNameIgnoreCase(String name);

    Page<DishType> findByNameIgnoreCaseContaining(String name, Pageable page);

    Page<DishType> findAll(PageRequest pageRequest);

    DishType save(DishType dt);

    Optional<DishType> findById(Long id);

    void deleteById(Long id);

}
