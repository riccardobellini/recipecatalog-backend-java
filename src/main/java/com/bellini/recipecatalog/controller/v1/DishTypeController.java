package com.bellini.recipecatalog.controller.v1;

import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.repository.DishTypeRepository;
import com.bellini.recipecatalog.service.DishTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/dishTypes")
public class DishTypeController {

    @Autowired
    private DishTypeService dishTypeService;

    @GetMapping(path = "")
    public @ResponseBody
    ResponseEntity<Iterable<DishType>> getAllDishTypes() {
        return new ResponseEntity<Iterable<DishType>>(
                dishTypeService.getAll(), HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<DishType> createDishType(@RequestBody DishType dt) {
        dishTypeService.create(dt);
        return new ResponseEntity<DishType>(dt, HttpStatus.OK);
    }

}
