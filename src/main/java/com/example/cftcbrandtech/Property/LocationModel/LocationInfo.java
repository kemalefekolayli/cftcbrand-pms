package com.example.cftcbrandtech.Property.LocationModel;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="location")
public class LocationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String locationName;

    @Column(nullable=true)
    private String locationAddress;

    @Column(nullable=true)
    private String locationCity;

    @Column(nullable=true)
    private String locationState;


}
