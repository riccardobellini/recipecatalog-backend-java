package com.bellini.recipecatalog.dao.v1.book;

import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.Book;

public interface BookCustomRepository {

    Iterable<Book> findByTitleIgnoreCaseContaining(String title, Pageable page);
}
