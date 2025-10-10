package com.example.cftcbrandtech.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupabaseUserInfo {
    private String userId;          // sub claim
    private String email;           // email claim
    private String role;            // role claim
    private Map<String, Object> userMetadata;  // user_metadata claim
    private Map<String, Object> appMetadata;   // app_metadata claim

}