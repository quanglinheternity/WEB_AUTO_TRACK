package com.transport.controller;

import jakarta.validation.Valid;

import java.time.YearMonth;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transport.dto.ApiResponse;
import com.transport.dto.expenseCategory.ExpenseByExpenseCategory;
import com.transport.dto.expenseCategory.ExpenseCategoryRequest;
import com.transport.dto.expenseCategory.ExpenseCategoryResponse;
import com.transport.dto.expenseCategory.ExpenseCategorySearchRequest;
import com.transport.dto.page.PageResponse;
import com.transport.service.expenseCategory.ExpenseCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/expense-category")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('EXPENSE_CATEGORY')")
@Tag(name = "Expense Category", description = "APIs for managing Expense Category")
public class ExpenseCategoryController {
    private final ExpenseCategoryService service;

    @Operation(summary = "Get list of expense categories")
    @GetMapping("/list")
    public ApiResponse<PageResponse<ExpenseCategoryResponse>> search(
            ExpenseCategorySearchRequest request,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.<PageResponse<ExpenseCategoryResponse>>builder()
                .message("Lấy danh sách thành cônng")
                .data(service.search(request, pageable))
                .build();
    }

    @Operation(summary = "Create a new expense category")
    @PostMapping("/create")
    public ApiResponse<ExpenseCategoryResponse> create(@Valid @RequestBody ExpenseCategoryRequest request) {
        return ApiResponse.<ExpenseCategoryResponse>builder()
                .message("Tạo loại chi phí thành công")
                .data(service.create(request))
                .build();
    }

    @Operation(summary = "Update an existing expense category")
    @PutMapping("/{id}/update")
    public ApiResponse<ExpenseCategoryResponse> update(
            @PathVariable Long id, @Valid @RequestBody ExpenseCategoryRequest request) {
        return ApiResponse.<ExpenseCategoryResponse>builder()
                .message("Cập nhật loại chi phí thành công")
                .data(service.update(id, request))
                .build();
    }

    @Operation(summary = "Delete an expense category")
    @DeleteMapping("/{id}/delete")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.<Void>builder()
                .message("Xóa loại chi phí thành công")
                .build();
    }

    @Operation(summary = "Get expense category details")
    @GetMapping("/{id}/detail")
    public ApiResponse<ExpenseCategoryResponse> get(@PathVariable Long id) {
        return ApiResponse.<ExpenseCategoryResponse>builder()
                .message("Lấy chi tiết loại chi phí thành công")
                .data(service.getById(id))
                .build();
    }
    @GetMapping("/{driverId}/expense")
    public ApiResponse<ExpenseByExpenseCategory> getExpense(@PathVariable Long driverId, @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        return ApiResponse.<ExpenseByExpenseCategory>builder()
                .message("Lấy danh sách chi phí")
                .data(service.getExpenseByExpenseCategory(driverId, yearMonth))
                .build();
    }
}
