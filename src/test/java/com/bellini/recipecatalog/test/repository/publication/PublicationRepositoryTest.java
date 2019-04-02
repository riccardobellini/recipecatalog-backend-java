package com.bellini.recipecatalog.test.repository.publication;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
}
