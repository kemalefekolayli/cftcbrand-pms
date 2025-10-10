package com.example.cftcbrandtech.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoint'lerin (gerekliyse) burada kalsın:
                        .requestMatchers("/api/public/**").permitAll()
                        // Şu an sen bunları açmışsın; istersen geçici olarak kalsın:
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/supabase/**").permitAll()
                        // Korunanlar:
                        .requestMatchers("/api/availability/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    // Şimdilik default bırakıyoruz; ileride Supabase claim -> ROLE_ map’leriz.
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String supabaseProjectRef = System.getenv("SUPABASE_PROJECT_REF");
        String jwksUri = "https://" + supabaseProjectRef + ".supabase.co/auth/v1/keys";
        return NimbusJwtDecoder.withJwkSetUri(jwksUri).build();
    }
}
