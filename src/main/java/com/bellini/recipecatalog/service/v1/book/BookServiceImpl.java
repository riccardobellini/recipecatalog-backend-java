package com.bellini.recipecatalog.service.v1.book;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.dao.v1.book.BookRepository;
import com.bellini.recipecatalog.exception.book.DuplicateBookException;
import com.bellini.recipecatalog.exception.book.NotExistingBookException;
import com.bellini.recipecatalog.model.v1.Book;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repo;

    @Override
    public Iterable<Book> getAll(Pageable pageable) {
        return repo.findAll(createSortedPageRequest(pageable));
    }

    private PageRequest createSortedPageRequest(Pageable pageable) {
        Sort.Order order = new Sort.Order(Direction.ASC, "Title").ignoreCase();
        Sort sort = Sort.by(order);
        PageRequest pgReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return pgReq;
    }

    @Override
    public Book create(Book b) {
        if (!repo.findByTitleIgnoreCase(b.getTitle()).isEmpty()) {
            throw new DuplicateBookException(b);
        }
        return repo.save(b);
    }

    @Override
    public Iterable<Book> get(String title, Pageable pageable) {
        return repo.findByTitleIgnoreCaseContaining(title, createSortedPageRequest(pageable));
    }

    @Override
    public Book get(Long id) {
        Optional<Book> optBk = repo.findById(id);
        if (!optBk.isPresent()) {
            throw new NotExistingBookException(id);
        }
        return optBk.get();
    }

    @Override
    public Book update(Long id, Book b) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid id");
        }
        if (b == null || StringUtils.isEmpty(b.getTitle())) {
            throw new IllegalArgumentException("Invalid book");
        }
        // FIXME refactor
        // search for name conflict
        List<Book> soughtList = (List<Book>) repo.findByTitleIgnoreCase(b.getTitle());
        if (soughtList.isEmpty() || soughtList.get(0).getId().equals(id)) {
            Optional<Book> toUpdate = repo.findById(id);
            if (!toUpdate.isPresent()) {
                throw new NotExistingBookException(id);
            }
            // update only the name and modification time
            toUpdate.get().setTitle(b.getTitle());
            toUpdate.get().setLastModificationTime(Instant.now());
            return repo.save(toUpdate.get());
        }
        throw new DuplicateBookException(b);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

}
