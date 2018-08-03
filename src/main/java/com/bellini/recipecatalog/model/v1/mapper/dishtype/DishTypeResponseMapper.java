package com.bellini.recipecatalog.model.v1.mapper.dishtype;

import java.time.ZoneOffset;

import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.model.v1.dto.dishtype.DishTypeDTO;
import com.bellini.recipecatalog.model.v1.mapper.Mapper;

public class DishTypeResponseMapper implements Mapper<DishType, DishTypeDTO> {

    private static DishTypeResponseMapper instance = new DishTypeResponseMapper();

    private DishTypeResponseMapper() {
    }

    public static DishTypeResponseMapper getInstance() {
        return instance;
    }

    @Override
    public DishTypeDTO toDto(DishType param) {
        DishTypeDTO dto = new DishTypeDTO();

        dto.setId(param.getId());
        dto.setName(param.getName());
        if (param.getCreationTime() != null) {
            dto.setCreationTime(param.getCreationTime().atZone(ZoneOffset.UTC));
        }
        if (param.getLastModificationTime() != null) {
            dto.setLastModificationTime(param.getLastModificationTime().atZone(ZoneOffset.UTC));
        }

        return dto;
    }

    @Override
    public DishType fromDto(DishTypeDTO param) {
        // not implemented
        return null;
    }

}
