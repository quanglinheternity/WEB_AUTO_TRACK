package com.transport.dto.salary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryReportDetailResponse {
    private Long reportId;
    private Long driverId;
    private String driverName;
    private String driverCode;
    private YearMonth month;
    
    private BigDecimal baseSalary;
    private Integer totalTrips;
    private BigDecimal totalDistance;
    private BigDecimal tripBonus;
    private BigDecimal allowance;
    private BigDecimal deduction;
    private BigDecimal totalSalary;
    
    private Boolean isPaid;
    private LocalDateTime paidAt;
    private String note;
    
    // Danh sách chuyến đi trong tháng
    private List<TripSummary> trips;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripSummary {
        private Long tripId;
        private String tripCode;
        private String routeName;
        private BigDecimal distance;
        private LocalDateTime departureTime;
        private LocalDateTime actualArrivalTime;
        private String status;
    }
}