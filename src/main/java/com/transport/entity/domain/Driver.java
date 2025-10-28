package com.transport.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.transport.entity.base.BaseEntity;
import com.transport.enums.EmploymentStatus;

@Entity
@Table(name = "drivers", indexes = {
    @Index(name = "idx_driver_code", columnList = "driver_code"),
    @Index(name = "idx_license_number", columnList = "license_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver extends BaseEntity {
    
    @Id
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;
    
    @Column(name = "driver_code", unique = true)
    private String driverCode; // Mã định danh tài xế duy nhất
    
    @Column(name = "license_number", unique = true)
    private String licenseNumber;  // Số giấy phép lái xe (GPLX)
    
    @Column(name = "license_class", nullable = false)
    private String licenseClass;   // Hạng bằng lái (ví dụ: B2, C, D)
    
    @Column(name = "license_issue_date", nullable = false)
    private LocalDate licenseIssueDate; // Ngày cấp bằng lái
    
    @Column(name = "license_expiry_date", nullable = false)
    private LocalDate licenseExpiryDate; // Ngày hết hạn bằng lái
    
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;  // Số năm kinh nghiệm lái xe
    
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status", nullable = false)
    private EmploymentStatus employmentStatus;  // Trạng thái làm việc
    
    @Column(name = "base_salary")
    private BigDecimal baseSalary; // Lương cơ bản của tài xế
    
    @Column(name = "note")
    private String note;
    
    @OneToMany(mappedBy = "driver")
    @Builder.Default
    private List<Trip> trips = new ArrayList<>();
    
    @OneToMany(mappedBy = "driver")
    @Builder.Default
    private List<SalaryReport> salaryReports = new ArrayList<>();
}