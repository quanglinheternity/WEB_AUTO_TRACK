package com.transport.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.transport.entity.base.BaseEntity;
import com.transport.enums.TripStatus;

@Entity
@Table(name = "trips", indexes = {
    @Index(name = "idx_trip_code", columnList = "trip_code"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_departure", columnList = "departure_time")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "trip_code", unique = true)
    private String tripCode; // Mã chuyến đi
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route; // Lộ trình chuyến đi (FK tới bảng route)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;  // Phương tiện sử dụng cho chuyến đi (FK tới bảng vehicle)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver; // Tài xế thực hiện chuyến đi (FK tới bảng driver)
    
    @Column(name = "departure_time")
    private LocalDateTime departureTime; // Thời gian khởi hành dự kiến của chuyến đi
    
    @Column(name = "estimated_arrival_time")
    private LocalDateTime estimatedArrivalTime; // Thời gian đến dự kiến
    
    @Column(name = "actual_arrival_time")
    private LocalDateTime actualArrivalTime; // Thời gian đến thực tế
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TripStatus status;  // Trạng thái chuyến đi
    
    @Column(name = "cargo_description")
    private String cargoDescription;  // Mô tả hàng hóa vận chuyển
    
    @Column(name = "cargo_weight")
    private BigDecimal cargoWeight; // Trọng lượng hàng hóa (kg hoặc tấn)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")  
    private User createdBy; // Người tạo chuyến đi

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;  // Thời gian phê duyệt chuyến đi đã hoàn thành
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedByUser; // Người phê duyệt chuyến đi đã hoàn thành
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt; // Thời điểm chuyến đi hoàn thành
    
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt; // Thời điểm chuyến đi bị hủy
    
    @Column(name = "cancellation_reason")
    private String cancellationReason;  // Lý do hủy chuyến
    
    @Column(name = "note")
    private String note;
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Expense> expenses = new ArrayList<>();
}