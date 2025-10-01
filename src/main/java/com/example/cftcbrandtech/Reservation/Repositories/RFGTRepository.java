package com.example.cftcbrandtech.Reservation.Repositories;

import com.example.cftcbrandtech.Reservation.ReservationForGivenTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RFGTRepository extends JpaRepository<ReservationForGivenTime,Long> {

}
