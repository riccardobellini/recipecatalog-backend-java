package com.bellini.recipecatalog.test.service.publication;

import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.bellini.recipecatalog.dao.v1.publication.PublicationRepository;
import com.bellini.recipecatalog.exception.publication.DuplicatePublicationException;
import com.bellini.recipecatalog.model.v1.Publication;
import com.bellini.recipecatalog.service.v1.publication.PublicationService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PublicationServiceTest {

    @Autowired
    private PublicationService pubSrv;

    @MockBean
    private PublicationRepository pubRepo;

    @Test(expected = DuplicatePublicationException.class)
    public void create_shouldThrowExceptionWhenDuplicate() {
        when(pubRepo.findByVolumeAndYear(anyInt(), anyInt())).thenReturn(Optional.of(dummyPublication()));
        pubSrv.create(dummyPublication());
    }

    private Publication dummyPublication() {
        final Publication pub = new Publication();
        pub.setCreationTime(Instant.now());
        pub.setLastModificationTime(Instant.now());
        pub.setId(1L);
        pub.setVolume(1);
        pub.setYear(2019);
        return pub;
    }
}
