package com.transport.service.expense;


import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.transport.dto.expense.ExpenseApproveRequest;
import com.transport.dto.expense.ExpenseRequest;
import com.transport.dto.expense.ExpenseResponse;
import com.transport.dto.expense.ExpenseSearchRequest;
import com.transport.dto.expense.ExpenseUpdateRequest;
import com.transport.dto.page.PageResponse;
import com.transport.entity.domain.Expense;
import com.transport.entity.domain.ExpenseCategory;
import com.transport.entity.domain.Trip;
import com.transport.entity.domain.User;
import com.transport.enums.ExpenseStatus;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.mapper.ExpenseMapper;
import com.transport.repository.expense.ExpenseRepository;
import com.transport.service.authentication.auth.AuthenticationService;
import com.transport.util.CodeGenerator;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Transactional
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {
    ExpenseRepository expenseRepository;
    ExpenseMapper expenseMapper;
    ExpenseValidator expenseValidator;
    AuthenticationService authenticationService;
    public PageResponse<ExpenseResponse> getAll(ExpenseSearchRequest request, Pageable pageable) {
        User currentUser = authenticationService.getCurrentUser();

        if (!authenticationService.hasPermission("EXPENSE_READ")) {
            request.setDriverId(currentUser.getId());
        }
        Page<Expense> page = expenseRepository.searchExpenses(request, pageable);
        Page<ExpenseResponse> mappedPage = page.map(expenseMapper::toExpenseResponse);
        return PageResponse.from(mappedPage);
    }
    public ExpenseResponse create(ExpenseRequest expenseRequest) {
        ExpenseCategory expenseCategory = expenseValidator.validateExpenseCategory(expenseRequest.getCategoryId());
        Trip trip = expenseValidator.validateTrip(expenseRequest.getTripId());
        User user = authenticationService.getCurrentUser();

        Expense expense = expenseMapper.toCreateExpense(expenseRequest);
        expense.setCategory(expenseCategory);
        expense.setTrip(trip);
        expense.setStatus(ExpenseStatus.PENDING);
        expense.setExpenseCode(CodeGenerator.generateCode("YCCP"));
        expense.setDriverBy(user);
        expense = expenseRepository.save(expense);
        return expenseMapper.toExpenseResponse(expense);
    }
    public ExpenseResponse update(Long id, ExpenseUpdateRequest expenseRequest) {
        Expense expense = expenseValidator.validateExpense(id);
        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new AppException(ErrorCode.EXPENSE_NOT_PENDING);
        }
        expenseMapper.updateExpenseFromRequest(expenseRequest, expense);
        if (expenseRequest.getCategoryId() != null &&
            (expense.getCategory() == null ||
            !expense.getCategory().getId().equals(expenseRequest.getCategoryId()))) {

            ExpenseCategory newCategory = expenseValidator.validateExpenseCategory(expenseRequest.getCategoryId());
            expense.setCategory(newCategory);
        }

        if (expenseRequest.getTripId() != null &&
            (expense.getTrip() == null ||
            !expense.getTrip().getId().equals(expenseRequest.getTripId()))) {

            Trip newTrip = expenseValidator.validateTrip(expenseRequest.getTripId());
            expense.setTrip(newTrip);
        }
        expense.setUpdatedAt(LocalDateTime.now());
        expense = expenseRepository.save(expense);
        return expenseMapper.toExpenseResponse(expense);
    }
    public ExpenseResponse getById(Long id) {
        Expense expense = expenseValidator.validateExpense(id);
        return expenseMapper.toExpenseResponse(expense);
    }
    public void delete(Long id) {
        Expense expense = expenseValidator.validateExpense(id);
        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new AppException(ErrorCode.EXPENSE_NOT_PENDING);
        }
        expenseRepository.delete(expense);
    }
    public ExpenseResponse expenseApprove(Long id, ExpenseApproveRequest request) {
        Expense expense = expenseValidator.validateExpense(id);
        ExpenseStatus currentStatus = expense.getStatus();
        User approver = authenticationService.getCurrentUser();

        switch (currentStatus) {
            case PENDING -> {
                // Bước 1: Quản lý duyệt
                authenticationService.requirePermission("EXPENSE_APPROVE_MANAGER");
                // log.info("Quản lý duyệt" + request );
                if (Boolean.FALSE.equals(request.getIsApproved())) {
                    if (request.getReason() == null || request.getReason().isBlank()) {
                        throw new AppException(ErrorCode.CANCELLATION_REASON_REQUIRED);
                    }

                    expense.setManagerApprovedBy(approver);
                    expense.setManagerApprovedAt(LocalDateTime.now());
                    expense.setManagerNote(request.getReason());
                    expense.setStatus(ExpenseStatus.REJECTED);
                    break;
                }

                expense.setManagerApprovedBy(approver);
                expense.setManagerApprovedAt(LocalDateTime.now());
                expense.setManagerNote(request.getReason());
                expense.setStatus(ExpenseStatus.MANAGER_APPROVED);
            }

            case MANAGER_APPROVED -> {
                // Bước 2: Kế toán duyệt
                authenticationService.requirePermission("EXPENSE_APPROVE_ACCOUNTANT");

                if (Boolean.FALSE.equals(request.getIsApproved())) {
                    if (request.getReason() == null || request.getReason().isBlank()) {
                        throw new AppException(ErrorCode.CANCELLATION_REASON_REQUIRED);
                    }

                    expense.setAccountantApprovedBy(approver);
                    expense.setAccountantApprovedAt(LocalDateTime.now());
                    expense.setAccountantNote(request.getReason());
                    expense.setStatus(ExpenseStatus.REJECTED);
                    break;
                }

                expense.setAccountantApprovedBy(approver);
                expense.setAccountantApprovedAt(LocalDateTime.now());
                expense.setAccountantNote(request.getReason());
                expense.setStatus(ExpenseStatus.ACCOUNTANT_APPROVED);
            }

            case ACCOUNTANT_APPROVED -> {
                // Bước 3: Thanh toán
                authenticationService.requirePermission("EXPENSE_APPROVE_ACCOUNTANT");

                expense.setAccountantApprovedBy(approver);
                expense.setAccountantApprovedAt(LocalDateTime.now());
                expense.setAccountantNote(request.getReason());
                expense.setStatus(ExpenseStatus.PAID);
            }

            default -> throw new AppException(ErrorCode.INVALID_STATE_TRANSITION);
        }

        expense = expenseRepository.save(expense);
        return expenseMapper.toExpenseResponse(expense);
    }



}
