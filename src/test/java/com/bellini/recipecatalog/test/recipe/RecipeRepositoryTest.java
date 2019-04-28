package com.bellini.recipecatalog.test.recipe;

import static org.mockito.Mockito.times;

import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bellini.recipecatalog.dao.v1.book.BookRepository;
import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.dao.v1.ingredient.IngredientRepository;
import com.bellini.recipecatalog.dao.v1.publication.PublicationRepository;
import com.bellini.recipecatalog.dao.v1.recipe.RecipeRepository;
import com.bellini.recipecatalog.model.v1.Book;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:db_seed/recipe/data.sql" })
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = { "classpath:db_seed/recipe/clean-data.sql" })
public class RecipeRepositoryTest {

    @Mock
    private BookRepository bookRepo;

    @Mock
    private DishTypeRepository dishTypeRepo;

    @Mock
    private IngredientRepository ingredientRepo;

    @Mock
    private PublicationRepository pubRepo;

    @Autowired
//    @InjectMocks
    private RecipeRepository recipeRepo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll_shouldReturnCorrectElements() {
        Mockito.when(bookRepo.findByRecipeId(org.mockito.ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(dummyBook()));
        recipeRepo.findAll(PageRequest.of(0, 50));
        Mockito.verify(bookRepo, times(2)).findById(org.mockito.ArgumentMatchers.any(Long.class));
//        assertFalse(result.isPresent());
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
