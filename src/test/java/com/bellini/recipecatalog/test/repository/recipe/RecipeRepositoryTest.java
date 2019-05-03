package com.bellini.recipecatalog.test.repository.recipe;

import static org.mockito.Mockito.times;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import com.bellini.recipecatalog.dao.v1.book.BookRepository;
import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.dao.v1.ingredient.IngredientRepository;
import com.bellini.recipecatalog.dao.v1.publication.PublicationRepository;
import com.bellini.recipecatalog.dao.v1.recipe.RecipeRepository;
import com.bellini.recipecatalog.dao.v1.recipe.RecipeRepositoryImpl;
import com.bellini.recipecatalog.model.v1.Book;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:db_seed/recipe/data.sql" })
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = { "classpath:db_seed/recipe/clean-data.sql" })
public class RecipeRepositoryTest {

    @MockBean
    private BookRepository bookRepo;

    @MockBean
    private DishTypeRepository dishTypeRepo;

    @MockBean
    private IngredientRepository ingredientRepo;

    @MockBean
    private PublicationRepository pubRepo;

    @Autowired
    private RecipeRepository recipeRepo = new RecipeRepositoryImpl();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll_shouldCallCorrectMethods() {
        Mockito.when(bookRepo.findByRecipeId(org.mockito.ArgumentMatchers.any(Long.class)))
                .thenReturn(Optional.of(dummyBook()));
        Mockito.when(dishTypeRepo.findByRecipeId(org.mockito.ArgumentMatchers.any(Long.class)))
                .thenReturn(Collections.emptyList());
        Mockito.when(ingredientRepo.findByRecipeId(org.mockito.ArgumentMatchers.any(Long.class)))
                .thenReturn(Collections.emptyList());
        Mockito.when(pubRepo.findByRecipeId(org.mockito.ArgumentMatchers.any(Long.class)))
                .thenReturn(Optional.ofNullable(null));
        recipeRepo.findAll(PageRequest.of(0, 50));

        Mockito.verify(bookRepo, times(2)).findByRecipeId(org.mockito.ArgumentMatchers.any(Long.class));
        Mockito.verify(dishTypeRepo, times(2)).findByRecipeId(org.mockito.ArgumentMatchers.any(Long.class));
        Mockito.verify(ingredientRepo, times(2)).findByRecipeId(org.mockito.ArgumentMatchers.any(Long.class));
        Mockito.verify(pubRepo, times(2)).findByRecipeId(org.mockito.ArgumentMatchers.any(Long.class));
    }

    private Book dummyBook() {
        Book bk = new Book();
        bk.setTitle("Dummy");
        bk.setId(1L);
        bk.setCreationTime(Instant.now());
        bk.setLastModificationTime(Instant.now());
        return bk;
    }
}
