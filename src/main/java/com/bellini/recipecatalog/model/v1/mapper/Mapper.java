package com.bellini.recipecatalog.model.v1.mapper;

public interface Mapper<S, T> {

    T toDto(S param);

    S fromDto(T param);

}
