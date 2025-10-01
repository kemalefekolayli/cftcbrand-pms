package com.example.cftcbrandtech.Reservation.Repositories;

import com.example.cftcbrandtech.Reservation.ReservationForGivenTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface RFGTRepository extends JpaRepository<ReservationForGivenTime,Long> {

    @Query("SELECT COUNT(r) FROM ReservationForGivenTime r WHERE " +
            "r.propertyId = :propertyId AND " +
            "r.startTime < :endTime AND " +
            "r.endTime > :startTime")
    long countConflicts(@Param("propertyId") Long propertyId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);
}
