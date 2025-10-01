package com.example.cftcbrandtech.Property.Mapper;

import com.example.cftcbrandtech.Property.Dto.PropertyCreateDto;
import com.example.cftcbrandtech.Property.LocationModel.LocationInfo;
import org.springframework.stereotype.Component;

@Component
public class LocationInfoMapper {

    public LocationInfo toEntity(PropertyCreateDto dto) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLocationName(dto.getLocationName());
        locationInfo.setLocationAddress(dto.getLocationAddress());
        locationInfo.setLocationCity(dto.getLocationCity());
        locationInfo.setLocationState(dto.getLocationState());
        return locationInfo;
    }
}