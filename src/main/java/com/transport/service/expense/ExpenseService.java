package com.transport.service.expense;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.transport.dto.expense.ExpenseApproveRequest;
import com.transport.dto.expense.ExpenseRequest;
import com.transport.dto.expense.ExpenseResponse;
import com.transport.dto.expense.ExpenseSearchRequest;
import com.transport.dto.expense.ExpenseUpdateRequest;
import com.transport.dto.page.PageResponse;

public interface ExpenseService {
    PageResponse<ExpenseResponse> getAll(ExpenseSearchRequest request, Pageable pageable);

    ExpenseResponse create(ExpenseRequest expenseRequest, MultipartFile file);

    ExpenseResponse getById(Long id);

    ExpenseResponse update(Long id, ExpenseUpdateRequest expenseRequest, MultipartFile file);

    void delete(Long id);

    ExpenseResponse expenseApprove(Long id, ExpenseApproveRequest expenseApproveRequest);
}
