package com.bellini.recipecatalog.service.v1.ingredient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.model.v1.Ingredient;

@Service
public interface IngredientService {

    Page<Ingredient> getAll(Pageable pageable);

    Ingredient create(Ingredient dt);

    Page<Ingredient> get(String name, Pageable pageable);

    Ingredient get(Long id);

    Ingredient update(Long id, Ingredient dt);
    
    void delete(Long id);
}