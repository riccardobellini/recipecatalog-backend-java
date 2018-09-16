package com.bellini.recipecatalog.model.v1;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "Recipe")
@Table(name = "RECIPE")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CREATION_TIME")
    private Instant creationTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    @Column(name = "LAST_MODIFICATION_TIME")
    private Instant lastModificationTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    @Column(name = "IMAGE_KEY")
    private String imageKey;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "INGREDIENT_RECIPE",
            joinColumns = { @JoinColumn(name = "ID_RECIPE") },
            inverseJoinColumns = { @JoinColumn(name = "ID_INGREDIENT")})
    private List<Ingredient> ingredients = new ArrayList<>();

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

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    @ManyToOne
    @JoinTable(name = "BOOK_RECIPE",
            joinColumns = { @JoinColumn(name = "ID_RECIPE", insertable = false, updatable = false)},
            inverseJoinColumns = { @JoinColumn(name = "ID_BOOK", insertable = false, updatable = false) })
    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
