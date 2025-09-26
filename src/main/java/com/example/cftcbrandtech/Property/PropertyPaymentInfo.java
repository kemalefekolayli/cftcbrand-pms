package com.example.cftcbrandtech.Property;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "property_payment_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyPaymentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nightly_rate", nullable = false)
    private BigDecimal nightlyRate; // Villa başına gecelik fiyat

    @Column(name = "cleaning_fee")
    private BigDecimal cleaningFee; // Villa başına temizlik ücreti

    @Column(name = "security_deposit")
    private BigDecimal securityDeposit;

    @Column(name = "service_fee_rate")
    private BigDecimal serviceFeeRate; // Percentage (e.g., 0.10 for 10%)

    @Column(name = "weekend_rate")
    private BigDecimal weekendRate; // Hafta sonu gecelik fiyat

    @Column(name = "weekly_discount")
    private BigDecimal weeklyDiscount; // Percentage discount for weekly stays

    @Column(name = "monthly_discount")
    private BigDecimal monthlyDiscount; // Percentage discount for monthly stays

    @Column(name = "min_nights")
    private Integer minNights = 1; // Minimum stay requirement

    @Column(name = "max_nights")
    private Integer maxNights = 365; // Maximum stay allowed
}