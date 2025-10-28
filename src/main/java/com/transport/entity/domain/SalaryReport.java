package com.transport.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

import com.transport.entity.base.BaseEntity;

@Entity
@Table(name = "salary_reports", indexes = {
    @Index(name = "idx_driver_month", columnList = "driver_id, report_month")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryReport extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    @Column(name = "report_month", nullable = false)
    private YearMonth reportMonth; // Tháng báo cáo
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver; // Tài xế liên kết với báo cáo lương này
    
    @Column(name = "total_trips", nullable = false)
    private Integer totalTrips;  // Tổng số chuyến đi của tài xế trong tháng
    
    @Column(name = "total_distance")  // Tổng quãng đường đã di chuyển trong tháng (km)
    private BigDecimal totalDistance;
    
    @Column(name = "base_salary")
    private BigDecimal baseSalary;  // Lương cơ bản tháng của tài xế
    
    @Column(name = "trip_bonus")
    private BigDecimal tripBonus; // Tiền thưởng theo số chuyến đi
    
    @Column(name = "allowance")
    private BigDecimal allowance; // Phụ cấp thêm (nếu có)
    
    @Column(name = "deduction")
    private BigDecimal deduction; // Khoản khấu trừ (nếu có)
    
    @Column(name = "total_salary", nullable = false)
    private BigDecimal totalSalary;   // Tổng lương nhận được

    @Builder.Default
    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false; // Trạng thái đã thanh toán hay chưa
    
    @Column(name = "paid_at")
    private LocalDateTime paidAt; // Thời điểm thanh toán lương
    
    @Column(name = "note")
    private String note; // Ghi chú thêm (nếu có)
}