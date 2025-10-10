package com.example.cftcbrandtech.User.Controller;

import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.Dto.SupabaseUserInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final JwtHelper jwtHelper;

    /**
     * Get current user profile from JWT token
     */
    @GetMapping("/me")
    public ResponseEntity<SupabaseUserInfo> getCurrentUserProfile() {
        SupabaseUserInfo currentUser = jwtHelper.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }

    /**
     * Check if user has specific role
     */
    @GetMapping("/has-role/{role}")
    public ResponseEntity<Boolean> hasRole(@PathVariable String role) {
        boolean hasRole = jwtHelper.hasRole(role);
        return ResponseEntity.ok(hasRole);
    }
}