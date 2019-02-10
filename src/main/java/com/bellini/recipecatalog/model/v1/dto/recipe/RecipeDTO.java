package com.bellini.recipecatalog.model.v1.dto.recipe;

import java.time.ZonedDateTime;

import com.bellini.recipecatalog.model.v1.dto.book.BookDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

public class RecipeDTO {

    private Long id;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime creationTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime lastModificationTime;
    private BookDTO book;

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

}
