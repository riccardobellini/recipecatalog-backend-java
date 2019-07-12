package com.bellini.recipecatalog.exception.recipe;

public class NotExistingRecipeException extends RuntimeException {

    private static final long serialVersionUID = 2975419159984559986L;
    private Long id;

    public NotExistingRecipeException(Long id) {
        super();
        if (id == null) {
            throw new IllegalArgumentException("Null object used as initializer of the exception");
        }
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Recipe " + id + " not found in database";
    }

}
