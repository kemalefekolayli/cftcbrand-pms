package com.example.cftcbrandtech.Reservation.Service;

import com.example.cftcbrandtech.Reservation.Repositories.RFGTRepository;
import com.example.cftcbrandtech.Reservation.ReservationForGivenTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RFGTService {

    private final RFGTRepository rfgtRepository;
    public ReservationForGivenTime createRFGT( Long villaId, Long reservationId, LocalDateTime endTime , LocalDateTime startTime ) {
        ReservationForGivenTime reservationForGivenTime = new ReservationForGivenTime();
        reservationForGivenTime.setVillaId(villaId);
        reservationForGivenTime.setReservationId(reservationId);
        reservationForGivenTime.setStartTime(startTime);
        reservationForGivenTime.setEndTime(endTime);
        rfgtRepository.save(reservationForGivenTime);
        return reservationForGivenTime;
    }
}
