package com.transport.dto.vehicleType;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTypeResponse {

    private Long id;
    private String code;
    private String name;
    private BigDecimal maxPayload;
    private String description;
    private Boolean isActive;

    // Nếu muốn trả về danh sách xe thuộc loại này (tùy chọn)
    private List<VehicleSummary> vehicles;

    // DTO con: tóm tắt xe
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleSummary {
        private Long id;
        private String licensePlate;
        private String status;
    }
}
