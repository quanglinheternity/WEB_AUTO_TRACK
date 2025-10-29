package com.transport.repository.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.user.UserResponse;
import com.transport.entity.domain.QUser;
import com.transport.entity.domain.User;
import com.transport.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final UserMapper userMapper;
    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;

    @Override
    public boolean existsByUsername(String username) {
        return queryFactory
                .selectOne()
                .from(user)
                .where(user.username.eq(username))
                .fetchFirst() != null;
    }

    @Override
    public boolean existsByUsernameAndIdNot(String username, Long id) {
        return queryFactory
                .selectOne()
                .from(user)
                .where(
                    user.username.eq(username)
                        .and(user.id.ne(id))
                )
                .fetchFirst() != null;
    }
    @Override
    public Page<UserResponse> searchUsers(String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // Điều kiện tìm kiếm
        if (keyword != null && !keyword.trim().isEmpty()) {
            BooleanExpression usernameContains = user.username.containsIgnoreCase(keyword);
            BooleanExpression fullNameContains = user.fullName.containsIgnoreCase(keyword);
            builder.and(usernameContains.or(fullNameContains));
        }

        // Truy vấn dữ liệu phân trang
        JPAQuery<User> query = queryFactory
                .selectFrom(user)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // Áp dụng sắp xếp
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                switch (order.getProperty()) {
                    case "username" ->
                            query.orderBy(order.isAscending() ? user.username.asc() : user.username.desc());
                    case "createdAt" ->
                            query.orderBy(order.isAscending() ? user.createdAt.asc() : user.createdAt.desc());
                    case "fullName" ->
                            query.orderBy(order.isAscending() ? user.fullName.asc() : user.fullName.desc());
                }
            });
        } else {
            query.orderBy(user.createdAt.desc()); // Mặc định sắp xếp mới nhất
        }

        // Lấy danh sách
        List<User> users = query.fetch();

        // ✅ Đếm tổng số bản ghi (QueryDSL 5.x, không deprecated)
        Long total = queryFactory
                .select(user.id.count())
                .from(user)
                .where(builder)
                .fetchOne();

        // Tránh NullPointer nếu không có kết quả
        long totalCount = total != null ? total : 0;

        // Map sang UserResponse (giả sử bạn có mapper)
        List<UserResponse> responses = users.stream()
                .map(userMapper::toResponse)
                .toList();

        return new PageImpl<>(responses, pageable, totalCount);
    }
}
