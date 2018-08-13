package com.bellini.recipecatalog.model.v1.mapper.ingredient;

import com.bellini.recipecatalog.model.v1.Ingredient;
import com.bellini.recipecatalog.model.v1.dto.ingredient.IngredientModificationDTO;
import com.bellini.recipecatalog.model.v1.mapper.Mapper;

public class IngredientModificationMapper implements Mapper<Ingredient, IngredientModificationDTO> {

    private static IngredientModificationMapper instance = new IngredientModificationMapper();

    private IngredientModificationMapper() {
    }

    public static IngredientModificationMapper getInstance() {
        return instance;
    }

    @Override
    public IngredientModificationDTO toDto(Ingredient param) {
        // not implemented
        return null;
    }

    @Override
    public Ingredient fromDto(IngredientModificationDTO param) {
        Ingredient obj = new Ingredient();

        obj.setName(param.getName());

        return obj;
    }

}
