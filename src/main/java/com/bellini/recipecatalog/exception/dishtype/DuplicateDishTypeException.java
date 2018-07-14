package com.bellini.recipecatalog.exception.dishtype;

import com.bellini.recipecatalog.model.v1.DishType;

public class DuplicateDishTypeException extends RuntimeException {

    private static final long serialVersionUID = -2458702761165927200L;
    private DishType dt;

    public DuplicateDishTypeException(DishType dt) {
        super();
        if (dt == null) {
            throw new NullPointerException();
        }
        this.dt = dt;
    }

    @Override
    public String getMessage() {
        return "A record with name \"" + dt.getName() + "\" already exists";
    }
}
