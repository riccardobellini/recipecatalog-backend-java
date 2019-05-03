package com.bellini.recipecatalog.test.repository.recipe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bellini.recipecatalog.dao.v1.recipe.RecipeRepository;
import com.bellini.recipecatalog.model.v1.Book;
import com.bellini.recipecatalog.model.v1.Recipe;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:db_seed/recipe/data.sql" })
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = { "classpath:db_seed/recipe/clean-data.sql" })
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepo;

    @Test
    public void findAll_shouldReturnNotNull() {
        Page<Recipe> result = recipeRepo.findAll(PageRequest.of(0, Integer.MAX_VALUE));
        assertThat(result, notNullValue());
    }

    @Test
    public void findAll_shouldReturnNotEmptyAndCorrectPageSize() {
        Page<Recipe> result = recipeRepo.findAll(PageRequest.of(0, 1));
        assertThat(result, not(emptyIterable()));
        assertThat(result.getContent(), hasSize(1));
    }

    @Test
    public void findAll_shouldReturnEmptyForIncorrectPageSize() {
        Page<Recipe> result = recipeRepo.findAll(PageRequest.of(50, 5)); // this page does not exist
        assertThat(result, emptyIterable());
    }

    @Test
    public void findAll_shouldHandlePaginationCorrectly() {
        final List<Recipe> content = recipeRepo.findAll(PageRequest.of(0, Integer.MAX_VALUE)).getContent(); // retrieve all elements
        // fetch one items each time until no results are available
        int startFrom = 0, fetch = 1, total = 0;
        List<Recipe> contentToCheck = new ArrayList<>();
        List<Recipe> tmpContent = null;
        do {
            tmpContent = recipeRepo.findAll(PageRequest.of(startFrom, fetch)).getContent();
            total += tmpContent.size();
            contentToCheck.addAll(tmpContent);
            ++startFrom;
        } while (!tmpContent.isEmpty());

        // check for matching number of elements
        assertThat(total, comparesEqualTo(2));

        // check that the lists are equal
        assertThat(contentToCheck, is(content));
    }

    @Test
    public void findById_shouldReturnCorrectElement() {
        Optional<Recipe> optRecipe = recipeRepo.findById(2L);
        assertThat(optRecipe, notNullValue());
        assertTrue(optRecipe.isPresent());
        assertThat(optRecipe.get().getId(), comparesEqualTo(2L));
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
