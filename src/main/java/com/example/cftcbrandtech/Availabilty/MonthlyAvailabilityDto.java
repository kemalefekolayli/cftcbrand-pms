package com.example.cftcbrandtech.Availabilty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAvailabilityDto {

    private Long propertyId;
    private String propertyName;
    private int year;
    private int month;
    private int totalVillas;
    private List<ReservationBlockDto> bookings;

    // Computed fields
    public int getTotalBookings() {
        return bookings != null ? bookings.size() : 0;
    }
}