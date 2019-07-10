package com.bellini.recipecatalog.dao.v1.publication;

import java.util.Optional;

import com.bellini.recipecatalog.model.v1.Publication;

public interface PublicationRepository {

    Publication save(Publication pub);

    Publication save(Long id, Publication pub);

    Optional<Publication> findByVolumeAndYear(Integer volume, Integer year);

    Optional<Publication> findById(Long id);

    Optional<Publication> findByRecipeId(Long id);

    void deleteById(Long id);

    void attachToRecipe(Long pubId, Long recId);
}
