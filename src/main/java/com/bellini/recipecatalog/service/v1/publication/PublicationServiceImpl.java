package com.bellini.recipecatalog.service.v1.publication;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.dao.v1.publication.PublicationRepository;
import com.bellini.recipecatalog.exception.publication.DuplicatePublicationException;
import com.bellini.recipecatalog.exception.publication.NotExistingPublicationException;
import com.bellini.recipecatalog.model.v1.Publication;

@Service
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    private PublicationRepository repo;

    @Override
    public Publication create(Publication pub) {
        if (!repo.findByVolumeAndYear(pub.getVolume(), pub.getYear()).isPresent()) {
            throw new DuplicatePublicationException(pub);
        }
        return repo.save(pub);
    }

    @Override
    public Publication get(Long id) {
        Optional<Publication> optPub = repo.findById(id);
        if (!optPub.isPresent()) {
            throw new NotExistingPublicationException(id);
        }
        return optPub.get();
    }

    @Override
    public Publication update(Long id, Publication pub) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid id");
        }
        if (pub == null || (pub.getVolume() == null && pub.getYear() == null)) {
            throw new IllegalArgumentException("Invalid publication");
        }
        // FIXME refactor
        // search for volume/year conflict
        Optional<Publication> sought = repo.findByVolumeAndYear(pub.getVolume(), pub.getYear());
        if (!sought.isPresent() || sought.get().getId().equals(id)) {
            Optional<Publication> toUpdate = repo.findById(id);
            if (!toUpdate.isPresent()) {
                throw new NotExistingPublicationException(id);
            }
            // update only the volume/year and modification time
            toUpdate.get().setVolume(pub.getVolume());
            toUpdate.get().setYear(pub.getYear());
            toUpdate.get().setLastModificationTime(Instant.now());
            return repo.save(toUpdate.get());
        }
        throw new DuplicatePublicationException(pub);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Publication> get(Integer volume, Integer year) {
        return repo.findByVolumeAndYear(volume, year);
    }

}
