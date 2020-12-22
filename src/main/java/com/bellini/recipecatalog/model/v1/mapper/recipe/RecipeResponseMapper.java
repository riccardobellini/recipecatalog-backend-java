package com.bellini.recipecatalog.model.v1.mapper.recipe;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collection;

import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.model.v1.Ingredient;
import com.bellini.recipecatalog.model.v1.Recipe;
import com.bellini.recipecatalog.model.v1.dto.book.BookDTO;
import com.bellini.recipecatalog.model.v1.dto.publication.PublicationDTO;
import com.bellini.recipecatalog.model.v1.dto.recipe.RecipeDTO;
import com.bellini.recipecatalog.model.v1.mapper.Mapper;
import com.bellini.recipecatalog.model.v1.mapper.dishtype.DishTypeResponseMapper;
import com.bellini.recipecatalog.model.v1.mapper.ingredient.IngredientResponseMapper;

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
            dto.setCreationTime(param.getCreationTime().atZone(ZoneOffset.UTC));
        }
        dto.setId(param.getId());
        if (param.getLastModificationTime() != null) {
            dto.setLastModificationTime(param.getLastModificationTime().atZone(ZoneOffset.UTC));
        }
        dto.setTitle(param.getTitle());

        if (param.getBook() != null) {
            BookDTO bDto = new BookDTO();
            bDto.setId(param.getBook().getId());
            bDto.setTitle(param.getBook().getTitle());
            Instant bookCreationTime = param.getBook().getCreationTime();
            if (bookCreationTime != null) {
                bDto.setCreationTime(bookCreationTime.atZone(ZoneOffset.UTC));
            }
            Instant bookLastModificationTime = param.getBook().getLastModificationTime();
            if (bookLastModificationTime != null) {
                bDto.setLastModificationTime(bookLastModificationTime.atZone(ZoneOffset.UTC));
            }
            dto.setBook(bDto);
        }

        Collection<Ingredient> ingredients = param.getIngredients();
        if (ingredients != null) {
            for (Ingredient ingr : ingredients) {
                dto.getIngredients().add(IngredientResponseMapper.getInstance().toDto(ingr));
            }
        }

        Collection<DishType> dishtypes = param.getDishtypes();
        if (dishtypes != null) {
            for (DishType dt : dishtypes) {
                dto.getDishtypes().add(DishTypeResponseMapper.getInstance().toDto(dt));
            }
        }

        if (param.getPublication() != null) {
            PublicationDTO pDto = new PublicationDTO();
            pDto.setVolume(param.getPublication().getVolume());
            pDto.setYear(param.getPublication().getYear());
            pDto.setMonth(param.getPublication().getMonth());
            dto.setPublication(pDto);
        }

        return dto;
    }

    @Override
    public Recipe fromDto(RecipeDTO param) {
        // not implemented
        return null;
    }

}
