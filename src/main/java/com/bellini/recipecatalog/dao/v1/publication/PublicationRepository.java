package com.bellini.recipecatalog.dao.v1.publication;

import java.util.Optional;

import com.bellini.recipecatalog.model.v1.Publication;

public interface PublicationRepository {

    Optional<Publication> findByRecipeId(Long id);

    void attachToRecipe(Long recId, Publication pub);
}
