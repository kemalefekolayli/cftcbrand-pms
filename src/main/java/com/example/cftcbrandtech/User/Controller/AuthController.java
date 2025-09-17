package com.example.cftcbrandtech.User.Controller;

import com.example.cftcbrandtech.User.Dto.UserRegisDto;
import com.example.cftcbrandtech.User.Service.UserModelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class AuthController {

    private final UserModelService userModelService;

    @PostMapping("register")
    public ResponseEntity<?> RegisterUser(@RequestHeader("Authorization" ) String token , @Valid @RequestBody UserRegisDto dto){
        if (userModelService.RegisterUser(dto)) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
