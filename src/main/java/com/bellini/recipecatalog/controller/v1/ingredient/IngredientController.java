package com.bellini.recipecatalog.controller.v1.ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bellini.recipecatalog.exception.ingredient.NotExistingIngredientException;
import com.bellini.recipecatalog.model.v1.Ingredient;
import com.bellini.recipecatalog.service.v1.ingredient.IngredientService;

@RestController
@RequestMapping("v1/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping(path = "", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Iterable<Ingredient>> getAllIngredients(@RequestParam(name="q", required = false) String name, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Iterable<Ingredient> list = null;
        if (name != null) {
            list = ingredientService.get(name, pageable);
        } else {
            list = ingredientService.getAll(pageable);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingr) {
        Ingredient insertedIngr = ingredientService.create(ingr);
        return new ResponseEntity<>(insertedIngr, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Ingredient> getSingleIngredient(@PathVariable(value = "id") Long id) {
        Ingredient ingr = ingredientService.get(id);

        if (ingr == null) {
            throw new NotExistingIngredientException(id);
        }

        return new ResponseEntity<>(ingr, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Ingredient> update(@PathVariable(value = "id") Long id, @RequestBody Ingredient ingr) {
        return new ResponseEntity<>(ingredientService.update(id, ingr), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public HttpEntity<String> delete(@PathVariable(value = "id") Long id) {
        ingredientService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
