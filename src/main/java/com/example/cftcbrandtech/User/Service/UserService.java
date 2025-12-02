package com.example.cftcbrandtech.User.Service;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.Security.JwtService;
import com.example.cftcbrandtech.User.Dto.AuthResponse;
import com.example.cftcbrandtech.User.Dto.LoginRequest;
import com.example.cftcbrandtech.User.Dto.UserProfileDto;
import com.example.cftcbrandtech.User.Dto.UserRegisDto;
import com.example.cftcbrandtech.User.Repositories.UserRepository;
import com.example.cftcbrandtech.User.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(UserRegisDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new GlobalException(ErrorCodes.AUTH_EMAIL_TAKEN);
        }

        UserEntity user = UserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);
        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new GlobalException(ErrorCodes.AUTH_INVALID_CREDENTIALS);
        }

        return buildAuthResponse(user);
    }

    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));
    }

    public UserProfileDto toProfile(UserEntity user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getRole())
                .build();
    }

    private AuthResponse buildAuthResponse(UserEntity user) {
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .user(toProfile(user))
                .build();
    }
}
