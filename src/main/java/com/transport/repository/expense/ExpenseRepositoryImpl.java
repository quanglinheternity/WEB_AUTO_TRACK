package com.transport.repository.expense;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
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
        JPAQuery<Expense> query = queryFactory
                .selectFrom(expense)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // ⚙️ Sắp xếp động từ pageable
        if (pageable.getSort().isSorted()) {
            List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                switch (order.getProperty()) {
                    case "id" -> orderSpecifiers.add(order.isAscending() ? expense.id.asc() : expense.id.desc());
                    case "status" -> orderSpecifiers.add(
                            order.isAscending() ? expense.status.asc() : expense.status.desc());
                    case "createdAt" -> orderSpecifiers.add(
                            order.isAscending() ? expense.createdAt.asc() : expense.createdAt.desc());
                    default -> orderSpecifiers.add(expense.createdAt.desc());
                }
            });
            query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
        } else {
            // fallback mặc định
            query.orderBy(expense.createdAt.desc());
        }

        // ⚙️ Đếm tổng số bản ghi
        Long count = queryFactory
                .select(expense.count())
                .from(expense)
                .where(builder)
                .fetchOne();

        long total = (count != null) ? count : 0L;
        return new PageImpl<>(query.fetch(), pageable, total);
    }
}
