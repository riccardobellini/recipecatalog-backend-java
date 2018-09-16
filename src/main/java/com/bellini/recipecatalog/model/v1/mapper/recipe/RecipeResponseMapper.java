package com.bellini.recipecatalog.model.v1.mapper.recipe;

import com.bellini.recipecatalog.model.v1.Recipe;
import com.bellini.recipecatalog.model.v1.dto.recipe.RecipeDTO;
import com.bellini.recipecatalog.model.v1.mapper.Mapper;

public class RecipeResponseMapper implements Mapper<Recipe, RecipeDTO> {
    
    private static RecipeResponseMapper instance = new RecipeResponseMapper();

    private RecipeResponseMapper() {
    }

    public static RecipeResponseMapper getInstance() {
        return instance;
    }

    @Override
    public RecipeDTO toDto(Recipe param) {
        RecipeDTO dto = new RecipeDTO();
        
        if (param.getCreationTime() != null) {
            dto.setCreationTime(param.getCreationTime());
        }
        dto.setId(param.getId());
        if (param.getLastModificationTime() != null) {
            dto.setLastModificationTime(param.getLastModificationTime());
        }
        dto.setTitle(param.getTitle());
        
        return dto;
    }

    @Override
    public Recipe fromDto(RecipeDTO param) {
        // not implemented
        return null;
    }

}
