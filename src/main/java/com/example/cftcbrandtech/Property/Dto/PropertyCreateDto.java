package com.example.cftcbrandtech.Property.Dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyCreateDto {


    @NotEmpty(message = "Property name is required")
    private String name;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotEmpty(message = "villatype is required")
    private String villaType;

    @NotNull(message = "Total villas count is required")
    @Min(value = 1, message = "Must have at least 1 villa")
    private Integer totalVillas;

    // Location Info fields
    @NotEmpty(message = "Location name is required")
    private String locationName;

    private String locationAddress;

    private String locationCity;

    private String locationState;

    // Payment Info fields
    @NotNull(message = "Nightly rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Nightly rate must be positive")
    private BigDecimal nightlyRate;

    private BigDecimal cleaningFee;

    private BigDecimal securityDeposit;

    private BigDecimal serviceFeeRate;

    private BigDecimal weekendRate;

    private BigDecimal weeklyDiscount;

    private BigDecimal monthlyDiscount;

    @Min(value = 1, message = "Minimum nights must be at least 1")
    private Integer minNights;

    @Max(value = 365, message = "Maximum nights cannot exceed 365")
    private Integer maxNights;
}