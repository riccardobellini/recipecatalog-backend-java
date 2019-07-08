package com.bellini.recipecatalog.exception.ingredient;

public class NotExistingIngredientException extends RuntimeException {

    private static final long serialVersionUID = 7881500207493108775L;
    private Long id;

    public NotExistingIngredientException(Long id) {
        super();
        if (id == null) {
            throw new IllegalArgumentException("Null object used as initializer of the exception");
        }
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Ingredient " + id + " not found in database";
    }
}
