package com.example.cftcbrandtech.Reservation.Dto;

import com.example.cftcbrandtech.Reservation.ReservationModel.ReservationStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateReservationDto {

    private String firstName;

    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number should be 10-11 digits")
    private String phoneNumber;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ReservationStatus status;

    private Double totalPrice;

    private String notes;
}