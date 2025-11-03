package com.transport.repository.route;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.route.RouteResponse;
import com.transport.dto.route.RouteSearchRequest;
import com.transport.entity.domain.QRoute;
import com.transport.entity.domain.Route;
import com.transport.mapper.RouteMapper;
import lombok.RequiredArgsConstructor;
@Repository
@RequiredArgsConstructor
public class RouteRepositoryImpl implements RouteRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final RouteMapper routeMapper;

    @Override
    public Page<RouteResponse> searchRoutes(RouteSearchRequest request, Pageable pageable) {
        QRoute route = QRoute.route;
        BooleanBuilder builder = new BooleanBuilder();

        // 🔍 Tìm kiếm theo từ khóa: code, name, origin, destination
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String kw = "%" + request.getKeyword().trim().toLowerCase() + "%";
            builder.and(
                    route.code.lower().like(kw)
                            .or(route.name.lower().like(kw))
                            .or(route.origin.lower().like(kw))
                            .or(route.destination.lower().like(kw))
            );
        }

        // 🔍 Lọc theo trạng thái hoạt động
        if (request.getIsActive() != null) {
            builder.and(route.isActive.eq(request.getIsActive()));
        } else {
            builder.and(route.isActive.isTrue());
        }

        // ⚙️ Truy vấn phân trang
        JPAQuery<Route> query = queryFactory
                .selectFrom(route)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // ⚙️ Sắp xếp động
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                switch (order.getProperty()) {
                    case "code" ->
                            query.orderBy(order.isAscending() ? route.code.asc() : route.code.desc());
                    case "name" ->
                            query.orderBy(order.isAscending() ? route.name.asc() : route.name.desc());
                    case "distanceKm" ->
                            query.orderBy(order.isAscending() ? route.distanceKm.asc() : route.distanceKm.desc());
                    case "createdAt" ->
                            query.orderBy(order.isAscending() ? route.createdAt.asc() : route.createdAt.desc());
                    default ->
                            query.orderBy(route.createdAt.desc());
                }
            });
        } else {
            query.orderBy(route.createdAt.desc());
        }

        // ⚙️ Lấy danh sách & đếm tổng
        List<Route> routes = query.fetch();
        Long total = queryFactory
                .select(route.id.count())
                .from(route)
                .where(builder)
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        // ✅ Map sang DTO phản hồi
        List<RouteResponse> responses = routes.stream()
                .map(routeMapper::toRouteResponse)
                .toList();

        return new PageImpl<>(responses, pageable, totalCount);
    }
}