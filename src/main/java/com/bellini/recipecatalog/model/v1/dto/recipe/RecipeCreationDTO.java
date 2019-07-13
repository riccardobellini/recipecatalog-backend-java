package com.bellini.recipecatalog.model.v1.dto.recipe;

import java.util.ArrayList;
import java.util.Collection;

public class RecipeCreationDTO {

    private String title;
    private Collection<Long> ingredients = new ArrayList<>();
    private Collection<Long> dishtypes = new ArrayList<>();
    private Long book;
    private Integer volume;
    private Integer year;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<Long> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<Long> ingredients) {
        this.ingredients = ingredients;
    }

    public Collection<Long> getDishtypes() {
        return dishtypes;
    }

    public void setDishtypes(Collection<Long> dishtypes) {
        this.dishtypes = dishtypes;
    }

    public Long getBook() {
        return book;
    }

    public void setBook(Long book) {
        this.book = book;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}
