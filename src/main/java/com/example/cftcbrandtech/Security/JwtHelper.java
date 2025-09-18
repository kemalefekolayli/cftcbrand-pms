package com.example.cftcbrandtech.Security;

import com.example.cftcbrandtech.User.Repository.UserModelRepository;
import com.example.cftcbrandtech.User.UserModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtHelper {

    private final UserModelRepository userModelRepository;
    private final String SECRET_KEY = "mySecretKeyForJWTTokenGenerationThatShouldBeAtLeast256Bits"; // cant store this here will move it to somwhere else

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Token generate et
    public String generateToken(UserModel user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("firstName", user.getFirstName())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(86400))) // 24 saat
                .signWith(getSigningKey())
                .compact();
    }

    // Token'dan user email çıkar
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Token'dan user bul
    public UserModel getUserFromToken(String token) {
        String email = getEmailFromToken(token);
        return userModelRepository.findByEmail(email).orElse(null);
    }
}