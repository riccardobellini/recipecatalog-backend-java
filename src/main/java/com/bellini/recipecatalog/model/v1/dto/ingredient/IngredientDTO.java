package com.bellini.recipecatalog.model.v1.dto.ingredient;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class IngredientDTO {

    private Long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime creationTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime lastModificationTime;

    public final Long getId() {
        return id;
    }

    public final void setId(Long id) {
        this.id = id;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public final void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public final ZonedDateTime getLastModificationTime() {
        return lastModificationTime;
    }

    public final void setLastModificationTime(ZonedDateTime lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

}
