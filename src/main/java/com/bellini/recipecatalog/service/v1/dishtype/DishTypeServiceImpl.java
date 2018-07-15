package com.bellini.recipecatalog.service.v1.dishtype;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.exception.dishtype.DuplicateDishTypeException;
import com.bellini.recipecatalog.exception.dishtype.NotExistingDishTypeException;
import com.bellini.recipecatalog.model.common.pagination.PaginationInfo;
import com.bellini.recipecatalog.model.v1.DishType;

@Service
public class DishTypeServiceImpl implements DishTypeService {

    @Autowired
    private DishTypeRepository repo;

    @Override
    public Iterable<DishType> getAll(PaginationInfo pgInfo) {
        return repo.findAll();
    }

    @Override
    public DishType create(DishType dt) {
        if (!repo.findByNameIgnoreCase(dt.getName()).isEmpty()) {
            throw new DuplicateDishTypeException(dt);
        }
        return repo.save(dt);
    }

    @Override
    public Collection<DishType> get(String name, PaginationInfo pgInfo) {
        return repo.findByNameIgnoreCaseContaining(name);
    }

    @Override
    public DishType get(Long id) {
        Optional<DishType> optDt = repo.findById(id);
        if (!optDt.isPresent()) {
            throw new NotExistingDishTypeException(id);
        }
        return optDt.get();
    }

    @Override
    public DishType update(Long id, DishType dt) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid id");
        }
        if (dt == null || StringUtils.isEmpty(dt.getName())) {
            throw new IllegalArgumentException("Invalid dishtype");
        }
        // FIXME refactor
        // search for name conflict
        List<DishType> soughtList = (List<DishType>) repo.findByNameIgnoreCase(dt.getName());
        if (soughtList.isEmpty() || soughtList.get(0).getId().equals(id)) {
            Optional<DishType> toUpdate = repo.findById(id);
            if (!toUpdate.isPresent()) {
                throw new NotExistingDishTypeException(id);
            }
            // update only the name and modification time
            toUpdate.get().setName(dt.getName());
            toUpdate.get().setLastModificationTime(Instant.now());
            return repo.save(toUpdate.get());
        }
        throw new DuplicateDishTypeException(dt);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

}
