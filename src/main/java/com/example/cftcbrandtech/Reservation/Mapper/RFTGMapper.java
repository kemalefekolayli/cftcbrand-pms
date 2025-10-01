package com.example.cftcbrandtech.Reservation.Mapper;

import com.example.cftcbrandtech.Reservation.Dto.CreateReservationDto;
import com.example.cftcbrandtech.Reservation.Dto.RFTGDTO;
import com.example.cftcbrandtech.Reservation.ReservationForGivenTime;
import org.springframework.stereotype.Component;

@Component
public class RFTGMapper {

    public ReservationForGivenTime toEntity (RFTGDTO dto){
        ReservationForGivenTime reservationForGivenTime = new ReservationForGivenTime();
        reservationForGivenTime.setPropertyId(dto.getPropertyId());
        reservationForGivenTime.setReservationId(dto.getReservationId());
        reservationForGivenTime.setStartTime(dto.getStartTime());
        reservationForGivenTime.setEndTime(dto.getEndTime());
        return  reservationForGivenTime;
    }

    public RFTGDTO fromCRDto (CreateReservationDto dto){
        RFTGDTO rftgDTO = new RFTGDTO();
        rftgDTO.setPropertyId(dto.getPropertyId());
        rftgDTO.setStartTime(dto.getStartTime());
        rftgDTO.setEndTime(dto.getEndTime());
        return  rftgDTO;
    }
}
