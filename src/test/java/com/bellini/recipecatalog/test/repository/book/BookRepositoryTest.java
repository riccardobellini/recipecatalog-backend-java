package com.bellini.recipecatalog.test.repository.book;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.comparesEqualTo;
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

    @Test
    public void findByTitleIgnoreCase_shouldReturnTheSameResult() {
        Collection<Book> resultUpperCase = repo.findByTitleIgnoreCase("Da Noi");
        Collection<Book> resultLowerCase = repo.findByTitleIgnoreCase("da noi");

        // collections should be equal
        assertThat(resultUpperCase, is(resultLowerCase));
    }

    @Test
    public void findByNamTitleIgnoreCase_shouldReturnNotNullAndEmptyWhenNoMatch() {
        Collection<Book> result = repo.findByTitleIgnoreCase("UnknownBook");

        assertThat(result, notNullValue());
        assertThat(result, empty());
    }

    @Test
    public void findByNameIgnoreCaseContaining_shouldReturnCorrectResults() {
        Page<Book> result = repo.findByTitleIgnoreCaseContaining("cuc", PageRequest.of(0, 10));

        assertThat(result, notNullValue());
        assertThat(result, allOf(
                hasItem(Matchers.<Book>hasProperty("title", is("La Cucina Italiana"))),
                hasItem(Matchers.<Book>hasProperty("title", is("Ci Piace Cucinare")))));
    }

    @Test
    public void findByNameIgnoreCaseContaining_shouldHandlePaginationCorrectly() {
        final List<Book> content = repo.findByTitleIgnoreCaseContaining("cuc", PageRequest.of(0, Integer.MAX_VALUE)).getContent(); // retrieve all elements
        // fetch two items each time until no results are available
        int startFrom = 0, fetch = 1, total = 0;
        List<Book> contentToCheck = new ArrayList<>();
        List<Book> tmpContent = null;
        do {
            tmpContent = repo.findByTitleIgnoreCaseContaining("cuc", PageRequest.of(startFrom, fetch)).getContent();
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
        Book dt = testBook();
        Book stored = repo.save(dt);

        assertThat(stored, hasProperty("id", allOf(notNullValue(), greaterThan((long) 0))));
    }

    private Book testBook() {
        Book dt = new Book();
        dt.setTitle("Il meglio di Sale & Pepe");
        return dt;
    }

    @Test
    public void findById_shouldReturnCorrectElement() {
        Optional<Book> bookOpt = repo.findById((long) 2);
        assertThat(bookOpt, notNullValue());
        assertTrue(bookOpt.isPresent());
        assertThat(bookOpt.get().getId(), comparesEqualTo((long) 2));
    }

    @Test
    public void findById_shouldReturnEmptyOptionalWhenNotFound() {
        Optional<Book> bookOpt = repo.findById((long) 11); // not existent id
        assertThat(bookOpt, notNullValue());
        assertFalse(bookOpt.isPresent());
    }

    @Test
    public void deleteById_shouldRemoveCorrectElement() {
        // ensure to remove an element which exists
        Optional<Book> optElement = repo.findById((long) 3);
        assertTrue(optElement.isPresent());
        final Book elem = optElement.get();
        repo.deleteById(elem.getId());
        // try to retrieve the same element
        optElement = repo.findById(elem.getId());
        // should not exist anymore
        assertFalse(optElement.isPresent());
    }

    @Test
    public void save_shouldUpdateElement() {
        final Book newBook = testUpdateBook();
        Book stored = repo.save((long) 4, newBook);
        assertThat(stored, notNullValue());
        assertThat(stored.getTitle(), is("Mi Piace Cucinare"));
        assertTrue(stored.getLastModificationTime().isAfter(stored.getCreationTime()));
    }

    private Book testUpdateBook() {
        Book book = new Book();
        book.setTitle("Mi Piace Cucinare");
        return book;
    }

    @Test
    public void findByRecipeId_shouldReturnCorrectElement() {
        Optional<Book> optBook = repo.findByRecipeId((long) 1);
        assertThat(optBook, notNullValue());
        assertTrue(optBook.isPresent());
        assertThat(optBook.get(), hasProperty("id", is((long) 2)));
    }

    @Test
    public void findByRecipeId_shouldReturnEmptyOptionalWhenNotFound() {
        Optional<Book> optBook = repo.findByRecipeId((long) 1000);
        assertThat(optBook, notNullValue());
        assertFalse(optBook.isPresent());
    }
}
