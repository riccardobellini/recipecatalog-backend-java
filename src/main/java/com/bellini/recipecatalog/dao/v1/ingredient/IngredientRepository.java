package com.bellini.recipecatalog.dao.v1.ingredient;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bellini.recipecatalog.model.v1.Ingredient;

public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, Long>, IngredientCustomRepository {

    Collection<Ingredient> findByNameIgnoreCase(String name);
    
    Collection<Ingredient> findByNameIgnoreCaseContaining(String name);
    
}
