package com.transport.repository.expenseCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transport.entity.domain.ExpenseCategory;

@Repository
public interface ExpenseCategoryRepository
        extends JpaRepository<ExpenseCategory, Long>, ExpenseCategoryRepositoryCustom {}
