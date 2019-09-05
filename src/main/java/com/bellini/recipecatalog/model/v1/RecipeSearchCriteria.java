package com.bellini.recipecatalog.model.v1;

import org.apache.commons.lang3.StringUtils;

public class RecipeSearchCriteria {

    private final String ingredient;
    private final String dishtype;
    private final String book;
    private final String titleQuery;

    public RecipeSearchCriteria(String ingredient, String dishtype, String book, String titleQuery) {
        this.ingredient = ingredient;
        this.dishtype = dishtype;
        this.book = book;
        this.titleQuery = titleQuery;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getDishtype() {
        return dishtype;
    }

    public String getBook() {
        return book;
    }

    public String getTitleQuery() {
        return titleQuery;
    }

    public boolean isByIngredient() {
        return StringUtils.isNotBlank(ingredient);
    }

    public boolean isByDishtype() {
        return StringUtils.isNotBlank(dishtype);
    }

    public boolean isByBook() {
        return StringUtils.isNotBlank(book);
    }

    public boolean isByTitle() {
        return StringUtils.isNotBlank(titleQuery);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String ingredient;
        private String dishtype;
        private String book;
        private String titleQuery;

        public Builder withIngredient(String ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public Builder withDishtype(String dishtype) {
            this.dishtype = dishtype;
            return this;
        }

        public Builder withBook(String book) {
            this.book = book;
            return this;
        }

        public Builder withTitleQuery(String titleQuery) {
            this.titleQuery = titleQuery;
            return this;
        }

        public RecipeSearchCriteria build() {
            return new RecipeSearchCriteria(this.ingredient, this.dishtype, this.book, this.titleQuery);
        }
    }
}
