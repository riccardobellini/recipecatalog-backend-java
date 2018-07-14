package com.bellini.recipecatalog.controller.v1.dishtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bellini.recipecatalog.exception.dishtype.NotExistingDishTypeException;
import com.bellini.recipecatalog.model.common.pagination.PaginationInfo;
import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.service.v1.dishtype.DishTypeService;

@RestController
@RequestMapping("v1/dishTypes")
public class DishTypeController {

    @Autowired
    private DishTypeService dishTypeService;

    @GetMapping(path = "", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Iterable<DishType>> getAllDishTypes(@RequestParam(name="q", required = false) String name, PaginationInfo pgInfo) {
        Iterable<DishType> list = null;
        if (name != null) {
            list = dishTypeService.get(name, pgInfo);
        } else {
            list = dishTypeService.getAll(pgInfo);
        }
        return new ResponseEntity<Iterable<DishType>>(
                list, HttpStatus.OK
        );
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<DishType> createDishType(@RequestBody DishType dt) {
        DishType insertedDt = dishTypeService.create(dt);
        return new ResponseEntity<DishType>(insertedDt, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DishType> getSingleDishType(@PathVariable(value = "id") Long id) {
        DishType dt = dishTypeService.get(id);

        if (dt == null) {
            throw new NotExistingDishTypeException(id);
        }

        return new ResponseEntity<DishType>(dt, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<DishType> update(@PathVariable(value = "id") Long id, @RequestBody DishType dt) {
        return new ResponseEntity<DishType>(dishTypeService.update(id, dt), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public HttpEntity<String> delete(@PathVariable(value = "id") Long id) {
        dishTypeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
