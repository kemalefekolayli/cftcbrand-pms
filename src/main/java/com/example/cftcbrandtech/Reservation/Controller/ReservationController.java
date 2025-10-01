package com.example.cftcbrandtech.Reservation.Controller;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Reservation.Dto.CreateReservationDto;
import com.example.cftcbrandtech.Reservation.Dto.ReservationResponseDto;
import com.example.cftcbrandtech.Reservation.Dto.UpdateReservationDto;
import com.example.cftcbrandtech.Reservation.Mapper.ReservationModelMapper;
import com.example.cftcbrandtech.Reservation.ReservationModel;
import com.example.cftcbrandtech.Reservation.Service.ReservationService;
import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.UserModel;
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

    // CREATE
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateReservationDto dto) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        ReservationModel reservation = reservationService.createReservation(dto);
        ReservationResponseDto response = reservationModelMapper.toResponseDto(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // READ - Get single reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        ReservationModel reservation = reservationService.getReservationById(id);
        ReservationResponseDto response = reservationModelMapper.toResponseDto(reservation);

        return ResponseEntity.ok(response);
    }

    // READ - Get all reservations
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations(
            @RequestHeader("Authorization") String token) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        List<ReservationModel> reservations = reservationService.getAllReservations();
        List<ReservationResponseDto> response = reservations.stream()
                .map(reservationModelMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // READ - Get reservations by property
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByProperty(
            @RequestHeader("Authorization") String token,
            @PathVariable Long propertyId) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        List<ReservationModel> reservations = reservationService.getReservationsByProperty(propertyId);
        List<ReservationResponseDto> response = reservations.stream()
                .map(reservationModelMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> updateReservation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationDto dto) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        ReservationModel reservation = reservationService.updateReservation(id, dto);
        ReservationResponseDto response = reservationModelMapper.toResponseDto(reservation);

        return ResponseEntity.ok(response);
    }

    // DELETE (Soft delete - Cancel reservation)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelReservation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        reservationService.cancelReservation(id);
        return ResponseEntity.ok("Reservation cancelled successfully");
    }
}