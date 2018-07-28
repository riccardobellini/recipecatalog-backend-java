package com.bellini.recipecatalog.dao.v1.book;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.Book;

public class BookRepositoryImpl implements BookCustomRepository {
    
    @Autowired
    EntityManager emf;

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Book> findByTitleIgnoreCaseContaining(String title, Pageable page) {
        
        Query q = emf.createQuery("SELECT b FROM Book b WHERE b.title LIKE :bTitle")
            .setParameter("bTitle", "%" + title + "%")
            .setFirstResult((int) page.getOffset())
            .setMaxResults(page.getPageSize());
        List<Book> bookList = q.getResultList();
        
        Query qTotal = emf.createQuery("SELECT count(b.id) FROM Book b WHERE b.title LIKE :bTitle")
            .setParameter("bTitle", "%" + title + "%");
        long total = (long) qTotal.getSingleResult();
        
        return new PageImpl<>(bookList, page, total);
    }

}
