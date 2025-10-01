package com.example.cftcbrandtech.Reservation.Service;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Property.PropertyModel;
import com.example.cftcbrandtech.Property.PropertyModelRepository;
import com.example.cftcbrandtech.Reservation.Dto.CreateReservationDto;
import com.example.cftcbrandtech.Reservation.Dto.RFTGDTO;
import com.example.cftcbrandtech.Reservation.Mapper.RFTGMapper;
import com.example.cftcbrandtech.Reservation.Repositories.RFGTRepository;
import com.example.cftcbrandtech.Reservation.ReservationForGivenTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RFGTService {

    private final RFGTRepository rfgtRepository;
    private final PropertyModelRepository propertyModelRepository;
    private final RFTGMapper rftgMapper;

    public ReservationForGivenTime createRFGT( RFTGDTO dto ) { // ÖNCE RFTG OLUŞTURS SONRA COUNTI KARŞILAŞTIR EĞER OLMAZSA RFTG SILINIYO
        PropertyModel property = propertyModelRepository.findById(dto.getPropertyId()).orElseThrow(() -> new GlobalException(ErrorCodes.PROPERTY_COULD_NOT_BE_FOUND));
        ReservationForGivenTime rftg = rftgMapper.toEntity(dto);
        Long conflictCount = rfgtRepository.countConflicts(rftg.getPropertyId(),rftg.getStartTime(),rftg.getEndTime());
        if(conflictCount >= property.getTotalVillas()) {
            throw new GlobalException(ErrorCodes.RFTG_COULD_NOT_BE_CREATED);
        }
        else {
            rfgtRepository.save(rftg);
            return rftg;
        }
    }

    public ReservationForGivenTime createRFGT(CreateReservationDto crDto) {
        RFTGDTO dto = rftgMapper.fromCRDto(crDto);
        PropertyModel property = propertyModelRepository.findById(dto.getPropertyId()).orElseThrow(() -> new GlobalException(ErrorCodes.PROPERTY_COULD_NOT_BE_FOUND));
        ReservationForGivenTime rftg = rftgMapper.toEntity(dto);
        Long conflictCount = rfgtRepository.countConflicts(rftg.getPropertyId(),rftg.getStartTime(),rftg.getEndTime());
        if(conflictCount >= property.getTotalVillas()) {
            throw new GlobalException(ErrorCodes.RFTG_COULD_NOT_BE_CREATED);
        }
        else {
            rfgtRepository.save(rftg);
            return rftg;
        }
    }
}
