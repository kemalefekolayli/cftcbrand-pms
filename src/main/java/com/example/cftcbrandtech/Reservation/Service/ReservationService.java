package com.example.cftcbrandtech.Reservation.Service;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Property.PropertyModel;
import com.example.cftcbrandtech.Reservation.Dto.CreateReservationDto;
import com.example.cftcbrandtech.Reservation.Mapper.RFTGMapper;
import com.example.cftcbrandtech.Reservation.Mapper.ReservationModelMapper;
import com.example.cftcbrandtech.Reservation.Repositories.ReservationModelRepository;
import com.example.cftcbrandtech.Reservation.ReservationForGivenTime;
import com.example.cftcbrandtech.Reservation.ReservationModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationModelRepository reservationModelRepository;
    private final ReservationModelMapper reservationModelMapper;
    private final RFGTService rfgtService;
    private final RFTGMapper rftgMapper;

    @Transactional
    public ReservationModel createReservation(CreateReservationDto dto){
        try {
            ReservationModel reservation = reservationModelMapper.toEntity(dto);
            rftg.setReservationId(reservation.getId());

            return reservationModelRepository.save(reservation); // todo burda olmayacak
        } catch (Exception e) {
            throw new GlobalException(ErrorCodes.RESERVATION_COULD_NOT_BE_CREATED);
        }

    }

    @Transactional
    private Int returnVillaAmount(PropertyModel property){

    }
}
