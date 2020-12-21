package com.bellini.recipecatalog.test.repository.dishtype;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
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

import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.model.v1.DishType;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:db_seed/dishtype/data.sql" })
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = { "classpath:db_seed/dishtype/clean-data.sql" })
public class DishTypeRepositoryTest {

    @Autowired
    private DishTypeRepository repo;

    @Test
    public void findAll_shouldReturnNotNull() {
        Page<DishType> result = repo.findAll(PageRequest.of(0, Integer.MAX_VALUE)); // retrieve all elements
        assertThat(result, notNullValue());
    }

    @Test
    public void findAll_shouldReturnNotEmptyAndCorrectPageSize() {
        Page<DishType> result = repo.findAll(PageRequest.of(0, 5)); // retrieve half of the elements
        final List<DishType> content = result.getContent();
        assertThat(result, not(emptyIterable()));
        assertThat(content, hasSize(5));
    }

    @Test
    public void findAll_shouldReturnEmptyForIncorrectPageSize() {
        Page<DishType> result = repo.findAll(PageRequest.of(50, 5)); // this page does not exist
        assertThat(result, emptyIterable());
    }

    @Test
    public void findAll_shouldHandlePaginationCorrectly() {
        final List<DishType> content = repo.findAll(PageRequest.of(0, Integer.MAX_VALUE)).getContent(); // retrieve all elements
        // fetch two items each time until no results are available
        int startFrom = 0, fetch = 2, total = 0;
        List<DishType> contentToCheck = new ArrayList<>();
        List<DishType> tmpContent = null;
        do {
            tmpContent = repo.findAll(PageRequest.of(startFrom, fetch)).getContent();
            total += tmpContent.size();
            contentToCheck.addAll(tmpContent);
            ++startFrom;
        } while (!tmpContent.isEmpty());

        // check for matching number of elements
        assertThat(total, comparesEqualTo(10));

        // check that the lists are equal
        assertThat(contentToCheck, is(content));
    }

    @Test
    public void findByNameIgnoreCase_shouldReturnTheSameResult() {
        Collection<DishType> resultUpperCase = repo.findByNameIgnoreCase("Salad");
        Collection<DishType> resultLowerCase = repo.findByNameIgnoreCase("salad");

        // collections should be equal
        assertThat(resultUpperCase, is(resultLowerCase));
    }

    @Test
    public void findByNameIgnoreCase_shouldReturnNotNullAndEmptyWhenNoMatch() {
        Collection<DishType> result = repo.findByNameIgnoreCase("UnknownDishType");

        assertThat(result, notNullValue());
        assertThat(result, empty());
    }

    @Test
    public void findByNameIgnoreCaseContaining_shouldReturnCorrectResults() {
        Page<DishType> result = repo.findByNameIgnoreCaseContaining("ast", PageRequest.of(0, 10));

        assertThat(result, notNullValue());
        assertThat(result, allOf(
                hasItem(Matchers.<DishType>hasProperty("name", is("Roast"))),
                hasItem(Matchers.<DishType>hasProperty("name", is("Pasta")))));
    }

    @Test
    public void findByNameIgnoreCaseContaining_shouldHandlePaginationCorrectly() {
        final List<DishType> content = repo.findByNameIgnoreCaseContaining("ast", PageRequest.of(0, Integer.MAX_VALUE)).getContent(); // retrieve all elements
        // fetch two items each time until no results are available
        int startFrom = 0, fetch = 1, total = 0;
        List<DishType> contentToCheck = new ArrayList<>();
        List<DishType> tmpContent = null;
        do {
            tmpContent = repo.findByNameIgnoreCaseContaining("ast", PageRequest.of(startFrom, fetch)).getContent();
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
    public void save_shouldStoreElement() {
        DishType dt = testDishType();
        DishType stored = repo.save(dt);

        assertThat(stored, hasProperty("id", allOf(notNullValue(), greaterThan((long) 0))));
    }

    private DishType testDishType() {
        DishType dt = new DishType();
        dt.setName("Cake");
        return dt;
    }

    @Test
    public void findById_shouldReturnCorrectElement() {
        Optional<DishType> dtOpt = repo.findById((long) 6);
        assertThat(dtOpt, notNullValue());
        assertTrue(dtOpt.isPresent());
        assertThat(dtOpt.get().getId(), comparesEqualTo((long) 6));
    }

    @Test
    public void findById_shouldReturnEmptyOptionalWhenNotFound() {
        Optional<DishType> dtOpt = repo.findById((long) 11); // not existent id
        assertThat(dtOpt, notNullValue());
        assertFalse(dtOpt.isPresent());
    }

    @Test
    public void deleteById_shouldRemoveCorrectElement() {
        // ensure to remove an element which exists
        Optional<DishType> optElement = repo.findById((long) 5);
        assertTrue(optElement.isPresent());
        final DishType elem = optElement.get();
        repo.deleteById(elem.getId());
        // try to retrieve the same element
        optElement = repo.findById(elem.getId());
        // should not exist anymore
        assertFalse(optElement.isPresent());
    }

    @Test
    public void save_shouldUpdateElement() {
        final DishType newDt = testUpdateDishType();
        DishType stored = repo.save((long) 4, newDt);
        assertThat(stored, notNullValue());
        assertThat(stored.getName(), is("Bread & Doughs"));
        System.out.println("Modification: " + stored.getLastModificationTime() + "\tCreation: " + stored.getCreationTime());
        assertTrue(stored.getLastModificationTime().isAfter(stored.getCreationTime()));
    }

    private DishType testUpdateDishType() {
        DishType dt = new DishType();
        dt.setName("Bread & Doughs");
        return dt;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findByRecipeId_shouldReturnCorrectElements() {
        Collection<DishType> result = repo.findByRecipeId((long) 1);
        assertThat(result, notNullValue());
        assertFalse(result.isEmpty());
        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder(
                hasProperty("id", is((long) 2)),
                hasProperty("id", is((long) 9))));
    }

    @Test
    public void findByRecipeId_shouldReturnNotNullAndEmptyWhenNoMatch() {
        Collection<DishType> result = repo.findByRecipeId((long) 10000);
        assertThat(result, notNullValue());
        assertThat(result, empty());
    }

    @Test
    public void getCount_shouldReturnCount() {
        int count = repo.getCount();
        assertThat(count, equalTo(10));
    }

}
