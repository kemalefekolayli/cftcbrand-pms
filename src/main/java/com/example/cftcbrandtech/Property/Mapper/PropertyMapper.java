package com.example.cftcbrandtech.Property.Mapper;

import com.example.cftcbrandtech.Property.Dto.PropertyCreateDto;
import com.example.cftcbrandtech.Property.PropertyModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PropertyMapper {

    public PropertyModel toEntity(PropertyCreateDto dto) {
        PropertyModel property = new PropertyModel();
        property.setName(dto.getName());
        property.setDescription(dto.getDescription());
        property.setTotalVillas(dto.getTotalVillas());
        property.setCreatedAt(LocalDateTime.now());
        property.setUpdatedAt(LocalDateTime.now());
        return property;
    }
}