package com.transport.repository.expenseCategory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.expenseCategory.ExpenseCategorySearchRequest;
import com.transport.entity.domain.ExpenseCategory;
import com.transport.entity.domain.QExpenseCategory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExpenseCategoryRepositoryImpl implements ExpenseCategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QExpenseCategory category = QExpenseCategory.expenseCategory;

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        Integer count = queryFactory
                .selectOne()
                .from(category)
                .where(category.name.eq(name).and(category.id.ne(id)))
                .fetchFirst();

        return count != null;
    }

    @Override
    public boolean existsByName(String name) {
        Integer count = queryFactory
                .selectOne()
                .from(category)
                .where(category.name.eq(name))
                .fetchFirst();

        return count != null;
    }

    @Override
    public Page<ExpenseCategory> searchExpenseCategories(ExpenseCategorySearchRequest request, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        // 🔍 Điều kiện tìm kiếm động
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String kw = "%" + request.getKeyword().trim().toLowerCase() + "%";
            builder.and(category.code
                    .lower()
                    .like(kw)
                    .or(category.name.lower().like(kw))
                    .or(category.description.lower().like(kw)));
        }

        if (request.getGroup() != null) {
            builder.and(category.categoryGroup.eq(request.getGroup()));
        }

        if (request.getIsActive() != null) {
            builder.and(category.isActive.eq(request.getIsActive()));
        } else {
            builder.and(category.isActive.isTrue()); // mặc định chỉ lấy active
        }

        // ⚙️ Truy vấn dữ liệu
        List<ExpenseCategory> results = queryFactory
                .selectFrom(category)
                .where(builder)
                .orderBy(category.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // ⚙️ Đếm tổng số bản ghi
        long total = queryFactory
                .select(category.count())
                .from(category)
                .where(builder)
                .fetchOne();

        if (total == 0L && !results.isEmpty()) {
            total = results.size(); // fallback an toàn
        }

        return new PageImpl<>(results, pageable, total);
    }
}
