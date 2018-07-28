package com.bellini.recipecatalog.service.v1.ingredient;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.dao.v1.ingredient.IngredientRepository;
import com.bellini.recipecatalog.exception.ingredient.DuplicateIngredientException;
import com.bellini.recipecatalog.exception.ingredient.NotExistingIngredientException;
import com.bellini.recipecatalog.model.v1.Ingredient;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository repo;

    @Override
    public Iterable<Ingredient> getAll(Pageable pageable) {
        return repo.findAll(createSortedPageRequest(pageable));
    }

    private PageRequest createSortedPageRequest(Pageable pageable) {
        Sort.Order order = new Sort.Order(Direction.ASC, "Name").ignoreCase();
        Sort sort = Sort.by(order);
        PageRequest pgReq = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return pgReq;
    }

    @Override
    public Ingredient create(Ingredient ingr) {
        if (!repo.findByNameIgnoreCase(ingr.getName()).isEmpty()) {
            throw new DuplicateIngredientException(ingr);
        }
        return repo.save(ingr);
    }

    @Override
    public Iterable<Ingredient> get(String name, Pageable pageable) {
        return repo.findByNameIgnoreCaseContaining(name, createSortedPageRequest(pageable));
    }

    @Override
    public Ingredient get(Long id) {
        Optional<Ingredient> optDt = repo.findById(id);
        if (!optDt.isPresent()) {
            throw new NotExistingIngredientException(id);
        }
        return optDt.get();
    }

    @Override
    public Ingredient update(Long id, Ingredient ingr) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid id");
        }
        if (ingr == null || StringUtils.isEmpty(ingr.getName())) {
            throw new IllegalArgumentException("Invalid ingredient");
        }
        // FIXME refactor
        // search for name conflict
        List<Ingredient> soughtList = (List<Ingredient>) repo.findByNameIgnoreCase(ingr.getName());
        if (soughtList.isEmpty() || soughtList.get(0).getId().equals(id)) {
            Optional<Ingredient> toUpdate = repo.findById(id);
            if (!toUpdate.isPresent()) {
                throw new NotExistingIngredientException(id);
            }
            // update only the name and modification time
            toUpdate.get().setName(ingr.getName());
            toUpdate.get().setLastModificationTime(Instant.now());
            return repo.save(toUpdate.get());
        }
        throw new DuplicateIngredientException(ingr);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

}
