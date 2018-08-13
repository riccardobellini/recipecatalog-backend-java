package com.bellini.recipecatalog.model.v1.mapper.dishtype;

import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.model.v1.dto.dishtype.DishTypeModificationDTO;
import com.bellini.recipecatalog.model.v1.mapper.Mapper;

public class DishTypeModificationMapper implements Mapper<DishType, DishTypeModificationDTO> {

    private static DishTypeModificationMapper instance = new DishTypeModificationMapper();

    private DishTypeModificationMapper() {
    }

    public static DishTypeModificationMapper getInstance() {
        return instance;
    }

    @Override
    public DishTypeModificationDTO toDto(DishType param) {
        // not implemented
        return null;
    }

    @Override
    public DishType fromDto(DishTypeModificationDTO param) {
        DishType obj = new DishType();

        obj.setName(param.getName());

        return obj;
    }

}
