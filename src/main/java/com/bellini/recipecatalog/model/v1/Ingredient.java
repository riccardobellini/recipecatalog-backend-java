package com.bellini.recipecatalog.model.v1;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

//@Entity(name = "Ingredient")
//@Table(name = "INGREDIENT")
public class Ingredient {

//    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    @Column(name = "ID")
    private Long id;

//    @Column(name = "NAME")
    private String name;

//    @Column(name = "CREATION_TIME")
    private Instant creationTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);

//    @Column(name = "LAST_MODIFICATION_TIME")
    private Instant lastModificationTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);

//    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "ingredients")
//    private List<Recipe> recipes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime.truncatedTo(ChronoUnit.SECONDS);
    }

    public Instant getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(Instant lastModificationTime) {
        this.lastModificationTime = lastModificationTime.truncatedTo(ChronoUnit.SECONDS);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ingredient other = (Ingredient) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Ingredient [id=" + id + ", name=" + name + "]";
    }

//    public List<Recipe> getRecipes() {
//        return recipes;
//    }

//    public void setRecipes(List<Recipe> recipes) {
//        this.recipes = recipes;
//    }

}
