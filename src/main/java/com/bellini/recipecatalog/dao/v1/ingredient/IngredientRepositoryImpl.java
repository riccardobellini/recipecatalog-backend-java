package com.bellini.recipecatalog.dao.v1.ingredient;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.bellini.recipecatalog.model.v1.Ingredient;

public class IngredientRepositoryImpl implements IngredientCustomRepository {
    
    @Autowired
    EntityManager emf;

    @SuppressWarnings("unchecked")
    @Override
    public Page<Ingredient> findByNameIgnoreCaseContaining(String name, Pageable page) {
        
        Query q = emf.createQuery("SELECT i FROM Ingredient i WHERE i.name LIKE :iName")
            .setParameter("iName", "%" + name + "%")
            .setFirstResult((int) page.getOffset())
            .setMaxResults(page.getPageSize());
        List<Ingredient> ingrList = q.getResultList();
        
        Query qTotal = emf.createQuery("SELECT count(i.id) FROM Ingredient i WHERE i.name LIKE :iName")
            .setParameter("iName", "%" + name + "%");
        long total = (long) qTotal.getSingleResult();
        
        return new PageImpl<>(ingrList, page, total);
    }

}
