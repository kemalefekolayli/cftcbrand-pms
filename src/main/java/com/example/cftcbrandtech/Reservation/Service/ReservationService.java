package com.example.cftcbrandtech.Reservation.Service;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Property.PropertyModel;
import com.example.cftcbrandtech.Property.PropertyModelRepository;
import com.example.cftcbrandtech.Reservation.Dto.CreateReservationDto;
import com.example.cftcbrandtech.Reservation.Mapper.ReservationModelMapper;
import com.example.cftcbrandtech.Reservation.Repositories.ReservationModelRepository;
import com.example.cftcbrandtech.Reservation.ReservationModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
