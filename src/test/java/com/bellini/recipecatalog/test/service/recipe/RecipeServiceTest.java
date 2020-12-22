package com.bellini.recipecatalog.test.service.recipe;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.bellini.recipecatalog.dao.v1.book.BookRepository;
import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.dao.v1.ingredient.IngredientRepository;
import com.bellini.recipecatalog.dao.v1.publication.PublicationRepository;
import com.bellini.recipecatalog.dao.v1.recipe.RecipeRepository;
import com.bellini.recipecatalog.exception.recipe.NotExistingRecipeException;
import com.bellini.recipecatalog.model.v1.Book;
import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.model.v1.Ingredient;
import com.bellini.recipecatalog.model.v1.Publication;
import com.bellini.recipecatalog.model.v1.Recipe;
import com.bellini.recipecatalog.service.v1.recipe.RecipeService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RecipeServiceTest {

    private static final String RECIPE_TITLE = "Pasta alla norma";

    private static final String DUMMY_INGREDIENT_1_NAME = "Melanzane";

    private static final String DUMMY_DISHTYPE_NAME = "Primi piatti - pasta";

    private static final String DUMMY_BOOK_TITLE = "Da Noi";

    @Autowired
    private RecipeService recipeSrv;

    @MockBean
    private RecipeRepository recipeRepo;

    @MockBean
    private IngredientRepository ingrRepo;

    @MockBean
    private DishTypeRepository dtRepo;

    @MockBean
    private BookRepository bookRepo;

    @MockBean
    private PublicationRepository pubRepo;

    private Recipe dummyRecipeSlim() {
        // returns a recipe with no other entities attached
        Recipe rec = new Recipe();
        rec.setCreationTime(Instant.now());
        rec.setLastModificationTime(Instant.now());
        rec.setId(1L);
        rec.setTitle(RECIPE_TITLE);
        return rec;
    }

    private Ingredient dummyIngredient(long id, String name) {
        final Ingredient ingr = new Ingredient();
        ingr.setId(id);
        ingr.setCreationTime(Instant.now());
        ingr.setLastModificationTime(Instant.now());
        ingr.setName(name);
        return ingr;
    }

    private DishType dummyDishType() {
        DishType dt = new DishType();
        dt.setId(1L);
        dt.setName(DUMMY_DISHTYPE_NAME);
        dt.setCreationTime(Instant.now());
        dt.setLastModificationTime(Instant.now());
        return dt;
    }

    private Book dummyBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle(DUMMY_BOOK_TITLE);
        return book;
    }

    private Publication dummyPublication() {
        final Publication pub = new Publication();
        pub.setVolume(1);
        pub.setYear(2019);
        return pub;
    }

    @Test(expected = NotExistingRecipeException.class)
    public void get_shouldThrowExceptionWhenNotFound() {
        when(recipeRepo.findById(anyLong())).thenReturn(Optional.empty());
        recipeSrv.get(1L);
    }

    @Test
    public void get_shouldCallOtherRepositoriesAndSetEntities() {
        when(recipeRepo.findById(anyLong())).thenReturn(Optional.of(dummyRecipeSlim()));
        when(dtRepo.findByRecipeId(anyLong())).thenReturn(Collections.singleton(dummyDishType()));
        when(ingrRepo.findByRecipeId(anyLong())).thenReturn(Collections.singleton(dummyIngredient(1L, DUMMY_INGREDIENT_1_NAME)));
        when(bookRepo.findByRecipeId(anyLong())).thenReturn(Optional.of(dummyBook()));
        when(pubRepo.findByRecipeId(anyLong())).thenReturn(Optional.of(dummyPublication()));

        Recipe theRecipe = recipeSrv.get(1L);
        assertFalse(theRecipe.getDishtypes().isEmpty());
        assertFalse(theRecipe.getIngredients().isEmpty());
        assertThat(theRecipe.getBook(), notNullValue());
        assertThat(theRecipe.getPublication(), notNullValue());

        verify(recipeRepo).findById(anyLong());
        verify(dtRepo, atLeastOnce()).findByRecipeId(anyLong());
        verify(ingrRepo, atLeastOnce()).findByRecipeId(anyLong());
        verify(bookRepo).findByRecipeId(anyLong());
        verify(pubRepo).findByRecipeId(anyLong());
    }

    @Test
    public void get_shouldCallOtherRepositoriesAndLeaveNullFieldsIfEntitiesAreNotFound() {
        when(recipeRepo.findById(anyLong())).thenReturn(Optional.of(dummyRecipeSlim()));
        when(dtRepo.findByRecipeId(anyLong())).thenReturn(Collections.singleton(dummyDishType()));
        when(ingrRepo.findByRecipeId(anyLong())).thenReturn(Collections.singleton(dummyIngredient(1L, DUMMY_INGREDIENT_1_NAME)));
        when(bookRepo.findByRecipeId(anyLong())).thenReturn(Optional.empty());
        when(pubRepo.findByRecipeId(anyLong())).thenReturn(Optional.empty());

        Recipe theRecipe = recipeSrv.get(1L);
        assertFalse(theRecipe.getDishtypes().isEmpty());
        assertFalse(theRecipe.getIngredients().isEmpty());
        assertThat(theRecipe.getBook(), nullValue());
        assertThat(theRecipe.getPublication(), nullValue());

        verify(recipeRepo).findById(anyLong());
        verify(dtRepo, atLeastOnce()).findByRecipeId(anyLong());
        verify(ingrRepo, atLeastOnce()).findByRecipeId(anyLong());
        verify(bookRepo).findByRecipeId(anyLong());
        verify(pubRepo).findByRecipeId(anyLong());
    }
}
