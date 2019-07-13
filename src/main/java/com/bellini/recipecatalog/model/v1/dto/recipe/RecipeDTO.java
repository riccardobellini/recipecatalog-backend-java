package com.bellini.recipecatalog.model.v1.dto.recipe;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;

import com.bellini.recipecatalog.model.v1.dto.book.BookDTO;
import com.bellini.recipecatalog.model.v1.dto.dishtype.DishTypeDTO;
import com.bellini.recipecatalog.model.v1.dto.ingredient.IngredientDTO;
import com.bellini.recipecatalog.model.v1.dto.publication.PublicationDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

public class RecipeDTO {

    private Long id;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime creationTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime lastModificationTime;
    private BookDTO book;
    private PublicationDTO publication;
    private Collection<IngredientDTO> ingredients = new ArrayList<>();
    private Collection<DishTypeDTO> dishtypes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public ZonedDateTime getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(ZonedDateTime lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public PublicationDTO getPublication() {
        return this.publication;
    }

    public void setPublication(PublicationDTO publication) {
        this.publication = publication;
    }

    public Collection<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public Collection<DishTypeDTO> getDishtypes() {
        return dishtypes;
    }

    public void setDishtypes(Collection<DishTypeDTO> dishtypes) {
        this.dishtypes = dishtypes;
    }

}
