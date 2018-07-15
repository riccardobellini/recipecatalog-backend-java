package com.bellini.recipecatalog.dao.v1.dishtype;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.DishType;

public class DishTypeRepositoryImpl implements DishTypeCustomRepository {
    
    @Autowired
    EntityManager emf;

    @SuppressWarnings("unchecked")
    @Override
    public Collection<DishType> findByNameIgnoreCaseContaining(String name, Pageable page) {
        
        Query q = emf.createQuery("SELECT d FROM DishType d WHERE d.name LIKE :dtName")
        .setParameter("dtName", "%" + name + "%");
        return q.getResultList();
    }

}
