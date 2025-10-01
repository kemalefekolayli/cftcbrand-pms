package com.example.cftcbrandtech.Reservation.Repositories;

import com.example.cftcbrandtech.Reservation.ReservationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationModelRepository extends JpaRepository<ReservationModel, Long> {

    List<ReservationModel> findByPropertyId(Long propertyId);

    @Query("SELECT COUNT(r) FROM ReservationModel r WHERE " +
            "r.propertyId = :propertyId AND " +
            "r.startTime < :endTime AND " +
            "r.endTime > :startTime AND " +
            "r.status != 'CANCELLED'")
    long countOverlappingReservations(@Param("propertyId") Long propertyId,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    /**
     * Find all reservations that overlap with the given date range
     * This includes:
     * - Reservations that start during the range
     * - Reservations that end during the range
     * - Reservations that span the entire range
     */
    @Query("SELECT r FROM ReservationModel r WHERE " +
            "r.propertyId = :propertyId AND " +
            "r.startTime < :endTime AND " +
            "r.endTime > :startTime " +
            "ORDER BY r.startTime ASC")
    List<ReservationModel> findByPropertyIdAndDateRange(
            @Param("propertyId") Long propertyId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}