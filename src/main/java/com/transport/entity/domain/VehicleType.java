package com.transport.entity.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

import com.querydsl.core.annotations.QueryEntity;


import java.util.ArrayList;

@Entity
@Table(name = "vehicle_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@QueryEntity
public class VehicleType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code", unique = true)
    private String code;// Mã loại xe
    
    @Column(name = "name")
    private String name; // Tên loại xe
    
    @Column(name = "max_payload")
    private BigDecimal maxPayload; // Tải trọng tối đa của loại xe 
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    
    @OneToMany(mappedBy = "vehicleType")
    @Builder.Default
    private List<Vehicle> vehicles = new ArrayList<>();
}