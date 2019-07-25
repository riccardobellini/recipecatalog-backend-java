package com.bellini.recipecatalog.test.repository.recipe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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

    @Test
    public void save_shouldStoreElement() {
        Recipe rec = dummyRecipe();
        Recipe stored = recipeRepo.save(rec);

        assertThat(stored, notNullValue());
        assertThat(stored, hasProperty("id", greaterThan(0L)));
    }

    private Recipe dummyRecipe() {
        Recipe rec = new Recipe();
        rec.setTitle("Dummy");
        return rec;
    }

    @Test
    public void save_shouldUpdateElement() {
        final Recipe newRecipe = testUpdateRecipe();
        Recipe updated = recipeRepo.save(1L, newRecipe);

        assertThat(updated, notNullValue());
        assertThat(updated.getTitle(), is("Pasta cacio e pepe"));
        assertTrue(updated.getLastModificationTime().isAfter(updated.getCreationTime()));
    }

    private Recipe testUpdateRecipe() {
        Recipe rec = new Recipe();
        rec.setTitle("Pasta cacio e pepe");
        return rec;
    }

    @Test
    public void findByTitleIgnoreCaseContaining_shouldReturnTheSameResult() {
        Page<Recipe> uppercaseResult = recipeRepo.findByTitleIgnoreCaseContaining("SPAGH", PageRequest.of(0, 10));
        Page<Recipe> lowercaseResult = recipeRepo.findByTitleIgnoreCaseContaining("spagh", PageRequest.of(0, 10));

        // collections should be equal
        assertThat(uppercaseResult.getContent(), is(lowercaseResult.getContent()));
    }

    @Test
    public void findByTitleIgnoreCaseContaining_shouldReturnNotNullAndEmptyWhenNoMatch() {
        Page<Recipe> result = recipeRepo.findByTitleIgnoreCaseContaining("Missing recipe", PageRequest.of(0, 10));

        assertThat(result, notNullValue());
        List<Recipe> resultList = result.getContent();
        assertThat(resultList, notNullValue());
        assertThat(resultList, empty());
    }

    @Test
    public void findByTitleIgnoreCaseContaining_shouldHandlePaginationCorrectly() {
        Page<Recipe> allResultPage = recipeRepo.findByTitleIgnoreCaseContaining("spagh", PageRequest.of(0, Integer.MAX_VALUE)); // retrieve all elements
        Page<Recipe> firstPage = recipeRepo.findByTitleIgnoreCaseContaining("spagh", PageRequest.of(0, 1));
        Page<Recipe> secondPage = recipeRepo.findByTitleIgnoreCaseContaining("spagh", PageRequest.of(1, 1));

        assertThat(firstPage.getContent().size() + secondPage.getContent().size(), comparesEqualTo(2));
        List<Recipe> allList = allResultPage.getContent();
        List<Recipe> pagedResults = new ArrayList<>(firstPage.getContent());
        pagedResults.addAll(secondPage.getContent());
        assertThat(allList, is(pagedResults));
    }
}
