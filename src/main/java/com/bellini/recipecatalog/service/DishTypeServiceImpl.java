package com.bellini.recipecatalog.service;

import com.bellini.recipecatalog.exception.dishtype.NotExistingDishTypeException;
import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.repository.DishTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishTypeServiceImpl implements DishTypeService {

    @Autowired
    private DishTypeRepository dishTypeRepository;

    public Iterable<DishType> getAll() {
        return dishTypeRepository.findAll();
    }

    @Transactional
    public void create(DishType dt) {
        dishTypeRepository.save(dt);
    }

    public List<DishType> get(String name) {
        return dishTypeRepository.findByNameIgnoreCase(name);
    }

    @Override
    public DishType get(Long id) {
        return dishTypeRepository.findOne(id);
    }

    @Override
    public DishType update(Long id, DishType dt) {
        if (dt.getId() != null && !id.equals(dt.getId())) {
            throw new IllegalArgumentException(
                    "Mismatch between path parameter id=" + id + " and object id=" + dt.getId()
            );
        }

        // should not allow updating a non existing record
        DishType sought = dishTypeRepository.findOne(id);
        if (sought == null) {
            throw new NotExistingDishTypeException(id);
        }

        if (dt.getId() == null) {
            dt.setId(id);
        }

        dishTypeRepository.save(dt);
        return null;
    }
}
