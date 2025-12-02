package com.example.cftcbrandtech.User.Controller;

import com.example.cftcbrandtech.User.Dto.AuthResponse;
import com.example.cftcbrandtech.User.Dto.LoginRequest;
import com.example.cftcbrandtech.User.Dto.UserRegisDto;
import com.example.cftcbrandtech.User.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserRegisDto dto) {
        AuthResponse response = userService.register(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}