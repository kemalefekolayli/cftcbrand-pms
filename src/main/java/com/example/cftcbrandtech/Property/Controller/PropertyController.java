package com.example.cftcbrandtech.Property.Controller;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Property.Dto.PropertyCreateDto;
import com.example.cftcbrandtech.Property.PropertyModel;
import com.example.cftcbrandtech.Property.Service.PropertyService;
import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.UserModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    private final JwtHelper jwtHelper;

    @PostMapping
    public ResponseEntity<PropertyModel> createProperty(@RequestHeader("Authorization") String token, @Valid @RequestBody PropertyCreateDto dto) {
        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));

        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        PropertyModel property = propertyService.createProperty(dto);
        return ResponseEntity.ok(property);
    } // should not return this fix it

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));

        if (currentUser == null) {
            throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
        }

        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Property deleted successfully");
    }
}