package com.bellini.recipecatalog.controller.v1.recipe;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bellini.recipecatalog.model.v1.Recipe;
import com.bellini.recipecatalog.model.v1.dto.recipe.RecipeCreationDTO;
import com.bellini.recipecatalog.model.v1.dto.recipe.RecipeDTO;
import com.bellini.recipecatalog.model.v1.mapper.recipe.RecipeResponseMapper;
import com.bellini.recipecatalog.service.v1.recipe.RecipeService;

@RestController
@RequestMapping("v1/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping(path = "", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Iterable<RecipeDTO>> getAllRecipes(@RequestParam(name = "q", required = false) String title, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Recipe> page = null;
        if (title != null) {
            page = recipeService.get(title, pageable);
        } else {
            page = recipeService.getAll(pageable);
        }
        page = recipeService.getAll(pageable);
        List<RecipeDTO> result = page.getContent().stream()
                .map(recipe -> RecipeResponseMapper.getInstance().toDto(recipe))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new PageImpl<>(result, pageable, page.getTotalElements()), HttpStatus.OK);
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE }, path = "")
    public HttpEntity<RecipeDTO> create(@RequestBody RecipeCreationDTO recipeDto) {
        Recipe newRecipe = recipeService.create(recipeDto);
        return new ResponseEntity<RecipeDTO>(RecipeResponseMapper.getInstance().toDto(newRecipe), HttpStatus.CREATED);
    }
}
