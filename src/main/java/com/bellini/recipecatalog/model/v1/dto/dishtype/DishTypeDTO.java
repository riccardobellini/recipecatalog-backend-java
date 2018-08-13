package com.bellini.recipecatalog.model.v1.dto.dishtype;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DishTypeDTO {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public ZonedDateTime getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(ZonedDateTime lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime creationTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime lastModificationTime;

}
