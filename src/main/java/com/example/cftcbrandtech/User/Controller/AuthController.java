package com.example.cftcbrandtech.User.Controller;

import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.Dto.UserLoginDto;
import com.example.cftcbrandtech.User.Dto.UserRegisDto;
import com.example.cftcbrandtech.User.Service.UserModelService;
import com.example.cftcbrandtech.User.UserModel;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class AuthController {

    private final UserModelService userModelService;

    private final JwtHelper jwtHelper; // we gonna eventually remove the token creation logic from this class else where

    @PostMapping("register")
    public ResponseEntity<?> RegisterUser(@Valid @RequestBody UserRegisDto dto){
        if (userModelService.RegisterUser(dto)) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody UserLoginDto dto) {
        UserModel user = userModelService.LoginUser(dto);
        String token = jwtHelper.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
