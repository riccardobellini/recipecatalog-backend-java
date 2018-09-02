package com.bellini.recipecatalog.dao.v1.recipe;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.Recipe;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long> {

}
