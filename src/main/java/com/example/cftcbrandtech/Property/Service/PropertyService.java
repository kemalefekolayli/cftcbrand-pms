package com.example.cftcbrandtech.Property.Service;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Property.Dto.PropertyCreateDto;
import com.example.cftcbrandtech.Property.Mapper.LocationInfoMapper;
import com.example.cftcbrandtech.Property.Mapper.PropertyMapper;
import com.example.cftcbrandtech.Property.Mapper.PropertyPaymentInfoMapper;
import com.example.cftcbrandtech.Property.PropertyModel;
import com.example.cftcbrandtech.Property.PropertyModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyModelRepository propertyModelRepository;
    private final PropertyMapper propertyMapper;
    private final LocationInfoMapper locationInfoMapper;
    private final PropertyPaymentInfoMapper propertyPaymentInfoMapper;

    @Transactional
    public PropertyModel createProperty(PropertyCreateDto dto) {
        try {
            PropertyModel property = propertyMapper.toEntity(dto);
            property.setLocation(locationInfoMapper.toEntity(dto));
            property.setPropertyPaymentInfo(propertyPaymentInfoMapper.toEntity(dto));

            return propertyModelRepository.save(property);
        } catch (Exception e) {
            throw new GlobalException(ErrorCodes.PROPERTY_COULD_NOT_BE_CREATED);
        }
    }

    @Transactional
    public void deleteProperty(Long id) {
        PropertyModel property = propertyModelRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCodes.PROPERTY_COULD_NOT_BE_DELETED));

        propertyModelRepository.delete(property);
    }
}