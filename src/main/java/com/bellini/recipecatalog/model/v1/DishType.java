package com.bellini.recipecatalog.model.v1;

import javax.persistence.*;

@Entity(name = "CATEGORIA")
public class DishType {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NOME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
