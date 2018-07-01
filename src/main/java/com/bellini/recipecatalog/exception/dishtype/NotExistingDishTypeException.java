package com.bellini.recipecatalog.exception.dishtype;

public class NotExistingDishTypeException extends RuntimeException {

	private static final long serialVersionUID = 7881500207493108775L;
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
