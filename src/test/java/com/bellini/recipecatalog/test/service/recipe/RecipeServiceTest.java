package com.bellini.recipecatalog.test.service.recipe;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;

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
    private static final String DUMMY_INGREDIENT_2_NAME = "Spaghetti";

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

    @Test
    public void create_shouldCallRepositoriesToAttachRelatedEntities() {
        when(recipeRepo.save(any())).thenReturn(dummyRecipeSlim());

        final Recipe toCreate = dummyRecipe();
        Recipe theNewRecipe = recipeSrv.create(toCreate);

        assertThat(theNewRecipe, notNullValue());

        verify(ingrRepo, atLeastOnce()).attachToRecipe(anyLong(), anyLong());
        verify(dtRepo, atLeastOnce()).attachToRecipe(anyLong(), anyLong());
        verify(bookRepo).attachToRecipe(anyLong(), anyLong());
        verify(pubRepo).attachToRecipe(anyLong(), anyLong());
    }

    private Recipe dummyRecipeSlim() {
        // returns a recipe with no other entities attached
        Recipe rec = new Recipe();
        rec.setCreationTime(Instant.now());
        rec.setLastModificationTime(Instant.now());
        rec.setId(1L);
        rec.setTitle(RECIPE_TITLE);
        return rec;
    }

    private Recipe dummyRecipe() {
        Recipe rec = new Recipe();

        rec.setCreationTime(Instant.now());
        rec.setLastModificationTime(Instant.now());
        rec.setId(1L);
        rec.setTitle(RECIPE_TITLE);

        rec.getIngredients().add(dummyIngredient(1L, DUMMY_INGREDIENT_1_NAME));
        rec.getIngredients().add(dummyIngredient(2L, DUMMY_INGREDIENT_2_NAME));
        rec.getDishtypes().add(dummyDishType());
        rec.setBook(dummyBook());
        rec.setPublication(dummyPublication());

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
        pub.setCreationTime(Instant.now());
        pub.setLastModificationTime(Instant.now());
        pub.setId(1L);
        pub.setVolume(1);
        pub.setYear(2019);
        return pub;
    }
}
