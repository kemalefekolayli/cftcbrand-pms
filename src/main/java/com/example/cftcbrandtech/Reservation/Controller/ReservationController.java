package com.example.cftcbrandtech.Reservation.Controller;

import com.example.cftcbrandtech.Reservation.Dto.CreateReservationDto;
import com.example.cftcbrandtech.Reservation.Dto.ReservationResponseDto;
import com.example.cftcbrandtech.Reservation.Dto.UpdateReservationDto;
import com.example.cftcbrandtech.Reservation.ReservationModel.ReservationStatus;
import com.example.cftcbrandtech.Reservation.Service.ReservationService;
import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.UserModel;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;
    private final JwtHelper jwtHelper;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateReservationDto dto) {

        // Token validation
        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        log.info("User {} creating reservation", currentUser.getEmail());

        ReservationResponseDto reservation = reservationService.createReservation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> updateReservation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationDto dto) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        log.info("User {} updating reservation {}", currentUser.getEmail(), id);

        ReservationResponseDto reservation = reservationService.updateReservation(id, dto);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        log.info("User {} fetching reservation {}", currentUser.getEmail(), id);

        ReservationResponseDto reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations(
            @RequestHeader("Authorization") String token) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        log.info("User {} fetching all reservations", currentUser.getEmail());

        List<ReservationResponseDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByProperty(
            @RequestHeader("Authorization") String token,
            @PathVariable Long propertyId) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        log.info("User {} fetching reservations for property {}", currentUser.getEmail(), propertyId);

        List<ReservationResponseDto> reservations = reservationService.getReservationsByProperty(propertyId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReservationResponseDto>> getUserReservations(
            @RequestHeader("Authorization") String token,
            @RequestParam String email) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));

        // Users can only fetch their own reservations unless they have admin privileges
        // TODO: Add role-based access control
        if (!currentUser.getEmail().equals(email)) {
            log.warn("User {} attempted to access reservations for {}", currentUser.getEmail(), email);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<ReservationResponseDto> reservations = reservationService.getReservationsByEmail(email);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable ReservationStatus status) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        log.info("User {} fetching reservations with status {}", currentUser.getEmail(), status);

        List<ReservationResponseDto> reservations = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/availability")
    public ResponseEntity<List<ReservationResponseDto>> checkAvailability(
            @RequestHeader("Authorization") String token,
            @RequestParam Long propertyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        log.info("User {} checking availability for property {} between {} and {}",
                currentUser.getEmail(), propertyId, startDate, endDate);

        List<ReservationResponseDto> availableSlots = reservationService.getAvailableSlots(propertyId, startDate, endDate);
        return ResponseEntity.ok(availableSlots);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponseDto> cancelReservation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        log.info("User {} cancelling reservation {}", currentUser.getEmail(), id);

        ReservationResponseDto reservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ReservationResponseDto> confirmReservation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        log.info("User {} confirming reservation {}", currentUser.getEmail(), id);

        // TODO: Add admin role check - only admins should be able to confirm reservations

        ReservationResponseDto reservation = reservationService.confirmReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteReservation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        log.info("User {} deleting reservation {}", currentUser.getEmail(), id);

        // TODO: Add permission check - users should only delete their own reservations

        reservationService.deleteReservation(id);
        return ResponseEntity.ok(Map.of("message", "Reservation deleted successfully"));
    }
}