package com.example.cftcbrandtech.Reservation.Repositories;

import com.example.cftcbrandtech.Reservation.ReservationModel;
import com.example.cftcbrandtech.Reservation.ReservationModel.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationModelRepository extends JpaRepository<ReservationModel, Long> {

    // Check for overlapping reservations for the same property
    @Query("SELECT r FROM ReservationModel r WHERE r.propertyId = :propertyId " +
            "AND r.status NOT IN ('CANCELLED') " +
            "AND ((r.startTime < :endTime AND r.endTime > :startTime) " +
            "OR (r.startTime >= :startTime AND r.startTime < :endTime) " +
            "OR (r.endTime > :startTime AND r.endTime <= :endTime))")
    List<ReservationModel> findOverlappingReservations(
            @Param("propertyId") Long propertyId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // Check for overlapping reservations excluding a specific reservation (for updates)
    @Query("SELECT r FROM ReservationModel r WHERE r.propertyId = :propertyId " +
            "AND r.id != :excludeId " +
            "AND r.status NOT IN ('CANCELLED') " +
            "AND ((r.startTime < :endTime AND r.endTime > :startTime) " +
            "OR (r.startTime >= :startTime AND r.startTime < :endTime) " +
            "OR (r.endTime > :startTime AND r.endTime <= :endTime))")
    List<ReservationModel> findOverlappingReservationsExcluding(
            @Param("propertyId") Long propertyId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId
    );

    List<ReservationModel> findByPropertyId(Long propertyId);

    List<ReservationModel> findByEmail(String email);

    List<ReservationModel> findByStatus(ReservationStatus status);

    List<ReservationModel> findByPropertyIdAndStatus(Long propertyId, ReservationStatus status);

    @Query("SELECT r FROM ReservationModel r WHERE r.propertyId = :propertyId " +
            "AND r.startTime >= :startDate AND r.endTime <= :endDate")
    List<ReservationModel> findByPropertyIdAndDateRange(
            @Param("propertyId") Long propertyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    Optional<ReservationModel> findByIdAndEmail(Long id, String email);
}