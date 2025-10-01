package com.example.cftcbrandtech.Availabilty;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.UserModel;
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
     * GET /api/availability/property/{propertyId}/month?year=2025&month=3
     */
    @GetMapping("/property/{propertyId}/month")
    public ResponseEntity<MonthlyAvailabilityDto> getMonthlyAvailability(
            @RequestHeader("Authorization") String token,
            @PathVariable Long propertyId,
            @RequestParam int year,
            @RequestParam int month) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

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
     * GET /api/availability/property/{propertyId}/check?startTime=2025-03-15T14:00:00&endTime=2025-03-20T11:00:00
     */
    @GetMapping("/property/{propertyId}/check")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @RequestHeader("Authorization") String token,
            @PathVariable Long propertyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        boolean isAvailable = availabilityService.isAvailable(propertyId, startTime, endTime);
        int availableVillas = availabilityService.getAvailableVillaCount(propertyId, startTime, endTime);

        return ResponseEntity.ok(Map.of(
                "propertyId", propertyId,
                "startTime", startTime,
                "endTime", endTime,
                "isAvailable", isAvailable,
                "availableVillas", availableVillas
        ));
    }

    /**
     * Get available villa count for specific dates
     * GET /api/availability/property/{propertyId}/count?startTime=2025-03-15T14:00:00&endTime=2025-03-20T11:00:00
     */
    @GetMapping("/property/{propertyId}/count")
    public ResponseEntity<Map<String, Object>> getAvailableCount(
            @RequestHeader("Authorization") String token,
            @PathVariable Long propertyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        int availableVillas = availabilityService.getAvailableVillaCount(propertyId, startTime, endTime);

        return ResponseEntity.ok(Map.of(
                "propertyId", propertyId,
                "availableVillas", availableVillas
        ));
    }
}