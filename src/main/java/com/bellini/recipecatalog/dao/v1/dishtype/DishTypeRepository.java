package com.bellini.recipecatalog.dao.v1.dishtype;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.DishType;

public interface DishTypeRepository {

    Collection<DishType> findByNameIgnoreCase(String name);

    Page<DishType> findByNameIgnoreCaseContaining(String name, Pageable page);

    Page<DishType> findAll(Pageable pageRequest);

    DishType save(DishType dt);

    DishType save(Long id, DishType dt);

    Optional<DishType> findById(Long id);

    void deleteById(Long id);

    Collection<DishType> findByRecipeId(Long recipeId);

    void attachToRecipe(Long dtId, Long recId);

    int getCount();
}
