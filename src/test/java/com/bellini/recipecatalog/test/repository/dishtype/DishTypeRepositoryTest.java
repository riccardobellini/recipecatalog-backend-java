package com.bellini.recipecatalog.test.repository.dishtype;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.model.v1.DishType;

@RunWith(SpringRunner.class)
@JdbcTest
@Transactional
@ComponentScan(basePackages = { "com.bellini.recipecatalog.dao" })
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
        final List<DishType> content = repo.findAll(PageRequest.of(0, Integer.MAX_VALUE)).getContent(); // retrieve all
                                                                                                        // elements
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

}
