package com.transport.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.annotations.QueryEntity;
import com.transport.entity.base.BaseEntity;
import com.transport.enums.VehicleStatus;

@Entity
@Table(name = "vehicles", indexes = {
    @Index(name = "idx_license_plate", columnList = "license_plate"),
    @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@QueryEntity
public class Vehicle extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "license_plate") 
    private String licensePlate;// Biển số xe
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType; // Loại xe (FK tới bảng loại xe)
    
    @Column(name = "brand")
    private String brand; // Thương hiệu xe 
    
    @Column(name = "model")
    private String model;// Model/kiểu xe
    
    @Column(name = "manufacture_year")
    private Integer manufactureYear; // Năm sản xuất
    
    @Column(name = "color")
    private String color; // Màu sắc xe
    
    @Column(name = "vin", unique = true)
    private String vin;  // Số khung xe (VIN - Vehicle Identification Number, duy nhất)
    
    @Column(name = "engine_number")
    private String engineNumber;  // Số máy/động cơ
    
    @Column(name = "registration_date")
    private LocalDate registrationDate; // Ngày hết hạn kiểm định
    
    @Column(name = "inspection_expiry_date")
    private LocalDate inspectionExpiryDate;  // Ngày hết hạn bảo hiểm
    
    @Column(name = "insurance_expiry_date")
    private LocalDate insuranceExpiryDate; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VehicleStatus status;
    
    @Column(name = "purchase_date")
    private LocalDate purchaseDate; // Ngày mua xe
    
    @Column(name = "note", length = 1000)
    private String note;
    
    @OneToMany(mappedBy = "vehicle")
    @Builder.Default
    private List<Trip> trips = new ArrayList<>();
}