package com.bellini.recipecatalog.test.service.ingredient;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.bellini.recipecatalog.dao.v1.ingredient.IngredientRepository;
import com.bellini.recipecatalog.exception.ingredient.DuplicateIngredientException;
import com.bellini.recipecatalog.exception.ingredient.NotExistingIngredientException;
import com.bellini.recipecatalog.model.v1.Ingredient;
import com.bellini.recipecatalog.service.v1.ingredient.IngredientService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class IngredientServiceTest {

    private static final String DUMMY_INGREDIENT_NAME = "Asparagi";

    @Autowired
    private IngredientService ingredientSrv;

    @MockBean
    private IngredientRepository ingredientRepo;

    @Test(expected = DuplicateIngredientException.class)
    public void create_shouldThrowExceptionWhenDuplicate() {
        when(ingredientRepo.findByNameIgnoreCase(ArgumentMatchers.eq(DUMMY_INGREDIENT_NAME))).thenReturn(Collections.singleton(dummyIngredient()));
        ingredientSrv.create(dummyIngredient());
    }

    private Ingredient dummyIngredient() {
        final Ingredient ingr = new Ingredient();
        ingr.setId(1L);
        ingr.setCreationTime(Instant.now());
        ingr.setLastModificationTime(Instant.now());
        ingr.setName(DUMMY_INGREDIENT_NAME);
        return ingr;
    }

    @Test
    public void create_shouldCallRepositoryToSave() {
        when(ingredientRepo.findByNameIgnoreCase(ArgumentMatchers.eq(DUMMY_INGREDIENT_NAME))).thenReturn(Collections.emptyList());
        when(ingredientRepo.save(any())).thenReturn(dummyIngredient());

        Ingredient newIngr = ingredientSrv.create(dummyIngredient());
        assertThat(newIngr, notNullValue());

        verify(ingredientRepo).findByNameIgnoreCase(ArgumentMatchers.eq(DUMMY_INGREDIENT_NAME));
        verify(ingredientRepo).save(any());
    }

    @Test(expected = NotExistingIngredientException.class)
    public void get_shouldThrowWhenNotFound() {
        when(ingredientRepo.findById(anyLong())).thenReturn(Optional.empty());
        ingredientSrv.get(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_shouldThrowWhenNullId() {
        ingredientSrv.update(null, dummyIngredient());
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_shouldThrowWhenNullIngredient() {
        ingredientSrv.update(1L, null);
    }

    @Test(expected = NotExistingIngredientException.class)
    public void update_shouldThrowWhenIngredientToBeUpdatedIsMissing() {
        when(ingredientRepo.findByNameIgnoreCase(anyString())).thenReturn(Collections.emptyList());
        when(ingredientRepo.findById(1L)).thenReturn(Optional.empty());
        ingredientSrv.update(1L, dummyIngredient());
    }

    @Test(expected = DuplicateIngredientException.class)
    public void update_shouldThrowWhenIngredientNameIsDuplicate() {
        when(ingredientRepo.findByNameIgnoreCase(anyString())).thenReturn(Collections.singleton(dummyIngredient()));
        ingredientSrv.update(2L, dummyIngredient());
    }

    @Test
    public void update_shouldCallRepositoryToUpdate() {
        when(ingredientRepo.findByNameIgnoreCase(anyString())).thenReturn(Collections.emptyList());
        when(ingredientRepo.findById(anyLong())).thenReturn(Optional.of(dummyIngredient()));
        when(ingredientRepo.save(anyLong(), any())).thenReturn(dummyIngredient());
        Ingredient updated = ingredientSrv.update(2L, dummyIngredient());
        assertThat(updated, notNullValue());

        verify(ingredientRepo).findByNameIgnoreCase(anyString());
        verify(ingredientRepo).findById(anyLong());
        verify(ingredientRepo).save(anyLong(), any());
    }

}
