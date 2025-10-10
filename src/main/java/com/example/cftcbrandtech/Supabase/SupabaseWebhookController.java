package com.example.cftcbrandtech.Supabase;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/supabase/webhook")
@RequiredArgsConstructor
public class SupabaseWebhookController {

    private final SupabaseWebhookService webhookService;

    @PostMapping
    public ResponseEntity<?> handleWebhook(
            @RequestHeader("x-webhook-signature") String signature,
            @RequestBody Map<String, Object> payload) {

        log.info("Received Supabase webhook: {}", payload);

        // Verify webhook signature
        if (!verifySignature(payload, signature)) {
            log.error("Invalid webhook signature");
            return ResponseEntity.status(401).body("Invalid signature");
        }

        try {
            String eventType = (String) payload.get("type");

            switch (eventType) {
                case "INSERT" -> {
                    Map<String, Object> record = (Map<String, Object>) payload.get("record");
                    webhookService.handleUserCreated(record);
                }
                case "UPDATE" -> {
                    Map<String, Object> record = (Map<String, Object>) payload.get("record");
                    webhookService.handleUserUpdated(record);
                }
                case "DELETE" -> {
                    Map<String, Object> oldRecord = (Map<String, Object>) payload.get("old_record");
                    webhookService.handleUserDeleted(oldRecord);
                }
                default -> log.warn("Unknown event type: {}", eventType);
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(500).body("Internal error");
        }
    }

    private boolean verifySignature(Map<String, Object> payload, String signature) {
        try {
            String secret = System.getenv("SUPABASE_WEBHOOK_SECRET");
            String payloadString = payload.toString(); // In production, use proper JSON serialization

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(payloadString.getBytes(StandardCharsets.UTF_8));
            String computedSignature = HexFormat.of().formatHex(hash);

            return computedSignature.equals(signature);

        } catch (Exception e) {
            log.error("Error verifying signature", e);
            return false;
        }
    }
}