package com.transport.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.transport.entity.base.BaseEntity;
import com.transport.enums.ExpenseStatus;

@Entity
@Table(name = "expenses", indexes = {
    @Index(name = "idx_trip_id", columnList = "trip_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_expense_date", columnList = "expense_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "expense_code", unique = true)
    private String expenseCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategory category;
    
    @Column(name = "amount")
    private BigDecimal amount;
    
    @Column(name = "expense_date")
    private LocalDate expenseDate;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "receipt_number")
    private String receiptNumber;
    
    @Column(name = "attachment_url")
    private String attachmentUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ExpenseStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_by_id")
    private User driverBy;
    
    @Column(name = "manager_approved_at")
    private LocalDateTime managerApprovedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_approved_by_id")
    private User managerApprovedBy;
    
    @Column(name = "manager_note")
    private String managerNote;
    
    @Column(name = "accountant_approved_at")
    private LocalDateTime accountantApprovedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountant_approved_by_id")
    private User accountantApprovedBy;
    
    @Column(name = "accountant_note")
    private String accountantNote;
    
}