package com.bellini.recipecatalog.model.v1;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
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

    public final Long getId() {
        return id;
    }

    public final void setId(Long id) {
        this.id = id;
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public final Instant getCreationTime() {
        return creationTime;
    }

    public final void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public final Instant getLastModificationTime() {
        return lastModificationTime;
    }

    public final void setLastModificationTime(Instant lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public final String getImageKey() {
        return imageKey;
    }

    public final void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    @OneToOne
    @JoinTable(name = "BOOK_RECIPE", joinColumns = {
            @JoinColumn(name = "ID_BOOK", referencedColumnName = "ID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "ID_RECIPE", referencedColumnName = "ID", unique = true)
    })
    private Book book;

    public final Book getBook() {
        return book;
    }

    public final void setBook(Book book) {
        this.book = book;
    }

}
