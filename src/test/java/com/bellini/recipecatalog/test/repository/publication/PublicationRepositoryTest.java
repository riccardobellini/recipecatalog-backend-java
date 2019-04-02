package com.bellini.recipecatalog.test.repository.publication;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bellini.recipecatalog.dao.v1.publication.PublicationRepository;
import com.bellini.recipecatalog.model.v1.Publication;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:db_seed/publication/data.sql" })
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = { "classpath:db_seed/publication/clean-data.sql" })
public class PublicationRepositoryTest {

    @Autowired
    private PublicationRepository repo;

    @Test
    public void save_shouldStoreElement() {
        Publication pub = testPublication();
        Publication stored = repo.save(pub);

        assertThat(stored, hasProperty("id", allOf(notNullValue(), greaterThan((long) 0))));
    }

    private Publication testPublication() {
        Publication pub = new Publication();
        pub.setVolume(3);
        pub.setYear(2014);
        return pub;
    }

    @Test
    public void findByVolumeAndYear_shouldReturnCorrectResult() {
        Optional<Publication> optPub = repo.findByVolumeAndYear(9, 2015);

        assertThat(optPub, notNullValue());
        assertTrue(optPub.isPresent());
        final Publication pub = optPub.get();
        assertThat(pub, hasProperty("id", comparesEqualTo((long) 8)));
        assertThat(pub, hasProperty("volume", comparesEqualTo((int) 9)));
        assertThat(pub, hasProperty("year", comparesEqualTo((int) 2015)));
    }

    @Test
    public void findByVolumeAndYear_shouldReturnEmptyOptionalWhenNotFound() {
        Optional<Publication> optPub = repo.findByVolumeAndYear(300, 4000); // not existent <volume, year>

        assertThat(optPub, notNullValue());
        assertFalse(optPub.isPresent());
    }

    @Test
    public void findById_shouldReturnCorrectElement() {
        Optional<Publication> optPub = repo.findById((long) 8);

        assertThat(optPub, notNullValue());
        assertTrue(optPub.isPresent());
        assertThat(optPub.get(), allOf(
                hasProperty("volume", comparesEqualTo(9)),
                hasProperty("year", comparesEqualTo(2015))));
    }

    @Test
    public void findById_shouldReturnEmptyOptionalWhenNotFound() {
        Optional<Publication> optPub = repo.findById((long) 11);// not existent id

        assertThat(optPub, notNullValue());
        assertFalse(optPub.isPresent());
    }
}
