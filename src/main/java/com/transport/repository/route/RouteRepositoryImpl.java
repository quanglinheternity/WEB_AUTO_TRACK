package com.transport.repository.route;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.route.RouteBySalary;
import com.transport.dto.route.RouteResponse;
import com.transport.dto.route.RouteSearchRequest;
import com.transport.entity.domain.QRoute;
import com.transport.entity.domain.QTrip;
import com.transport.entity.domain.Route;
import com.transport.mapper.RouteMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RouteRepositoryImpl implements RouteRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final RouteMapper routeMapper;
    private final QRoute route = QRoute.route;
    private final BooleanBuilder builder = new BooleanBuilder();
    private final QTrip trip = QTrip.trip;


    @Override
    public Page<RouteResponse> searchRoutes(RouteSearchRequest request, Pageable pageable) {

        // 🔍 Tìm kiếm theo từ khóa: code, name, origin, destination
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String kw = "%" + request.getKeyword().trim().toLowerCase() + "%";
            builder.and(route.code
                    .lower()
                    .like(kw)
                    .or(route.name.lower().like(kw))
                    .or(route.origin.lower().like(kw))
                    .or(route.destination.lower().like(kw)));
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
                    case "code" -> query.orderBy(order.isAscending() ? route.code.asc() : route.code.desc());
                    case "name" -> query.orderBy(order.isAscending() ? route.name.asc() : route.name.desc());
                    case "distanceKm" -> query.orderBy(
                            order.isAscending() ? route.distanceKm.asc() : route.distanceKm.desc());
                    case "createdAt" -> query.orderBy(
                            order.isAscending() ? route.createdAt.asc() : route.createdAt.desc());
                    default -> query.orderBy(route.createdAt.desc());
                }
            });
        } else {
            query.orderBy(route.createdAt.desc());
        }

        // ⚙️ Lấy danh sách & đếm tổng
        List<Route> routes = query.fetch();
        Long total =
                queryFactory.select(route.id.count()).from(route).where(builder).fetchOne();

        long totalCount = (total != null) ? total : 0L;

        // ✅ Map sang DTO phản hồi
        List<RouteResponse> responses =
                routes.stream().map(routeMapper::toRouteResponse).toList();

        return new PageImpl<>(responses, pageable, totalCount);
    }
    public List<RouteBySalary> findRoutesByDriverAndMonth(Long driverId, YearMonth month) {
            LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);

    return queryFactory
            .select(
                    com.querydsl.core.types.Projections.constructor(
                            RouteBySalary.class,
                            route.name,
                            trip.countDistinct().as("totalTrips"),
                            route.distanceKm.sum().coalesce(BigDecimal.ZERO).as("totalDistance"),
                            com.querydsl.core.types.ConstantImpl.create(BigDecimal.ZERO)
                    )
            )
            .from(trip)
            .join(trip.route, route)
            .where(
                    trip.driver.id.eq(driverId)
                            .and(trip.departureTime.between(startOfMonth, endOfMonth))
            )
            .groupBy(route.id, route.name)
            .fetch();
}
}
