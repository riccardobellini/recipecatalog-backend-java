package com.bellini.recipecatalog.exception.book;

public class NotExistingBookException extends RuntimeException {

    private static final long serialVersionUID = 7881500207493108775L;
    private Long id;

    public NotExistingBookException(Long id) {
        super();
        if (id == null) {
            throw new IllegalArgumentException("Null object used as initializer of the exception");
        }
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Book " + id + " not found in database";
    }
}
