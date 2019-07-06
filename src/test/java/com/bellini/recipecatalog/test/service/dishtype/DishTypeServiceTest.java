package com.bellini.recipecatalog.test.service.dishtype;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.exception.dishtype.DuplicateDishTypeException;
import com.bellini.recipecatalog.exception.dishtype.NotExistingDishTypeException;
import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.service.v1.dishtype.DishTypeService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DishTypeServiceTest {

    private static final String DUMMY_DISHTYPE_NAME = "Antipasti";

    @Autowired
    private DishTypeService dtSrv;

    @MockBean
    private DishTypeRepository dtRepo;

    @Test(expected = DuplicateDishTypeException.class)
    public void create_shouldThrowExceptionWhenDuplicate() {
        when(dtRepo.findByNameIgnoreCase(ArgumentMatchers.eq(DUMMY_DISHTYPE_NAME))).thenReturn(Collections.singleton(new DishType()));
        dtSrv.create(dummyDishType());
        // verify method calls
        verify(dtRepo).findByNameIgnoreCase(ArgumentMatchers.eq(DUMMY_DISHTYPE_NAME));
    }

    @Test(expected = NotExistingDishTypeException.class)
    public void get_shouldThrowWhenNotFound() {
        when(dtRepo.findById(anyLong())).thenReturn(Optional.empty());
        dtSrv.get(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_shouldThrowWhenNullId() {
        dtSrv.update(null, dummyDishType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_shouldThrowWhenNullUpdatedObject() {
        dtSrv.update(1L, null);
    }

    private DishType dummyDishType() {
        DishType dt = new DishType();
        dt.setId(1L);
        dt.setName(DUMMY_DISHTYPE_NAME);
        dt.setCreationTime(Instant.now());
        dt.setLastModificationTime(Instant.now());
        return dt;
    }
}
