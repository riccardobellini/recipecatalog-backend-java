package com.bellini.recipecatalog.dao.v1.recipe;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.Recipe;

public interface RecipeRepository {

    Page<Recipe> findAll(Pageable page);

    Recipe save(Recipe recipe);

    Optional<Recipe> findById(Long id);
}
