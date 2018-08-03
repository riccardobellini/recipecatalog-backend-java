package com.bellini.recipecatalog.service.v1.publication;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.model.v1.Publication;

@Service
public interface PublicationService {

    Publication create(Publication pub);

    Optional<Publication> get(Integer volume, Integer year);

    Publication get(Long id);

    Publication update(Long id, Publication pub);
    
    void delete(Long id);
}
