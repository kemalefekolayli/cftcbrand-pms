package com.example.cftcbrandtech.User.Controller;

import com.example.cftcbrandtech.User.Dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class AuthController {

    @PostMapping("/register")
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
                      "email_confirm": true,
                      "user_metadata": {
                        "firstName": "%s",
                        "lastName": "%s",
                        "phoneNumber": "%s"
                      }
                    }
                """.formatted(dto.getEmail(), dto.getPassword(),
                            dto.getFirstName(), dto.getLastName(), dto.getPhoneNumber())))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 && response.statusCode() != 201) {
                return ResponseEntity.status(response.statusCode())
                        .body("Supabase user creation failed: " + response.body());
            }

            return ResponseEntity.ok("User created successfully in Supabase");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String supabaseUrl = System.getenv("SUPABASE_URL");
            String serviceRoleKey = System.getenv("SUPABASE_SERVICE_ROLE_KEY");

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
                // Response içinde access_token ve user bilgileri var
                return ResponseEntity.ok(response.body());
            } else {
                return ResponseEntity.status(response.statusCode())
                        .body("Login failed: " + response.body());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}