package com.bellini.recipecatalog.controller.v1.dishtype;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bellini.recipecatalog.exception.dishtype.DuplicateDishTypeException;
import com.bellini.recipecatalog.exception.dishtype.NotExistingDishTypeException;
import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.service.v1.dishtype.DishTypeService;

@RestController
@RequestMapping("v1/dishTypes")
public class DishTypeController {

    @Autowired
    private DishTypeService dishTypeService;

    @GetMapping(path = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<Iterable<DishType>> getAllDishTypes(@RequestParam(name="q") String name) {
    	Iterable<DishType> list = null;
    	if (name != null) {
    		list = dishTypeService.get(name);
    	} else {
    		list = dishTypeService.getAll();
    	}
        return new ResponseEntity<Iterable<DishType>>(
                list, HttpStatus.OK
        );
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<DishType> createDishType(@RequestBody DishType dt) {
        Collection<DishType> result = dishTypeService.get(dt.getName());
        
        if (!result.isEmpty()) {
            throw new DuplicateDishTypeException(dt);
        }

        dishTypeService.create(dt);
        return new ResponseEntity<DishType>(dt, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DishType> getSingleDishType(@PathVariable(value = "id") Long id) {
        DishType dt = dishTypeService.get(id);

        if (dt == null) {
            throw new NotExistingDishTypeException(id);
        }

        return new ResponseEntity<DishType>(dt, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<DishType> update(@PathVariable(value = "id") Long id, @RequestBody DishType dt) {
        return new ResponseEntity<DishType>(dishTypeService.update(id, dt), HttpStatus.OK);
    }

}
