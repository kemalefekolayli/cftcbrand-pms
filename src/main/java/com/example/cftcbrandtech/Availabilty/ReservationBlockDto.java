package com.example.cftcbrandtech.Availabilty;


import com.example.cftcbrandtech.Reservation.ReservationModel.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationBlockDto {

    private Long reservationId;
    private String guestName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private ReservationStatus status;
    private Double totalPrice;
}