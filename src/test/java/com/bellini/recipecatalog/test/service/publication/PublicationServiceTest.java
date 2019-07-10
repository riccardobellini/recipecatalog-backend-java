package com.bellini.recipecatalog.test.service.publication;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
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
import com.bellini.recipecatalog.exception.publication.NotExistingPublicationException;
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

    @Test
    public void create_shouldCallRepositoryWhenNotDuplicate() {
        when(pubRepo.findByVolumeAndYear(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(pubRepo.save(any())).thenReturn(dummyPublication());
        Publication thePublication = pubSrv.create(dummyPublication());

        assertThat(thePublication, notNullValue());
        verify(pubRepo).findByVolumeAndYear(anyInt(), anyInt());
        verify(pubRepo).save(any());
    }

    @Test(expected = NotExistingPublicationException.class)
    public void get_shouldThrowWhenNotFound() {
        when(pubRepo.findById(anyLong())).thenReturn(Optional.empty());
        pubSrv.get(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_shouldThrowWhenNullId() {
        pubSrv.update(null, dummyPublication());
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_shouldThrowWhenNullPublication() {
        pubSrv.update(1L, null);
    }

    @Test(expected = DuplicatePublicationException.class)
    public void update_shouldThrowExceptionWhenDuplicate() {
        when(pubRepo.findByVolumeAndYear(anyInt(), anyInt())).thenReturn(Optional.of(dummyPublication()));
        pubSrv.update(2L, dummyPublication());
    }

    @Test(expected = NotExistingPublicationException.class)
    public void update_shouldThrowExceptionWhenNotFound() {
        when(pubRepo.findByVolumeAndYear(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(pubRepo.findById(2L)).thenReturn(Optional.empty());
        pubSrv.update(2L, dummyPublication());
    }

    @Test
    public void update_shouldCallRepository() {
        when(pubRepo.findByVolumeAndYear(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(pubRepo.findById(2L)).thenReturn(Optional.of(dummyPublication()));
        when(pubRepo.save(anyLong(), any())).thenReturn(dummyPublication());
        Publication updated = pubSrv.update(2L, dummyPublication());
        assertThat(updated, notNullValue());

        verify(pubRepo).findByVolumeAndYear(anyInt(), anyInt());
        verify(pubRepo).findById(anyLong());
        verify(pubRepo).save(anyLong(), any());
    }
}
