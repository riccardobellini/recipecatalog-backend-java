package com.bellini.recipecatalog.dao.v1.ingredient;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.Ingredient;

public interface IngredientRepository {

    Collection<Ingredient> findByNameIgnoreCase(String name);

    Page<Ingredient> findByNameIgnoreCaseContaining(String name, Pageable page);

    Page<Ingredient> findAll(Pageable page);

    Ingredient save(Ingredient ingr);

    Ingredient save(Long id, Ingredient ingr);

    Optional<Ingredient> findById(Long id);

    void deleteById(Long id);

    Collection<Ingredient> findByRecipeId(Long recipeId);

    void attachToRecipe(Long ingrId, Long recId);

    int getCount();
}
