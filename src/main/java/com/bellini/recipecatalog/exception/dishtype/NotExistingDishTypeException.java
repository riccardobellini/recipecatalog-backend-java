package com.bellini.recipecatalog.exception.dishtype;

public class NotExistingDishTypeException extends RuntimeException {

    private Long id;

    public NotExistingDishTypeException(Long id) {
        super();
        if (id == null) {
            throw new IllegalArgumentException("Null object used as initializer of the exception");
        }
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Dish type " + id + " not found in database";
    }
}
