package com.bellini.recipecatalog.model.v1.dto.book;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BookDTO {

    private Long id;
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime creationTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime lastModificationTime;

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

    public ZonedDateTime getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(ZonedDateTime lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

}
