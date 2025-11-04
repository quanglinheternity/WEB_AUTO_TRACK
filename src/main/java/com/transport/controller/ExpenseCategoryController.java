package com.transport.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transport.dto.ApiResponse;
import com.transport.dto.expenseCategory.ExpenseCategoryRequest;
import com.transport.dto.expenseCategory.ExpenseCategoryResponse;
import com.transport.dto.expenseCategory.ExpenseCategorySearchRequest;
import com.transport.dto.page.PageResponse;
import com.transport.service.expenseCategory.ExpenseCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/loai-chi-phi")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('EXPENSE_CATEGORY')")
public class ExpenseCategoryController {
    private final ExpenseCategoryService service;

    @GetMapping
    public ApiResponse<PageResponse<ExpenseCategoryResponse>> search(
            ExpenseCategorySearchRequest request,
            @PageableDefault(page = 0, size = 10, sort = "expenseDate", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.<PageResponse<ExpenseCategoryResponse>>builder()
                .message("Lấy danh sách thành cônng")
                .data(service.search(request, pageable))
                .build();
    }

    @PostMapping
    public ApiResponse<ExpenseCategoryResponse> create(@Valid @RequestBody ExpenseCategoryRequest request) {
        return ApiResponse.<ExpenseCategoryResponse>builder()
                .message("Tạo loại chi phí thành công")
                .data(service.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ExpenseCategoryResponse> update(
            @PathVariable Long id, @Valid @RequestBody ExpenseCategoryRequest request) {
        return ApiResponse.<ExpenseCategoryResponse>builder()
                .message("Cập nhật loại chi phí thành công")
                .data(service.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.<Void>builder()
                .message("Xóa loại chi phí thành công")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ExpenseCategoryResponse> get(@PathVariable Long id) {
        return ApiResponse.<ExpenseCategoryResponse>builder()
                .message("Lấy chi tiết loại chi phí thành công")
                .data(service.getById(id))
                .build();
    }
}
