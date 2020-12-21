package com.bellini.recipecatalog.controller.v1.dishtype;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.model.v1.dto.dishtype.DishTypeDTO;
import com.bellini.recipecatalog.model.v1.dto.dishtype.DishTypeModificationDTO;
import com.bellini.recipecatalog.model.v1.dto.generic.CountResultDTO;
import com.bellini.recipecatalog.model.v1.mapper.dishtype.DishTypeModificationMapper;
import com.bellini.recipecatalog.model.v1.mapper.dishtype.DishTypeResponseMapper;
import com.bellini.recipecatalog.service.v1.dishtype.DishTypeService;

@RestController
@RequestMapping("v1/dishTypes")
public class DishTypeController {

    @Autowired
    private DishTypeService dishTypeService;

    @GetMapping(path = "", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Iterable<DishTypeDTO>> getAllDishTypes(@RequestParam(name = "q", required = false) String name, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<DishType> page = null;
        if (name != null) {
            page = dishTypeService.get(name, pageable);
        } else {
            page = dishTypeService.getAll(pageable);
        }
        List<DishTypeDTO> result = page.getContent().stream()
                .map(dt -> DishTypeResponseMapper.getInstance().toDto(dt))
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                new PageImpl<>(result, pageable, page.getTotalElements()), HttpStatus.OK);
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<DishTypeDTO> createDishType(@RequestBody DishTypeModificationDTO dt) {
        DishType insertedDt = dishTypeService.create(DishTypeModificationMapper.getInstance().fromDto(dt));
        return new ResponseEntity<>(DishTypeResponseMapper.getInstance().toDto(insertedDt), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DishTypeDTO> getSingleDishType(@PathVariable(value = "id") Long id) {
        DishType dt = dishTypeService.get(id);

        if (dt == null) {
            throw new NotExistingDishTypeException(id);
        }

        return new ResponseEntity<>(DishTypeResponseMapper.getInstance().toDto(dt), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<DishTypeDTO> update(@PathVariable(value = "id") Long id, @RequestBody DishTypeModificationDTO dt) {
        return new ResponseEntity<>(DishTypeResponseMapper.getInstance().toDto(dishTypeService.update(id, DishTypeModificationMapper.getInstance().fromDto(dt))), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public HttpEntity<String> delete(@PathVariable(value = "id") Long id) {
        dishTypeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/count")
    public HttpEntity<CountResultDTO> getDishTypeCount() {
        int total = dishTypeService.getCount();
        return new ResponseEntity<>(new CountResultDTO(total), HttpStatus.OK);
    }
}
