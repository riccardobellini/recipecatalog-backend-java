package com.bellini.recipecatalog.service.v1.recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.model.v1.Recipe;

@Service
public interface RecipeService {

    Recipe create(Recipe pub);

    Recipe get(Long id);
    
    Page<Recipe> getAll(Pageable page);

}
