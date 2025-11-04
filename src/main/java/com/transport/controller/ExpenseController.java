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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/yeu-cau-chi-phi")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ExpenseController {
    ExpenseService expenseService;

    @GetMapping
    public ApiResponse<PageResponse<ExpenseResponse>> getAllExpenses(
            ExpenseSearchRequest request,
            @PageableDefault(page = 0, size = 10, sort = "expenseDate", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.<PageResponse<ExpenseResponse>>builder()
                .message("Lấy dang sách thành công")
                .data(expenseService.getAll(request, pageable))
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ExpenseResponse> create(
            @Valid @RequestPart("data") ExpenseRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ApiResponse.<ExpenseResponse>builder()
                .message("Tạo Yêu cầu chi phí thành công")
                .data(expenseService.create(request, file))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ExpenseResponse> update(
            @PathVariable Long id,
            @Valid @RequestPart("data") ExpenseUpdateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ApiResponse.<ExpenseResponse>builder()
                .message("Cập nhật Yêu cầu chi phí thành công.")
                .data(expenseService.update(id, request, file))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ExpenseResponse> get(@PathVariable Long id) {
        return ApiResponse.<ExpenseResponse>builder()
                .message("Lấy chi tiết Yêu cầu chi phí thành công.")
                .data(expenseService.getById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Xóa Yêu cầu chi phí thành công.")
                .build();
    }

    @PutMapping("/{id}/trang-thai")
    public ApiResponse<ExpenseResponse> updateByTrangThai(
            @PathVariable Long id, @RequestBody ExpenseApproveRequest request) {
        ExpenseResponse response = expenseService.expenseApprove(id, request);
        String message =
                switch (response.status()) {
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
