package com.transport.repository.expenseCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.transport.entity.domain.ExpenseCategory;
import com.transport.entity.domain.QExpenseCategory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExpenseCategoryRepositoryImpl implements ExpenseCategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QExpenseCategory qExpenseCategory = QExpenseCategory.expenseCategory;

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        Integer count = queryFactory
                .selectOne()
                .from(qExpenseCategory)
                .where(qExpenseCategory.name.eq(name)
                        .and(qExpenseCategory.id.ne(id)))
                .fetchFirst();

        return count != null;
    }

    @Override
    public boolean existsByName(String name) {
        Integer count = queryFactory
                .selectOne()
                .from(qExpenseCategory)
                .where(qExpenseCategory.name.eq(name))
                .fetchFirst();

        return count != null;
    }

    @Override
    public Page<ExpenseCategory> search(String keyword, Pageable pageable) {
        BooleanExpression predicate = qExpenseCategory.isActive.isTrue();

        if (keyword != null && !keyword.trim().isEmpty()) {
            String likePattern = "%" + keyword.trim().toLowerCase() + "%";
            predicate = predicate.and(
                    qExpenseCategory.code.lower().like(likePattern)
                            .or(qExpenseCategory.name.lower().like(likePattern))
            );
        }

        // Count query
        Long total = queryFactory
                    .select(qExpenseCategory.id.count())
                    .from(qExpenseCategory)
                    .where(predicate)
                    .fetchOne();

        // Data query
        var query = queryFactory
                .selectFrom(qExpenseCategory)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // Apply sorting
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                var path = switch (order.getProperty()) {
                    case "code" -> qExpenseCategory.code;
                    case "name" -> qExpenseCategory.name;
                    // case "description" -> qVehicleType.description;
                    default -> qExpenseCategory.id;
                };
                query.orderBy(order.isAscending() ? path.asc() : path.desc());
            });
        } else {
            query.orderBy(qExpenseCategory.id.desc());
        }

        var content = query.fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
