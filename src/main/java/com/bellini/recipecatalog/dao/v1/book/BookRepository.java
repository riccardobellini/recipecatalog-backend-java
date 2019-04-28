package com.bellini.recipecatalog.dao.v1.book;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.Book;

public interface BookRepository {

    Collection<Book> findByTitleIgnoreCase(String title);

    Page<Book> findByTitleIgnoreCaseContaining(String title, Pageable page);

    Page<Book> findAll(Pageable page);

    Book save(Book book);

    Book save(Long id, Book book);

    Optional<Book> findById(Long id);

    void deleteById(Long id);

    Optional<Book> findByRecipeId(Long recipeId);

    void attachToRecipe(Long bookId, Long recId);

}
