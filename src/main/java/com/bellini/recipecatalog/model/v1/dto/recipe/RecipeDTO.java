package com.bellini.recipecatalog.model.v1.dto.recipe;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RecipeDTO {

    private Long id;
    private String title;
    private Instant creationTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
    private Instant lastModificationTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);

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

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public Instant getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(Instant lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

}
