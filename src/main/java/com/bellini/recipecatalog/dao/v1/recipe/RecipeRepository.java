package com.bellini.recipecatalog.dao.v1.recipe;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.Recipe;
import com.bellini.recipecatalog.model.v1.RecipeSearchCriteria;

public interface RecipeRepository {

    Page<Recipe> findAll(Pageable page);

    Page<Recipe> findByTitleIgnoreCaseContaining(String title, Pageable page);

    Recipe save(Recipe recipe);

    Recipe save(Long id, Recipe recipe);

    Optional<Recipe> findById(Long id);

    Page<Recipe> search(RecipeSearchCriteria searchCriteria, Pageable page);

    int getCount();
}
