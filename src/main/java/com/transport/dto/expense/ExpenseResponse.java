package com.transport.dto.expense;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

import com.transport.enums.ExpenseStatus;
public record ExpenseResponse(
    Long id,
    String expenseCode,

    String codeTrip, // lấy thêm từ LichTrinh nếu cần

    String nameCategory,

    String driverByName,

    BigDecimal amount,
    Date expenseDate,
    String location,
    String description,
    String receiptNumber,
    String attachmentUrl,
    ExpenseStatus status,
    String textStatus,

    String accountantApprovedByName,
    LocalDateTime accountantApprovedAt,
    String accountantNote,

    String managerApprovedByName,
    LocalDateTime managerApprovedAt,
    String managerNote,

    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
