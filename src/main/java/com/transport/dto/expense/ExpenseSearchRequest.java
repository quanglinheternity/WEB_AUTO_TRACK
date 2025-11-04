package com.transport.dto.expense;

import java.time.LocalDate;

import com.transport.enums.ExpenseStatus;

import lombok.Data;

@Data
public class ExpenseSearchRequest {
    private Long tripId;
    private Long driverId;
    private Long categoryId;
    private ExpenseStatus status;
    private String keyword; // tìm theo mã, mô tả, địa điểm,...
    private LocalDate startDate;
    private LocalDate endDate;
}
