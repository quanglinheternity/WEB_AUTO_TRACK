package com.transport.repository.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transport.entity.domain.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> , ExpenseRepositoryCustom {

}
