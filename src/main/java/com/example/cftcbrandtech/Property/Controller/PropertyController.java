package com.example.cftcbrandtech.Property.Controller;

import com.example.cftcbrandtech.Property.Dto.PropertyCreateDto;
import com.example.cftcbrandtech.Property.PropertyModel;
import com.example.cftcbrandtech.Property.Service.PropertyService;
import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.Dto.SupabaseUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    private final JwtHelper jwtHelper;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProperty(@Valid @RequestBody PropertyCreateDto dto) {
        SupabaseUserInfo currentUser = jwtHelper.getCurrentUser();

        PropertyModel property = propertyService.createProperty(dto);

        return ResponseEntity.ok(Map.of(
                "property", property,
                "createdBy", currentUser.getEmail()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProperty(@PathVariable Long id) {
        SupabaseUserInfo currentUser = jwtHelper.getCurrentUser();

        propertyService.deleteProperty(id);

        return ResponseEntity.ok(Map.of(
                "message", "Property deleted successfully",
                "deletedBy", currentUser.getEmail()
        ));
    }
}