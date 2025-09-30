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

    @Column(nullable = false)
    private Integer size;

    @Column(name = "bed_num")
    private Integer bedNum;

    @Column(name = "wc_num")
    private Integer wcNum;

    @Column(name = "person_size")
    private Integer personCapacity;

    @Column(name = "alan")
    private Float alan;

    @Column(name = "animals_allowed")
    private Boolean animalsAllowed;

    @Column(name = "villa_type")
    private String villaType;

}
