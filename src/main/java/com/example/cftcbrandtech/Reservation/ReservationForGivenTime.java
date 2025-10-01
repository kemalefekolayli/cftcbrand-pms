package com.example.cftcbrandtech.Reservation;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ReservationForGivenTime {

    @Id
    private Long id;
    @NotEmpty
    private Long villaId;
    @NotEmpty
    private LocalDateTime startTime;
    @NotEmpty
    private LocalDateTime endTime;
    @NotEmpty
    private Long reservationId;


}
