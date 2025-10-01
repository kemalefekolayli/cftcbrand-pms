package com.example.cftcbrandtech.Property.Mapper;

import com.example.cftcbrandtech.Property.Dto.PropertyCreateDto;
import com.example.cftcbrandtech.Property.PropertyPaymentInfo.PropertyPaymentInfo;
import org.springframework.stereotype.Component;

@Component
public class PropertyPaymentInfoMapper {

    public PropertyPaymentInfo toEntity(PropertyCreateDto dto) {
        PropertyPaymentInfo paymentInfo = new PropertyPaymentInfo();
        paymentInfo.setNightlyRate(dto.getNightlyRate());
        paymentInfo.setCleaningFee(dto.getCleaningFee());
        paymentInfo.setSecurityDeposit(dto.getSecurityDeposit());
        paymentInfo.setServiceFeeRate(dto.getServiceFeeRate());
        paymentInfo.setWeekendRate(dto.getWeekendRate());
        paymentInfo.setWeeklyDiscount(dto.getWeeklyDiscount());
        paymentInfo.setMonthlyDiscount(dto.getMonthlyDiscount());
        paymentInfo.setMinNights(dto.getMinNights() != null ? dto.getMinNights() : 1);
        paymentInfo.setMaxNights(dto.getMaxNights() != null ? dto.getMaxNights() : 365);
        return paymentInfo;
    }
}