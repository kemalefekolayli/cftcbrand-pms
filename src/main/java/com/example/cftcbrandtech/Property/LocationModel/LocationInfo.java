package com.example.cftcbrandtech.Property.LocationModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="location")
@NoArgsConstructor
@AllArgsConstructor
public class LocationInfo {

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
