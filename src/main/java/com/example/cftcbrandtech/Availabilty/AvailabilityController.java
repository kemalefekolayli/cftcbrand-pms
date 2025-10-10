package com.example.cftcbrandtech.Availabilty;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.Dto.SupabaseUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final JwtHelper jwtHelper;

    /**
     * Get monthly availability for a property
     */
    @GetMapping("/property/{propertyId}/month")
    public ResponseEntity<MonthlyAvailabilityDto> getMonthlyAvailability(
            @PathVariable Long propertyId,
            @RequestParam int year,
            @RequestParam int month) {

        // Token kontrolü - Security context'te yoksa exception fırlatır
        SupabaseUserInfo currentUser = jwtHelper.getCurrentUser();

        // Validate month
        if (month < 1 || month > 12) {
            throw new GlobalException(ErrorCodes.VALIDATION_ERROR);
        }

        MonthlyAvailabilityDto availability = availabilityService
                .getMonthlyAvailability(propertyId, year, month);

        return ResponseEntity.ok(availability);
    }

    /**
     * Check if property is available for given dates
     */
    @GetMapping("/property/{propertyId}/check")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @PathVariable Long propertyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        SupabaseUserInfo currentUser = jwtHelper.getCurrentUser();

        boolean isAvailable = availabilityService.isAvailable(propertyId, startTime, endTime);
        int availableVillas = availabilityService.getAvailableVillaCount(propertyId, startTime, endTime);

        return ResponseEntity.ok(Map.of(
                "propertyId", propertyId,
                "startTime", startTime,
                "endTime", endTime,
                "isAvailable", isAvailable,
                "availableVillas", availableVillas,
                "requestedBy", currentUser.getEmail() // Opsiyonel: kim sordu
        ));
    }

    /**
     * Get available villa count for specific dates
     */
    @GetMapping("/property/{propertyId}/count")
    public ResponseEntity<Map<String, Object>> getAvailableCount(
            @PathVariable Long propertyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        SupabaseUserInfo currentUser = jwtHelper.getCurrentUser();

        int availableVillas = availabilityService.getAvailableVillaCount(propertyId, startTime, endTime);

        return ResponseEntity.ok(Map.of(
                "propertyId", propertyId,
                "availableVillas", availableVillas
        ));
    }
}