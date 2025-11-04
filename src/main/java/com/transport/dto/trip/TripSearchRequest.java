package com.transport.dto.trip;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripSearchRequest {
    private String keyword; // Tìm theo mã chuyến, mô tả, tài xế, xe, tuyến đường
    private String status;
    private Long driverId;
    private Long vehicleId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
