package com.transport.repository.trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.trip.TripSearchRequest;
import com.transport.entity.domain.QRoute;
import com.transport.entity.domain.QTrip;
import com.transport.entity.domain.Trip;
import com.transport.enums.TripStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TripRepositoryImpl implements TripRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QTrip trip = QTrip.trip;

    @Override
    public long countOverlappingTripsByVehicle(Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime) {
        return countOverlappingTrips(trip.vehicle.id.eq(vehicleId), departureTime, estimatedArrivalTime, null);
    }

    @Override
    public long countOverlappingTripsByDriver(Long driverId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime) {
        return countOverlappingTrips(trip.driver.id.eq(driverId), departureTime, estimatedArrivalTime, null);
    }

    @Override
    public long countOverlappingTripsByVehicleExcluding(Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, Long excludeTripId) {
        return countOverlappingTrips(trip.vehicle.id.eq(vehicleId), departureTime, estimatedArrivalTime, excludeTripId);
    }

    @Override
    public long countOverlappingTripsByDriverExcluding(Long driverId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, Long excludeTripId) {
        return countOverlappingTrips(trip.driver.id.eq(driverId), departureTime, estimatedArrivalTime, excludeTripId);
    }

    private long countOverlappingTrips(
            BooleanExpression fieldCondition,
            LocalDateTime departureTime,
            LocalDateTime estimatedArrivalTime,
            Long excludeTripId
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(fieldCondition)
            .and(trip.status.notIn(TripStatus.CANCELLED, TripStatus.ARRIVED))
            .and(trip.departureTime.loe(estimatedArrivalTime)
                    .and(trip.estimatedArrivalTime.goe(departureTime)));

        if (excludeTripId != null) {
            builder.and(trip.id.ne(excludeTripId));
        }

        Long result = queryFactory
                .select(Wildcard.count)
                .from(trip)
                .where(builder)
                .fetchOne();

        return result != null ? result : 0L;
    }
    @Override
    public Page<Trip> searchTrips(TripSearchRequest request, Pageable pageable) {
        QTrip trip = QTrip.trip;
        BooleanBuilder builder = new BooleanBuilder();

        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            builder.or(trip.tripCode.containsIgnoreCase(request.getKeyword()))
                   .or(trip.cargoDescription.containsIgnoreCase(request.getKeyword()))
                   .or(trip.driver.user.fullName.containsIgnoreCase(request.getKeyword()))
                   .or(trip.vehicle.licensePlate.containsIgnoreCase(request.getKeyword()))
                   .or(trip.route.name.containsIgnoreCase(request.getKeyword()));
        }

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            builder.and(trip.status.stringValue().eq(request.getStatus()));
        }

        if (request.getVehicleId() != null) {
            builder.and(trip.vehicle.id.eq(request.getVehicleId()));
        }

        if (request.getDriverId() != null) {
            builder.and(trip.driver.id.eq(request.getDriverId()));
        }

        if (request.getFromDate() != null) {
            builder.and(trip.departureTime.goe(request.getFromDate()));
        }
        if (request.getToDate() != null) {
            builder.and(trip.departureTime.loe(request.getToDate()));
        }

        var query = queryFactory.selectFrom(trip)
                .leftJoin(trip.driver).fetchJoin()
                .leftJoin(trip.vehicle).fetchJoin()
                .leftJoin(trip.route).fetchJoin()
                .where(builder);

        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                switch (order.getProperty()) {
                    case "tripCode" ->
                            query.orderBy(order.isAscending() ? trip.tripCode.asc() : trip.tripCode.desc());
                    case "departureTime" ->
                            query.orderBy(order.isAscending() ? trip.departureTime.asc() : trip.departureTime.desc());
                    case "status" ->
                            query.orderBy(order.isAscending() ? trip.status.asc() : trip.status.desc());
                    default ->
                            query.orderBy(trip.createdAt.desc());
                }
            });
        } else {
            query.orderBy(trip.createdAt.desc());
        }

        long total =  queryFactory
                        .select(trip.count())
                        .from(trip)
                        .where(builder)
                        .fetchOne();
        List<Trip> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }
    @Override
    public long countTripsByDriverAndMonth(Long driverId, YearMonth month) {

        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(23, 59, 59);

        Long count = queryFactory.select(trip.count())
                .from(trip)
                .where(
                        trip.driver.id.eq(driverId),
                        trip.status.ne(TripStatus.ARRIVED),
                        trip.approvalStatus.eq(true),
                        trip.departureTime.between(start, end)
                )
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public BigDecimal sumDistanceByDriverAndMonth(Long driverId, YearMonth month) {
        QRoute route = QRoute.route;

        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal total = queryFactory
                .select(route.distanceKm.sum().coalesce(BigDecimal.ZERO))
                .from(trip)
                .leftJoin(trip.route, route)
                .where(
                        trip.driver.id.eq(driverId),
                        trip.departureTime.between(start, end)
                )
                .fetchOne();

        return total != null ? total : BigDecimal.ZERO;
    }
    @Override
    public List<Trip> findCompletedTripsByDriverAndMonth(Long driverId, YearMonth month) {

        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(23, 59, 59);

        return queryFactory.selectFrom(trip)
                .where(
                        trip.driver.id.eq(driverId),
                        // trip.status.eq(TripStatus.ARRIVED),
                        // trip.approvalStatus.isTrue(),
                        trip.departureTime.between(start, end)
                )
                .orderBy(trip.departureTime.asc())
                .fetch();
    }

}
