package com.bellini.recipecatalog.exception.ingredient;

import com.bellini.recipecatalog.model.v1.Ingredient;

public class DuplicateIngredientException extends RuntimeException {

    private static final long serialVersionUID = -2458702761165927200L;
    private Ingredient ingr;

    public DuplicateIngredientException(Ingredient ingr) {
        super();
        if (ingr == null) {
            throw new NullPointerException();
        }
        this.ingr = ingr;
    }

    @Override
    public String getMessage() {
        return "A record with name \"" + ingr.getName() + "\" already exists";
    }
}
