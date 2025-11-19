package com.transport.dto.expense;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

import com.transport.enums.ExpenseStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpenseResponse{
    Long id;
    String expenseCode;
    String codeTrip;
    String nameCategory;
    String driverByName;
    BigDecimal amount;
    Date expenseDate;
    String location;
    String description;
    String receiptNumber;
    String attachmentUrl;
    public ExpenseStatus status;
    String textStatus;
    String accountantApprovedByName;
    LocalDateTime accountantApprovedAt;
    String accountantNote;
    String managerApprovedByName;
    LocalDateTime managerApprovedAt;
    String managerNote;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
