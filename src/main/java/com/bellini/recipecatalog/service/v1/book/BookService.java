package com.bellini.recipecatalog.service.v1.book;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.model.v1.Book;

@Service
public interface BookService {

    Iterable<Book> getAll(Pageable pageable);

    Book create(Book dt);

    Iterable<Book> get(String name, Pageable pageable);

    Book get(Long id);

    Book update(Long id, Book dt);
    
    void delete(Long id);
}
