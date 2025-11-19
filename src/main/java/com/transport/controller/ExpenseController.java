package com.transport.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.transport.dto.ApiResponse;
import com.transport.dto.expense.ExpenseApproveRequest;
import com.transport.dto.expense.ExpenseRequest;
import com.transport.dto.expense.ExpenseResponse;
import com.transport.dto.expense.ExpenseSearchRequest;
import com.transport.dto.expense.ExpenseUpdateRequest;
import com.transport.dto.page.PageResponse;
import com.transport.service.expense.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Expense", description = "APIs for managing Expense")
public class ExpenseController {
    ExpenseService expenseService;

    @Operation(summary = "Get list of expenses")
    @GetMapping("/list")
    public ApiResponse<PageResponse<ExpenseResponse>> getAllExpenses(
            ExpenseSearchRequest request,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.<PageResponse<ExpenseResponse>>builder()
                .message("Lấy dang sách thành công")
                .data(expenseService.getAll(request, pageable))
                .build();
    }

    @Operation(summary = "Create a new expense request")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ExpenseResponse> createExpense(
            @Valid @RequestPart("data") ExpenseRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ApiResponse.<ExpenseResponse>builder()
                .message("Tạo Yêu cầu chi phí thành công")
                .data(expenseService.create(request, file))
                .build();
    }

    @Operation(summary = "Update an existing expense request")
    @PutMapping(value = "/{id}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestPart("data") ExpenseUpdateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ApiResponse.<ExpenseResponse>builder()
                .message("Cập nhật Yêu cầu chi phí thành công.")
                .data(expenseService.update(id, request, file))
                .build();
    }

    @Operation(summary = "Get expense details by ID")
    @GetMapping("/{id}/detail")
    public ApiResponse<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        return ApiResponse.<ExpenseResponse>builder()
                .message("Lấy chi tiết Yêu cầu chi phí thành công.")
                .data(expenseService.getById(id))
                .build();
    }

    @Operation(summary = "Delete expense by ID")
    @DeleteMapping("/{id}/delete")
    public ApiResponse<Void> deleteExpenseById(@PathVariable Long id) {
        expenseService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Xóa Yêu cầu chi phí thành công.")
                .build();
    }

    @Operation(summary = "Update expense approval status")
    @PutMapping("/{id}/status")
    public ApiResponse<ExpenseResponse> updateByStatus(
            @PathVariable Long id, @RequestBody ExpenseApproveRequest request) {
        ExpenseResponse response = expenseService.expenseApprove(id, request);
        String message =
                switch (response.getStatus()) {
                    case MANAGER_APPROVED -> "Yêu cầu chi phí đã được duyệt thành công";
                    case ACCOUNTANT_APPROVED -> "Yêu cầu chi phí đã được duyệt tài chính";
                    case PAID -> "Yêu cầu chi phí được thanh toán";
                    case REJECTED -> "Yêu cầu chi phí đã bị từ chối";
                    default -> "Cập nhật trạng thái thành công";
                };
        return ApiResponse.<ExpenseResponse>builder()
                .code(1000)
                .message(message)
                .data(response)
                .build();
    }
}
