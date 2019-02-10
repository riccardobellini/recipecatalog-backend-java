package com.bellini.recipecatalog.model.v1.dto.publication;

public class PublicationDTO {

    private Long id;
    private Integer volume;
    private Integer year;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVolume() {
        return this.volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}