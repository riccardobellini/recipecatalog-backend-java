package com.bellini.recipecatalog.service;

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
}
