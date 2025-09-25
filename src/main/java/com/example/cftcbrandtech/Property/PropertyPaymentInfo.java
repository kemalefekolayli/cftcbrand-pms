package com.example.cftcbrandtech.Property;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Table(name = "property_payment_info")
public class PropertyPaymentInfo {


    @Column(name = "nightly_rate")
    private Integer nightlyRate; // Villa başına gecelik fiyat

    @Column(name = "cleaning_fee")
    private Integer cleaningFee; // Villa başına temizlik ücreti

    @Column(name = "security_deposit")
    private Integer securityDeposit;

    @Column(name = "service_fee_rate")
    private BigDecimal serviceFeeRate;

}
