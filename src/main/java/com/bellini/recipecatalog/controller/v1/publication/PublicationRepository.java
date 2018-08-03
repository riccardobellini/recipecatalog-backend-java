package com.bellini.recipecatalog.controller.v1.publication;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bellini.recipecatalog.model.v1.Publication;

public interface PublicationRepository extends CrudRepository<Publication, Long> {

    Optional<Publication> findByVolumeAndYear(Integer volume, Integer year);

}
