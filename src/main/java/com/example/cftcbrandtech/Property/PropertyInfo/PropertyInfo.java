package com.example.cftcbrandtech.Property.PropertyInfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="property_info")
@NoArgsConstructor
@AllArgsConstructor
public class PropertyInfo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;


}
