package com.bellini.recipecatalog.test.repository.book;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

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

import com.bellini.recipecatalog.dao.v1.book.BookRepository;
import com.bellini.recipecatalog.model.v1.Book;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:db_seed/book/data.sql" })
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = { "classpath:db_seed/book/clean-data.sql" })
public class BookRepositoryTest {

    @Autowired
    private BookRepository repo;

    @Test
    public void findAll_shouldReturnNotNull() {
        Page<Book> result = repo.findAll(PageRequest.of(0, Integer.MAX_VALUE)); // retrieve all elements
        assertThat(result, notNullValue());
    }

    @Test
    public void findAll_shouldReturnNotEmptyAndCorrectPageSize() {
        Page<Book> result = repo.findAll(PageRequest.of(0, 2)); // retrieve half of the elements
        final List<Book> content = result.getContent();
        assertThat(result, not(emptyIterable()));
        assertThat(content, hasSize(2));
    }

    @Test
    public void findAll_shouldReturnEmptyForIncorrectPageSize() {
        Page<Book> result = repo.findAll(PageRequest.of(50, 5)); // this page does not exist
        assertThat(result, emptyIterable());
    }

    @Test
    public void findAll_shouldHandlePaginationCorrectly() {
        final List<Book> content = repo.findAll(PageRequest.of(0, Integer.MAX_VALUE)).getContent(); // retrieve all elements
        // fetch two items each time until no results are available
        int startFrom = 0, fetch = 2, total = 0;
        List<Book> contentToCheck = new ArrayList<>();
        List<Book> tmpContent = null;
        do {
            tmpContent = repo.findAll(PageRequest.of(startFrom, fetch)).getContent();
            total += tmpContent.size();
            contentToCheck.addAll(tmpContent);
            ++startFrom;
        } while (!tmpContent.isEmpty());

        // check for matching number of elements
        assertThat(total, comparesEqualTo(4));

        // check that the lists are equal
        assertThat(contentToCheck, is(content));
    }
}
