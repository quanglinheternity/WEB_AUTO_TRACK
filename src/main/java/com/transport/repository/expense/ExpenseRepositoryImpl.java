package com.transport.repository.expense;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.expense.ExpenseSearchRequest;
import com.transport.entity.domain.Expense;
import com.transport.entity.domain.QExpense;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QExpense expense = QExpense.expense;

    @Override
    public Page<Expense> searchExpenses(ExpenseSearchRequest request, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        // 🔍 Điều kiện tìm kiếm động
        if (request.getTripId() != null) {
            builder.and(expense.trip.id.eq(request.getTripId()));
        }
        if (request.getDriverId() != null) {
            builder.and(expense.driverBy.id.eq(request.getDriverId()));
        }
        if (request.getCategoryId() != null) {
            builder.and(expense.category.id.eq(request.getCategoryId()));
        }
        if (request.getStatus() != null) {
            builder.and(expense.status.eq(request.getStatus()));
        }
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String kw = "%" + request.getKeyword().trim().toLowerCase() + "%";
            builder.and(expense.expenseCode
                    .lower()
                    .like(kw)
                    .or(expense.description.lower().like(kw))
                    .or(expense.location.lower().like(kw))
                    .or(expense.receiptNumber.lower().like(kw)));
        }
        if (request.getStartDate() != null) {
            builder.and(expense.expenseDate.goe(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            builder.and(expense.expenseDate.loe(request.getEndDate()));
        }

        // ⚙️ Truy vấn dữ liệu
        List<Expense> results = queryFactory
                .selectFrom(expense)
                .where(builder)
                .orderBy(expense.expenseDate.desc(), expense.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // ⚙️ Đếm tổng số bản ghi
        long total = queryFactory
                .select(expense.count())
                .from(expense)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}
