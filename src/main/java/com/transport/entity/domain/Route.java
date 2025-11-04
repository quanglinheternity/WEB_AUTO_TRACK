package com.transport.entity.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import com.transport.entity.base.BaseEntity;

import lombok.*;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true)
    private String code; // Mã tuyến đườn

    @Column(name = "name", nullable = false, length = 200)
    private String name; // Tên tuyến đường

    @Column(name = "origin", nullable = false, length = 200)
    private String origin; // Điểm xuất phát của tuyến đường

    @Column(name = "destination", nullable = false, length = 200)
    private String destination; // Điểm đến của tuyến đường

    @Column(name = "distance_km", precision = 10, scale = 2, nullable = false)
    private BigDecimal distanceKm; // Tổng quãng đường tính bằng km

    @Column(name = "estimated_duration_hours", precision = 5, scale = 2)
    private BigDecimal estimatedDurationHours; // Thời gian ước tính chạy tuyến (giờ)

    @Column(name = "estimated_fuel_cost", precision = 15, scale = 2)
    private BigDecimal estimatedFuelCost; // Chi phí nhiên liệu ước tính cho tuyến

    @Column(name = "description", length = 1000)
    private String description; // Mô tả chi tiết về tuyến đường

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // Trạng thái tuyến (còn hoạt động hay không)

    @OneToMany(mappedBy = "route")
    @Builder.Default
    private List<Trip> trips = new ArrayList<>();
}
