package com.transport.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.user.UserResponse;
import com.transport.dto.user.UserSearchRequest;
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
                        .fetchFirst()
                != null;
    }

    @Override
    public boolean existsByUsernameAndIdNot(String username, Long id) {
        return queryFactory
                        .selectOne()
                        .from(user)
                        .where(user.username.eq(username).and(user.id.ne(id)))
                        .fetchFirst()
                != null;
    }

    @Override
    public Optional<User> findByName(String name) {
        return Optional.ofNullable(
                queryFactory.selectFrom(user).where(user.username.eq(name)).fetchFirst());
    }

    @Override
    public Page<UserResponse> searchUsers(UserSearchRequest request, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        // 🔍 Keyword: tìm theo username, fullName, phone, idNumber
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String kw = "%" + request.getKeyword().trim().toLowerCase() + "%";
            builder.and(user.username
                    .lower()
                    .like(kw)
                    .or(user.fullName.lower().like(kw))
                    .or(user.phone.lower().like(kw))
                    .or(user.idNumber.lower().like(kw)));
        }

        // 🔍 Lọc theo role (nếu có)
        if (request.getRole() != null) {
            builder.and(user.roles.any().roleName.eq(request.getRole()));
        }

        // 🔍 Lọc theo trạng thái hoạt động
        if (request.getIsActive() != null) {
            builder.and(user.isActive.eq(request.getIsActive()));
        } else {
            builder.and(user.isActive.isTrue()); // mặc định chỉ lấy active
        }

        // ⚙️ Truy vấn phân trang
        JPAQuery<User> query = queryFactory
                .selectFrom(user)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // ⚙️ Sắp xếp động
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                switch (order.getProperty()) {
                    case "username" -> query.orderBy(order.isAscending() ? user.username.asc() : user.username.desc());
                    case "fullName" -> query.orderBy(order.isAscending() ? user.fullName.asc() : user.fullName.desc());
                    case "createdAt" -> query.orderBy(
                            order.isAscending() ? user.createdAt.asc() : user.createdAt.desc());
                    default -> query.orderBy(user.createdAt.desc());
                }
            });
        } else {
            query.orderBy(user.createdAt.desc());
        }

        // ⚙️ Lấy kết quả & đếm tổng
        List<User> users = query.fetch();

        Long total =
                queryFactory.select(user.id.count()).from(user).where(builder).fetchOne();

        long totalCount = (total != null) ? total : 0L;

        //  Map sang DTO phản hồi
        List<UserResponse> responses =
                users.stream().map(userMapper::toResponse).toList();

        return new PageImpl<>(responses, pageable, totalCount);
    }

    @Override
    public User findByUsername(String username) {
        return queryFactory.selectFrom(user).where(user.username.eq(username)).fetchOne();
    }
}
