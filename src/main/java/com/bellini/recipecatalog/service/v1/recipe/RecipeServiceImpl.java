package com.bellini.recipecatalog.service.v1.recipe;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.dao.v1.recipe.RecipeRepository;
import com.bellini.recipecatalog.exception.ingredient.NotExistingIngredientException;
import com.bellini.recipecatalog.model.v1.Recipe;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository repo;

    @Override
    public Recipe create(Recipe pub) {
        // TODO performs check
        return repo.save(pub);
    }

    @Override
    public Recipe get(Long id) {
        Optional<Recipe> optPub = repo.findById(id);
        if (!optPub.isPresent()) {
            throw new NotExistingIngredientException(id);
        }
        return optPub.get();
    }

    @Override
    public Page<Recipe> getAll(Pageable page) {
        return repo.findAll(page);
    }

}
