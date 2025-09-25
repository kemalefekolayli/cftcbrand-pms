
package com.example.cftcbrandtech.Property;

import com.example.cftcbrandtech.Property.PropertyPaymentInfo;
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

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "property_address", nullable = false)
    private String propertyAddress;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "total_villas", nullable = false)
    private Integer totalVillas = 1; // Default: tek villa

    @Column(name = "villa_type")
    private String villaType;

    @Column(nullable = false)
    private Integer size;

    @Column(name = "bed_num", nullable = false)
    private Integer bedNum; // Her villa'daki yatak sayısı

    @Column(name = "person_size", nullable = false)
    private Integer personSize; // Her villa'nın max kapasitesi

    @Column(name = "animals_allowed")
    private Boolean animalsAllowed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany
    @JoinColumn(name = "property_payment_info", nullable = true) // todo şimdilik nullable
    private PropertyPaymentInfo propertyPaymentInfo;


}