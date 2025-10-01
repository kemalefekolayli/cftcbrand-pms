package com.example.cftcbrandtech.Reservation.Repositories;

import com.example.cftcbrandtech.Reservation.ReservationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReservationModelRepository extends JpaRepository<ReservationModel, Long> {

    @Query("SELECT COUNT(r) FROM ReservationModel r WHERE " +
            "r.propertyId = :propertyId AND " +
            "r.startTime < :endTime AND " +
            "r.endTime > :startTime AND " +
            "r.status != 'CANCELLED'")
    long countOverlappingReservations(@Param("propertyId") Long propertyId,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);
}