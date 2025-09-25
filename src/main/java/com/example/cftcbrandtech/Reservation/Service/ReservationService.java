package com.example.cftcbrandtech.Reservation.Service;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Reservation.Dto.CreateReservationDto;
import com.example.cftcbrandtech.Reservation.Dto.ReservationResponseDto;
import com.example.cftcbrandtech.Reservation.Dto.UpdateReservationDto;
import com.example.cftcbrandtech.Reservation.Mapper.ReservationModelMapper;
import com.example.cftcbrandtech.Reservation.Repositories.ReservationModelRepository;
import com.example.cftcbrandtech.Reservation.ReservationModel;
import com.example.cftcbrandtech.Reservation.ReservationModel.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationModelRepository reservationRepository;
    private final ReservationModelMapper reservationMapper;

    @Transactional
    public ReservationResponseDto createReservation(CreateReservationDto dto) {
        // Validate dates
        validateReservationDates(dto.getStartTime(), dto.getEndTime());

        // Check for overlapping reservations
        List<ReservationModel> overlappingReservations = reservationRepository.findOverlappingReservations(
                dto.getPropertyId(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        if (!overlappingReservations.isEmpty()) {
            log.error("Overlapping reservation found for property {} between {} and {}",
                    dto.getPropertyId(), dto.getStartTime(), dto.getEndTime());
            throw new GlobalException(ErrorCodes.VALIDATION_ERROR);
        }

        // Create and save reservation
        ReservationModel reservation = reservationMapper.toEntity(dto);
        ReservationModel savedReservation = reservationRepository.save(reservation);

        log.info("Reservation created successfully with ID: {}", savedReservation.getId());
        return reservationMapper.toResponseDto(savedReservation);
    }

    @Transactional
    public ReservationResponseDto updateReservation(Long id, UpdateReservationDto dto) {
        ReservationModel reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));

        // If dates are being updated, check for conflicts
        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            validateReservationDates(dto.getStartTime(), dto.getEndTime());

            // Check for overlapping reservations excluding current one
            List<ReservationModel> overlappingReservations = reservationRepository.findOverlappingReservationsExcluding(
                    reservation.getPropertyId(),
                    dto.getStartTime(),
                    dto.getEndTime(),
                    id
            );

            if (!overlappingReservations.isEmpty()) {
                log.error("Cannot update reservation {}: time conflict found", id);
                throw new GlobalException(ErrorCodes.VALIDATION_ERROR);
            }

            reservation.setStartTime(dto.getStartTime());
            reservation.setEndTime(dto.getEndTime());
        }

        // Update other fields if provided
        if (dto.getFirstName() != null) reservation.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) reservation.setLastName(dto.getLastName());
        if (dto.getEmail() != null) reservation.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) reservation.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getStatus() != null) reservation.setStatus(dto.getStatus());
        if (dto.getTotalPrice() != null) reservation.setTotalPrice(dto.getTotalPrice());
        if (dto.getNotes() != null) reservation.setNotes(dto.getNotes());

        ReservationModel updatedReservation = reservationRepository.save(reservation);
        log.info("Reservation {} updated successfully", id);
        return reservationMapper.toResponseDto(updatedReservation);
    }

    public ReservationResponseDto getReservationById(Long id) {
        ReservationModel reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));
        return reservationMapper.toResponseDto(reservation);
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> getReservationsByProperty(Long propertyId) {
        return reservationRepository.findByPropertyId(propertyId).stream()
                .map(reservationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> getReservationsByEmail(String email) {
        return reservationRepository.findByEmail(email).stream()
                .map(reservationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status).stream()
                .map(reservationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> getAvailableSlots(Long propertyId, LocalDateTime startDate, LocalDateTime endDate) {
        List<ReservationModel> existingReservations = reservationRepository.findByPropertyIdAndDateRange(
                propertyId, startDate, endDate
        );

        // This is a simplified version - in production, you'd want to return actual available time slots
        return existingReservations.stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .map(reservationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponseDto cancelReservation(Long id) {
        ReservationModel reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            log.warn("Reservation {} is already cancelled", id);
            throw new GlobalException(ErrorCodes.VALIDATION_ERROR);
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        ReservationModel cancelledReservation = reservationRepository.save(reservation);

        log.info("Reservation {} cancelled successfully", id);
        return reservationMapper.toResponseDto(cancelledReservation);
    }

    @Transactional
    public ReservationResponseDto confirmReservation(Long id) {
        ReservationModel reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            log.warn("Cannot confirm reservation {} with status {}", id, reservation.getStatus());
            throw new GlobalException(ErrorCodes.VALIDATION_ERROR);
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        ReservationModel confirmedReservation = reservationRepository.save(reservation);

        log.info("Reservation {} confirmed successfully", id);
        return reservationMapper.toResponseDto(confirmedReservation);
    }

    @Transactional
    public void deleteReservation(Long id) {
        ReservationModel reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));

        reservationRepository.delete(reservation);
        log.info("Reservation {} deleted successfully", id);
    }

    private void validateReservationDates(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new GlobalException(ErrorCodes.VALIDATION_ERROR);
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new GlobalException(ErrorCodes.VALIDATION_ERROR);
        }
    }
}