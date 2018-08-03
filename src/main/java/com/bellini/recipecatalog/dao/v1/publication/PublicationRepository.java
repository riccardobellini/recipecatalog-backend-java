package com.bellini.recipecatalog.dao.v1.publication;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bellini.recipecatalog.model.v1.Publication;

@Repository
public interface PublicationRepository extends CrudRepository<Publication, Long> {

    Optional<Publication> findByVolumeAndYear(Integer volume, Integer year);

}
