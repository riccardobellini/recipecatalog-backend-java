package com.bellini.recipecatalog.dao.v1.ingredient;

import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.Ingredient;

public interface IngredientCustomRepository {

    Iterable<Ingredient> findByNameIgnoreCaseContaining(String name, Pageable page);
}
