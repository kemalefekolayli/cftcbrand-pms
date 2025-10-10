package com.example.cftcbrandtech.User.Controller;

import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.Dto.LoginRequest;
import com.example.cftcbrandtech.User.Dto.UserLoginDto;
import com.example.cftcbrandtech.User.Dto.UserRegisDto;
import com.example.cftcbrandtech.User.Service.UserModelService;
import com.example.cftcbrandtech.User.UserModel;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class AuthController {

    private final UserModelService userModelService;

    private final JwtHelper jwtHelper; // we gonna eventually remove the token creation logic from this class else where

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisDto dto) {

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(System.getenv("SUPABASE_URL") + "/auth/v1/admin/users"))
                    .header("apikey", System.getenv("SUPABASE_SERVICE_ROLE_KEY"))
                    .header("Authorization", "Bearer " + System.getenv("SUPABASE_SERVICE_ROLE_KEY"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("""
                    {
                      "email": "%s",
                      "password": "%s",
                      "email_confirm": true
                    }
                """.formatted(dto.getEmail(), dto.getPassword())))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 && response.statusCode() != 201) {
                return ResponseEntity.status(response.statusCode())
                        .body("Supabase user creation failed: " + response.body());
            }
            return ResponseEntity.ok("User created via Supabase");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String supabaseUrl = System.getenv("SUPABASE_URL");
            String serviceRoleKey = System.getenv("SUPABASE_SERVICE_ROLE_KEY");

            // Prepare Supabase Auth login request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(supabaseUrl + "/auth/v1/token?grant_type=password"))
                    .header("apikey", serviceRoleKey)
                    .header("Authorization", "Bearer " + serviceRoleKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                          "email": "%s",
                          "password": "%s"
                        }
                    """.formatted(loginRequest.getEmail(), loginRequest.getPassword())))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return ResponseEntity.ok(response.body());
            } else {
                return ResponseEntity.status(response.statusCode())
                        .body("Login failed: " + response.body());
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

}
