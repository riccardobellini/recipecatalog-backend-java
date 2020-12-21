package com.bellini.recipecatalog.model.v1.dto.generic;

public class CountResultDTO {

    private int totalElements;

    public CountResultDTO(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

}
