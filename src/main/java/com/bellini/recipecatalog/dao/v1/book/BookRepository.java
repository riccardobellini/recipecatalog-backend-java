package com.bellini.recipecatalog.dao.v1.book;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bellini.recipecatalog.model.v1.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, Long>, BookCustomRepository {

    Collection<Book> findByNameIgnoreCase(String name);
    
    Collection<Book> findByNameIgnoreCaseContaining(String name);
    
}
