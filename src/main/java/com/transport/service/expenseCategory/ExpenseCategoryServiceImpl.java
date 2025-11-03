package com.transport.service.expenseCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.transport.dto.expenseCategory.ExpenseCategoryRequest;
import com.transport.dto.expenseCategory.ExpenseCategoryResponse;
import com.transport.dto.expenseCategory.ExpenseCategorySearchRequest;
import com.transport.dto.page.PageResponse;
import com.transport.entity.domain.ExpenseCategory;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.mapper.ExpenseCategoryMapper;
import com.transport.repository.expenseCategory.ExpenseCategoryRepository;
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
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {
    ExpenseCategoryRepository expenseCategoryRepository;
    ExpenseCategoryMapper expenseCategoryMapper;
    ExpenseCategoryValidator expenseCategoryValidator;
    public PageResponse<ExpenseCategoryResponse> search(ExpenseCategorySearchRequest request, Pageable pageable){
        Page<ExpenseCategory> page = expenseCategoryRepository.searchExpenseCategories(request, pageable);

        return PageResponse.from(page.map(expenseCategoryMapper::toResponse));
    }
    @Override
    public ExpenseCategoryResponse getById(Long id) {
        ExpenseCategory expenseCategory = expenseCategoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VEHICLE_TYPE_NOT_FOUND));
        return expenseCategoryMapper.toResponse(expenseCategory);
    }
    @Override
    public ExpenseCategoryResponse create(ExpenseCategoryRequest request) {
        // Validate trước khi tạo
        expenseCategoryValidator.validateBeforeCreate(request);

        ExpenseCategory expenseCategory = expenseCategoryMapper.toCreateExpenseCategory(request);
        expenseCategory.setCode(CodeGenerator.generateCode("LCP"));
        expenseCategoryRepository.save(expenseCategory);

        return expenseCategoryMapper.toResponse(expenseCategory);
    }

    @Override
    public ExpenseCategoryResponse update(Long id, ExpenseCategoryRequest request) {
        ExpenseCategory expenseCategory = expenseCategoryValidator.getExpenseCategory(id);

        expenseCategoryValidator.validateBeforeUpdate(id, request);

        expenseCategoryMapper.updateExpenseCategoryFromRequest(request, expenseCategory);
        expenseCategoryRepository.save(expenseCategory);

        return expenseCategoryMapper.toResponse(expenseCategory);
    }

    @Override
    public void delete(Long id) {
        ExpenseCategory expenseCategory = expenseCategoryValidator.getExpenseCategory(id);

        expenseCategoryRepository.delete(expenseCategory);
    }
}
