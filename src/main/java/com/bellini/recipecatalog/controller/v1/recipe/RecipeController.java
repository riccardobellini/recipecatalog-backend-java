package com.bellini.recipecatalog.controller.v1.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bellini.recipecatalog.model.v1.Recipe;
import com.bellini.recipecatalog.service.v1.recipe.RecipeService;

@RestController
@RequestMapping("v1/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping(path = "", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Iterable<Recipe>> getAllRecipes(@RequestParam(name = "q", required = false) String name, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Recipe> list = null;
        list = recipeService.getAll(pageable);
        // FIXME convert to DTO
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
