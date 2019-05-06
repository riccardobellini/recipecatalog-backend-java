package com.bellini.recipecatalog.test.service.book;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.bellini.recipecatalog.dao.v1.book.BookRepository;
import com.bellini.recipecatalog.exception.book.DuplicateBookException;
import com.bellini.recipecatalog.model.v1.Book;
import com.bellini.recipecatalog.service.v1.book.BookService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookServiceTest {

    private static final String DUMMY_BOOK_TITLE = "Da Noi";

    @Autowired
    private BookService bookSrv;

    @MockBean
    private BookRepository bookRepo;

    @Test(expected = DuplicateBookException.class)
    public void create_shouldThrowExceptionWhenDuplicate() {
        when(bookRepo.findByTitleIgnoreCase(ArgumentMatchers.eq(DUMMY_BOOK_TITLE))).thenReturn(Collections.singleton(new Book()));
        bookSrv.create(dummyBook());
        // verify method calls
        verify(bookRepo).findByTitleIgnoreCase(ArgumentMatchers.eq(DUMMY_BOOK_TITLE));
        verify(bookRepo, never()).save(ArgumentMatchers.any(Book.class));
    }

    @Test
    public void create_shouldInvokeStorageWhenNotDuplicate() {
        when(bookRepo.findByTitleIgnoreCase(ArgumentMatchers.eq(DUMMY_BOOK_TITLE))).thenReturn(Collections.emptyList());
        final Book mock = dummyBook();
        when(bookRepo.save(mock)).thenReturn(mock);
        Book stored = bookSrv.create(mock);
        // verify method calls
        verify(bookRepo).findByTitleIgnoreCase(ArgumentMatchers.eq(DUMMY_BOOK_TITLE));
        verify(bookRepo).save(mock);
        assertEquals(mock, stored);
    }

    private Book dummyBook() {
        Book mock = new Book();
        mock.setTitle(DUMMY_BOOK_TITLE);
        return mock;
    }
}
