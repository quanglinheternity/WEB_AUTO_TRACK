package com.transport.entity.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import com.querydsl.core.annotations.QueryEntity;
import com.transport.enums.ExpenseGroup;

import lombok.*;

@Entity
@Table(name = "expense_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@QueryEntity
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code; // Mã danh mục chi phí

    @Column(name = "name", nullable = false)
    private String name; // Tên danh mục chi phí

    @Column(name = "description")
    private String description; // Mô tả chi tiết về danh mục chi phí

    @Enumerated(EnumType.STRING)
    @Column(name = "category_group")
    private ExpenseGroup categoryGroup; // Nhóm danh mục chi phí

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // Trạng thái hoạt động của danh mục

    @OneToMany(mappedBy = "category")
    @Builder.Default
    private List<Expense> expenses = new ArrayList<>();
}
