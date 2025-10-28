package com.transport.enums;

public enum ExpenseStatus {
    PENDING,                // Chờ duyệt
    MANAGER_APPROVED,       // Quản lý đã duyệt
    ACCOUNTANT_APPROVED,    // Kế toán đã duyệt
    PAID,                   // Đã thanh toán
    REJECTED                // Từ chối
}
