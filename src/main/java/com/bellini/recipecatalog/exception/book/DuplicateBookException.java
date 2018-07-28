package com.bellini.recipecatalog.exception.book;

import com.bellini.recipecatalog.model.v1.Book;

public class DuplicateBookException extends RuntimeException {

    private static final long serialVersionUID = -2458702761165927200L;
    private Book book;

    public DuplicateBookException(Book book) {
        super();
        if (book == null) {
            throw new NullPointerException();
        }
        this.book = book;
    }

    @Override
    public String getMessage() {
        return "A record with name \"" + book.getName() + "\" already exists";
    }
}
