package com.example.cftcbrandtech.Reservation.Mapper;

import com.example.cftcbrandtech.Reservation.Dto.CreateReservationDto;
import com.example.cftcbrandtech.Reservation.Dto.ReservationResponseDto;
import com.example.cftcbrandtech.Reservation.ReservationModel;
import org.springframework.stereotype.Component;

@Component
public class ReservationModelMapper {

    public ReservationModel toEntity(CreateReservationDto dto) {
        ReservationModel reservation = new ReservationModel();
        reservation.setFirstName(dto.getFirstName());
        reservation.setLastName(dto.getLastName());
        reservation.setEmail(dto.getEmail());
        reservation.setPhoneNumber(dto.getPhoneNumber());
        reservation.setTcKimlikNo(dto.getTcKimlikNo());
        reservation.setPropertyId(dto.getPropertyId());
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setTotalPrice(dto.getTotalPrice());
        reservation.setNotes(dto.getNotes());
        reservation.setStatus(ReservationModel.ReservationStatus.PENDING);
        return reservation;
    }

    public ReservationResponseDto toResponseDto(ReservationModel reservation) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setId(reservation.getId());
        dto.setFirstName(reservation.getFirstName());
        dto.setLastName(reservation.getLastName());
        dto.setEmail(reservation.getEmail());
        dto.setPhoneNumber(reservation.getPhoneNumber());
        dto.setTcKimlikNo(reservation.getTcKimlikNo());
        dto.setPropertyId(reservation.getPropertyId());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setStatus(reservation.getStatus());
        dto.setTotalPrice(reservation.getTotalPrice());
        dto.setNotes(reservation.getNotes());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());
        return dto;
    }
}