package com.example.cftcbrandtech.Reservation.Dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RFTGDTO {

    @NotEmpty
    private Long propertyId;
    @NotEmpty
    private Long reservationId;
    @NotEmpty
    private LocalDateTime startTime;
    @NotEmpty
    private LocalDateTime endTime;

}
