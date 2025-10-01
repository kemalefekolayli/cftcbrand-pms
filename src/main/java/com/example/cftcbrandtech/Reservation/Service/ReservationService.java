package com.example.cftcbrandtech.Reservation.Service;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Property.PropertyModel;
import com.example.cftcbrandtech.Property.PropertyModelRepository;
import com.example.cftcbrandtech.Reservation.Dto.CreateReservationDto;
import com.example.cftcbrandtech.Reservation.Dto.UpdateReservationDto;
import com.example.cftcbrandtech.Reservation.Mapper.ReservationModelMapper;
import com.example.cftcbrandtech.Reservation.Repositories.ReservationModelRepository;
import com.example.cftcbrandtech.Reservation.ReservationModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationModelRepository reservationModelRepository;
    private final ReservationModelMapper reservationModelMapper;
    private final PropertyModelRepository propertyModelRepository;

    @Transactional
    public ReservationModel createReservation(CreateReservationDto dto) {
        // 1. Get property
        PropertyModel property = propertyModelRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new GlobalException(ErrorCodes.PROPERTY_COULD_NOT_BE_FOUND));

        // 2. Count overlapping reservations
        Long overlappingCount = reservationModelRepository.countOverlappingReservations(
                dto.getPropertyId(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        // 3. Check availability
        if (overlappingCount >= property.getTotalVillas()) {
            throw new GlobalException(ErrorCodes.RESERVATION_COULD_NOT_BE_CREATED);
        }

        // 4. Create reservation
        ReservationModel reservation = reservationModelMapper.toEntity(dto);
        return reservationModelRepository.save(reservation);
    }

    public ReservationModel getReservationById(Long id) {
        return reservationModelRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCodes.RESERVATION_COULD_NOT_BE_CREATED));
    }

    public List<ReservationModel> getAllReservations() {
        return reservationModelRepository.findAll();
    }

    public List<ReservationModel> getReservationsByProperty(Long propertyId) {
        return reservationModelRepository.findByPropertyId(propertyId);
    }

    @Transactional
    public ReservationModel updateReservation(Long id, UpdateReservationDto dto) {
        ReservationModel reservation = reservationModelRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCodes.RESERVATION_COULD_NOT_BE_CREATED));

        // Update fields if provided
        if (dto.getFirstName() != null && !dto.getFirstName().isEmpty()) {
            reservation.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null && !dto.getLastName().isEmpty()) {
            reservation.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            reservation.setEmail(dto.getEmail());
        }
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {
            reservation.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getStatus() != null) {
            reservation.setStatus(dto.getStatus());
        }
        if (dto.getTotalPrice() != null) {
            reservation.setTotalPrice(dto.getTotalPrice());
        }
        if (dto.getNotes() != null) {
            reservation.setNotes(dto.getNotes());
        }

        // TODO: If dates are being updated, check availability again
        if (dto.getStartTime() != null || dto.getEndTime() != null) {
            // For now, we'll just update them
            // In production, you should check availability for the new dates
            if (dto.getStartTime() != null) {
                reservation.setStartTime(dto.getStartTime());
            }
            if (dto.getEndTime() != null) {
                reservation.setEndTime(dto.getEndTime());
            }
        }

        return reservationModelRepository.save(reservation);
    }

    @Transactional
    public void cancelReservation(Long id) {
        ReservationModel reservation = reservationModelRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCodes.RESERVATION_COULD_NOT_BE_CREATED));

        reservation.setStatus(ReservationModel.ReservationStatus.CANCELLED);
        reservationModelRepository.save(reservation);
    }
}