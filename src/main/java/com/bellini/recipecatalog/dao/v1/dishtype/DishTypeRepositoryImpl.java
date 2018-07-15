package com.bellini.recipecatalog.dao.v1.dishtype;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.DishType;

public class DishTypeRepositoryImpl implements DishTypeCustomRepository {
    
    @Autowired
    EntityManager emf;

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<DishType> findByNameIgnoreCaseContaining(String name, Pageable page) {
        
        Query q = emf.createQuery("SELECT d FROM DishType d WHERE d.name LIKE :dtName")
            .setParameter("dtName", "%" + name + "%")
            .setFirstResult((int) page.getOffset())
            .setMaxResults(page.getPageSize());
        List<DishType> dtList = q.getResultList();
        
        Query qTotal = emf.createQuery("SELECT count(d.id) FROM DishType d WHERE d.name LIKE :dtName")
            .setParameter("dtName", "%" + name + "%");
        long total = (long) qTotal.getSingleResult();
        
        return new PageImpl<>(dtList, page, total);
    }

}
