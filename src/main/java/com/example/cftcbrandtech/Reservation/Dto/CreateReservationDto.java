package com.example.cftcbrandtech.Reservation.Dto;

import com.example.cftcbrandtech.DateValidation.ValidDateRange;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ValidDateRange(startField = "startTime", endField = "endTime", message = "End time must be after start time")
public class CreateReservationDto {

    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number should be 10-11 digits")
    private String phoneNumber;


    @Pattern(regexp = "^[0-9]{11}$", message = "TC Kimlik No should be exactly 11 digits")
    private String tcKimlikNo;

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private Double totalPrice;

    private String notes;
}