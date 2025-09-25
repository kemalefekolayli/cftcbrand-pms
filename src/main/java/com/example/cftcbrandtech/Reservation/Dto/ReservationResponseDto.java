package com.example.cftcbrandtech.Reservation.Dto;

import com.example.cftcbrandtech.Reservation.ReservationModel.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String tcKimlikNo;
    private Long propertyId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus status;
    private Double totalPrice;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}