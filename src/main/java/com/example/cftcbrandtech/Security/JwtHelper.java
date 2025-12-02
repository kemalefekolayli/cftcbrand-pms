package com.example.cftcbrandtech.Security;

import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.User.Dto.UserProfileDto;
import com.example.cftcbrandtech.User.Service.UserService;
import com.example.cftcbrandtech.User.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtHelper {

    private final UserService userService;

    public UserProfileDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User userDetails) {
            UserEntity user = userService.getByEmail(userDetails.getUsername());
            return toProfile(user);
        }
        throw new GlobalException(ErrorCodes.AUTH_TOKEN_INVALID);
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }

    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }

    private UserProfileDto toProfile(UserEntity user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }
}
