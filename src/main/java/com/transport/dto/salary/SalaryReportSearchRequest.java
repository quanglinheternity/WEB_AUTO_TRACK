package com.transport.dto.salary;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SalaryReportSearchRequest {

    @DateTimeFormat(pattern = "yyyy-MM")
    private YearMonth month; // Lọc theo tháng

    private Long driverId; // Lọc theo tài xế

    private Boolean isPaid; // true = đã thanh toán, false = chưa

    private String keyword; // tìm theo tên tài xế, biển số, ghi chú, v.v.
}
