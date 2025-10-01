package com.example.cftcbrandtech.Availabilty;



import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Property.PropertyModel;
import com.example.cftcbrandtech.Property.PropertyModelRepository;
import com.example.cftcbrandtech.Reservation.ReservationModel;
import com.example.cftcbrandtech.Reservation.Repositories.ReservationModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final ReservationModelRepository reservationModelRepository;
    private final PropertyModelRepository propertyModelRepository;

    /**
     * Get all bookings for a property in a specific month
     * @param propertyId The property ID
     * @param year The year (e.g., 2025)
     * @param month The month (1-12)
     * @return MonthlyAvailabilityDto containing all bookings
     */
    public MonthlyAvailabilityDto getMonthlyAvailability(Long propertyId, int year, int month) {
        // Validate property exists
        PropertyModel property = propertyModelRepository.findById(propertyId)
                .orElseThrow(() -> new GlobalException(ErrorCodes.PROPERTY_COULD_NOT_BE_FOUND));

        // Calculate month start and end
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        // Get all reservations that overlap with this month
        List<ReservationModel> reservations = reservationModelRepository
                .findByPropertyIdAndDateRange(propertyId, startOfMonth, endOfMonth);

        // Convert to DTOs
        List<ReservationBlockDto> bookings = reservations.stream()
                .filter(r -> r.getStatus() != ReservationModel.ReservationStatus.CANCELLED)
                .map(this::convertToBlockDto)
                .collect(Collectors.toList());

        return MonthlyAvailabilityDto.builder()
                .propertyId(propertyId)
                .propertyName(property.getName())
                .year(year)
                .month(month)
                .totalVillas(property.getTotalVillas())
                .bookings(bookings)
                .build();
    }

    /**
     * Check if a property is available for given dates
     * @param propertyId The property ID
     * @param startTime Check-in date/time
     * @param endTime Check-out date/time
     * @return true if available, false otherwise
     */
    public boolean isAvailable(Long propertyId, LocalDateTime startTime, LocalDateTime endTime) {
        PropertyModel property = propertyModelRepository.findById(propertyId)
                .orElseThrow(() -> new GlobalException(ErrorCodes.PROPERTY_COULD_NOT_BE_FOUND));

        long overlappingCount = reservationModelRepository.countOverlappingReservations(
                propertyId, startTime, endTime
        );

        return overlappingCount < property.getTotalVillas();
    }

    /**
     * Get available villa count for specific dates
     * @param propertyId The property ID
     * @param startTime Check-in date/time
     * @param endTime Check-out date/time
     * @return Number of available villas
     */
    public int getAvailableVillaCount(Long propertyId, LocalDateTime startTime, LocalDateTime endTime) {
        PropertyModel property = propertyModelRepository.findById(propertyId)
                .orElseThrow(() -> new GlobalException(ErrorCodes.PROPERTY_COULD_NOT_BE_FOUND));

        long bookedCount = reservationModelRepository.countOverlappingReservations(
                propertyId, startTime, endTime
        );

        return (int) (property.getTotalVillas() - bookedCount);
    }

    private ReservationBlockDto convertToBlockDto(ReservationModel reservation) {
        return ReservationBlockDto.builder()
                .reservationId(reservation.getId())
                .guestName(reservation.getFirstName() + " " + reservation.getLastName())
                .checkIn(reservation.getStartTime())
                .checkOut(reservation.getEndTime())
                .status(reservation.getStatus())
                .totalPrice(reservation.getTotalPrice())
                .build();
    }
}