package com.example.cftcbrandtech.Property;

import com.example.cftcbrandtech.Property.LocationModel.LocationInfo;
import com.example.cftcbrandtech.Property.PropertyInfo.PropertyInfo;
import com.example.cftcbrandtech.Property.PropertyPaymentInfo.PropertyPaymentInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "property")
@Data
public class PropertyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "total_villas", nullable = false)
    private Integer totalVillas = 1;

    @ManyToOne
    @JoinColumn(name = "property_info_id")
    private PropertyInfo propertyInfo;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationInfo location;

    @ManyToOne
    @JoinColumn(name = "property_payment_info_id")
    private PropertyPaymentInfo propertyPaymentInfo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}