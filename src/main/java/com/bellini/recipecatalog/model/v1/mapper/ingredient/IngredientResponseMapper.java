package com.bellini.recipecatalog.model.v1.mapper.ingredient;

import java.time.ZoneOffset;

import com.bellini.recipecatalog.model.v1.Ingredient;
import com.bellini.recipecatalog.model.v1.dto.ingredient.IngredientDTO;
import com.bellini.recipecatalog.model.v1.mapper.Mapper;

public class IngredientResponseMapper implements Mapper<Ingredient, IngredientDTO> {

    private static IngredientResponseMapper instance = new IngredientResponseMapper();

    private IngredientResponseMapper() {
    }

    public static IngredientResponseMapper getInstance() {
        return instance;
    }

    @Override
    public IngredientDTO toDto(Ingredient param) {
        IngredientDTO dto = new IngredientDTO();

        dto.setId(param.getId());
        dto.setName(param.getName());
        if (param.getCreationTime() != null) {
            dto.setCreationTime(param.getCreationTime().atZone(ZoneOffset.UTC));
        }
        if (param.getLastModificationTime() != null) {
            dto.setLastModificationTime(param.getLastModificationTime().atZone(ZoneOffset.UTC));
        }

        return dto;
    }

    @Override
    public Ingredient fromDto(IngredientDTO param) {
        // not implemented
        return null;
    }

}
