package com.example.cftcbrandtech.Reservation.Controller;

import com.example.cftcbrandtech.Reservation.Dto.CreateReservationDto;
import com.example.cftcbrandtech.Reservation.Dto.ReservationResponseDto;
import com.example.cftcbrandtech.Reservation.Dto.UpdateReservationDto;
import com.example.cftcbrandtech.Reservation.Mapper.ReservationModelMapper;
import com.example.cftcbrandtech.Reservation.ReservationModel;
import com.example.cftcbrandtech.Reservation.Service.ReservationService;
import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.Dto.UserProfileDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationModelMapper reservationModelMapper;
    private final JwtHelper jwtHelper;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @Valid @RequestBody CreateReservationDto dto) {

        ReservationModel reservation = reservationService.createReservation(dto);
        ReservationResponseDto response = reservationModelMapper.toResponseDto(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Long id) {
        ReservationModel reservation = reservationService.getReservationById(id);
        ReservationResponseDto response = reservationModelMapper.toResponseDto(reservation);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationModel> reservations = reservationService.getAllReservations();
        List<ReservationResponseDto> response = reservations.stream()
                .map(reservationModelMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByProperty(
            @PathVariable Long propertyId) {

        List<ReservationModel> reservations = reservationService.getReservationsByProperty(propertyId);
        List<ReservationResponseDto> response = reservations.stream()
                .map(reservationModelMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationDto dto) {

        ReservationModel reservation = reservationService.updateReservation(id, dto);
        ReservationResponseDto response = reservationModelMapper.toResponseDto(reservation);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        UserProfileDto currentUser = jwtHelper.getCurrentUser();

        reservationService.cancelReservation(id);
        return ResponseEntity.ok("Reservation cancelled by " + currentUser.getEmail());
    }
}