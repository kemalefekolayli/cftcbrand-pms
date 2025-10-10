package com.example.cftcbrandtech.Supabase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupabaseWebhookService {

    private final UserModelRepository userModelRepository;

    @Transactional
    public void handleUserCreated(Map<String, Object> record) {
        String supabaseId = (String) record.get("id");
        String email = (String) record.get("email");

        // Check if user already exists
        if (userModelRepository.findBySupabaseId(supabaseId).isPresent()) {
            log.info("User already exists: {}", supabaseId);
            return;
        }

        // Extract metadata if available
        Map<String, Object> rawUserMetadata = (Map<String, Object>) record.get("raw_user_meta_data");

        String firstName = rawUserMetadata != null ?
                (String) rawUserMetadata.get("firstName") : "User";
        String lastName = rawUserMetadata != null ?
                (String) rawUserMetadata.get("lastName") : "";

        // Create user in database
        UserModel user = new UserModel();
        user.setSupabaseId(supabaseId);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(true);

        userModelRepository.save(user);
        log.info("Created user in database: {}", email);
    }

    @Transactional
    public void handleUserUpdated(Map<String, Object> record) {
        String supabaseId = (String) record.get("id");
        String email = (String) record.get("email");

        userModelRepository.findBySupabaseId(supabaseId).ifPresent(user -> {
            user.setEmail(email);
            user.setUpdatedAt(LocalDateTime.now());

            // Update other fields from metadata if needed
            Map<String, Object> rawUserMetadata = (Map<String, Object>) record.get("raw_user_meta_data");
            if (rawUserMetadata != null) {
                if (rawUserMetadata.containsKey("firstName")) {
                    user.setFirstName((String) rawUserMetadata.get("firstName"));
                }
                if (rawUserMetadata.containsKey("lastName")) {
                    user.setLastName((String) rawUserMetadata.get("lastName"));
                }
            }

            userModelRepository.save(user);
            log.info("Updated user in database: {}", email);
        });
    }

    @Transactional
    public void handleUserDeleted(Map<String, Object> oldRecord) {
        String supabaseId = (String) oldRecord.get("id");

        userModelRepository.findBySupabaseId(supabaseId).ifPresent(user -> {
            user.setActive(false);
            user.setUpdatedAt(LocalDateTime.now());
            userModelRepository.save(user);
            log.info("Soft deleted user: {}", user.getEmail());
        });
    }
}