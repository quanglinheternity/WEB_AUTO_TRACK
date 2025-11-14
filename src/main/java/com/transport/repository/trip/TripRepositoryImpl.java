package com.transport.repository.trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.expense.ExpenseByExpenseCategoryReponse;
import com.transport.dto.expense.ExpenseByTripResponse;
import com.transport.dto.expense.ExpenseReportResponse;
import com.transport.dto.trip.TripByVehicleReponse;
import com.transport.dto.trip.TripReport;
import com.transport.dto.trip.TripSearchRequest;
import com.transport.entity.domain.QDriver;
import com.transport.entity.domain.QExpense;
import com.transport.entity.domain.QRoute;
import com.transport.entity.domain.QTrip;
import com.transport.entity.domain.QVehicle;
import com.transport.entity.domain.Trip;
import com.transport.enums.TripStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TripRepositoryImpl implements TripRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QTrip trip = QTrip.trip;
    private final BooleanBuilder builder = new BooleanBuilder();
    private final QVehicle vehicle = QVehicle.vehicle;
    private final QDriver driver = QDriver.driver;
    private final QRoute route = QRoute.route;
    private final QExpense expense = QExpense.expense;

    @Override
    public long countOverlappingTripsByVehicle(
            Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime) {
        return countOverlappingTrips(trip.vehicle.id.eq(vehicleId), departureTime, estimatedArrivalTime, null);
    }

    @Override
    public long countOverlappingTripsByDriver(
            Long driverId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime) {
        return countOverlappingTrips(trip.driver.id.eq(driverId), departureTime, estimatedArrivalTime, null);
    }

    @Override
    public long countOverlappingTripsByVehicleExcluding(
            Long vehicleId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, Long excludeTripId) {
        return countOverlappingTrips(trip.vehicle.id.eq(vehicleId), departureTime, estimatedArrivalTime, excludeTripId);
    }

    @Override
    public long countOverlappingTripsByDriverExcluding(
            Long driverId, LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, Long excludeTripId) {
        return countOverlappingTrips(trip.driver.id.eq(driverId), departureTime, estimatedArrivalTime, excludeTripId);
    }

    private long countOverlappingTrips(
            BooleanExpression fieldCondition,
            LocalDateTime departureTime,
            LocalDateTime estimatedArrivalTime,
            Long excludeTripId) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(fieldCondition)
                .and(trip.status.notIn(TripStatus.CANCELLED, TripStatus.ARRIVED))
                .and(trip.departureTime.loe(estimatedArrivalTime).and(trip.estimatedArrivalTime.goe(departureTime)));

        if (excludeTripId != null) {
            builder.and(trip.id.ne(excludeTripId));
        }

        Long result =
                queryFactory.select(Wildcard.count).from(trip).where(builder).fetchOne();

        return result != null ? result : 0L;
    }

    @Override
    public Page<Trip> searchTrips(TripSearchRequest request, Pageable pageable) {
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

        var query = queryFactory
                .selectFrom(trip)
                .leftJoin(trip.driver)
                .fetchJoin()
                .leftJoin(trip.vehicle)
                .fetchJoin()
                .leftJoin(trip.route)
                .fetchJoin()
                .where(builder);

        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                switch (order.getProperty()) {
                    case "tripCode" -> query.orderBy(order.isAscending() ? trip.tripCode.asc() : trip.tripCode.desc());
                    case "departureTime" -> query.orderBy(
                            order.isAscending() ? trip.departureTime.asc() : trip.departureTime.desc());
                    case "status" -> query.orderBy(order.isAscending() ? trip.status.asc() : trip.status.desc());
                    default -> query.orderBy(trip.createdAt.desc());
                }
            });
        } else {
            query.orderBy(trip.createdAt.desc());
        }

        long total = Optional.ofNullable(queryFactory
                        .select(trip.count())
                        .from(trip)
                        .where(builder)
                        .fetchOne())
                .orElse(0L);
        List<Trip> results =
                query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public long countTripsByDriverAndMonth(Long driverId, YearMonth month) {

        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(23, 59, 59);

        Long count = queryFactory
                .select(trip.count())
                .from(trip)
                .where(
                        trip.driver.id.eq(driverId),
                        trip.status.ne(TripStatus.ARRIVED),
                        trip.approvalStatus.eq(true),
                        trip.departureTime.between(start, end))
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
                .where(trip.driver.id.eq(driverId), trip.departureTime.between(start, end))
                .fetchOne();

        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<Trip> findCompletedTripsByDriverAndMonth(Long driverId, YearMonth month) {

        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(23, 59, 59);

        return queryFactory
                .selectFrom(trip)
                .where(
                        trip.driver.id.eq(driverId),
                        // trip.status.eq(TripStatus.ARRIVED),
                        // trip.approvalStatus.isTrue(),
                        trip.departureTime.between(start, end))
                .orderBy(trip.departureTime.asc())
                .fetch();
    }

    @Override
    public boolean isVehicleUsedInTrip(Long vehicleId) {

        Long count = queryFactory
                .select(trip.id.count())
                .from(trip)
                .where(trip.vehicle.id.eq(vehicleId))
                .fetchOne();

        return count != null && count > 0;
    }

    @Override
    public TripReport findReportTripByVehicle(Long vehicleId, YearMonth month) {

        if (vehicleId != null) {
            builder.and(trip.vehicle.id.eq(vehicleId));
        }
        if (month != null) {
            LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
            builder.and(trip.departureTime.between(startOfMonth, endOfMonth));
        }
        List<Tuple> trips = queryFactory
                .select(
                        trip.vehicle.licensePlate,
                        trip.vehicle.model,
                        trip.driver.driverCode,
                        trip.driver.user.fullName,
                        trip.route.origin,
                        trip.route.destination,
                        trip.departureTime.min(),
                        trip.note,
                        trip.count())
                .from(trip)
                .leftJoin(trip.vehicle, vehicle)
                .leftJoin(trip.driver, driver)
                .leftJoin(trip.route, route)
                .where(builder)
                .groupBy(
                        trip.vehicle.id,
                        trip.vehicle.licensePlate,
                        trip.vehicle.model,
                        trip.driver.driverCode,
                        trip.driver.user.fullName,
                        trip.route.origin,
                        trip.route.destination,
                        trip.note)
                .orderBy(trip.departureTime.min().asc())
                .fetch();

        List<TripByVehicleReponse> responses = trips.stream()
                .map(tuple -> new TripByVehicleReponse(
                        tuple.get(trip.driver.driverCode),
                        tuple.get(trip.driver.user.fullName),
                        tuple.get(trip.vehicle.licensePlate),
                        tuple.get(trip.vehicle.model),
                        tuple.get(trip.departureTime.min()),
                        tuple.get(trip.route.origin),
                        tuple.get(trip.route.destination),
                        tuple.get(trip.count()),
                        tuple.get(trip.note)))
                .toList();
        long totalTrips =
                responses.stream().mapToLong(TripByVehicleReponse::totalTrip).sum();
        return new TripReport(responses, totalTrips);
    }

    public ExpenseReportResponse findTripReportByExpense(YearMonth month) {
        if (month != null) {
            LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
            builder.and(trip.departureTime.between(startOfMonth, endOfMonth));
        }
        List<Tuple> tuples = queryFactory
                .select(
                        trip.driver.driverCode,
                        trip.driver.user.fullName,
                        trip.vehicle.licensePlate,
                        trip.vehicle.model,
                        trip.route.origin,
                        expense.category.name,
                        expense.amount,
                        trip.route.estimatedFuelCost)
                .from(trip)
                .leftJoin(trip.expenses, expense)
                .where(builder)
                .fetch();
        Map<String, List<Tuple>> grouped = tuples.stream().collect(Collectors.groupingBy(t -> {
            String code = t.get(trip.driver.driverCode);
            return code != null ? code : "UNKNOWN"; // fallback tránh null
        }));
        List<ExpenseByTripResponse> expenses = new ArrayList<>();
        for (List<Tuple> group : grouped.values()) {
            Tuple first = group.getFirst();
            List<ExpenseByExpenseCategoryReponse> categories = group.stream()
                    .filter(t -> t.get(expense.category.name) != null)
                    .map(t -> new ExpenseByExpenseCategoryReponse(t.get(expense.category.name), t.get(expense.amount)))
                    .toList();
            BigDecimal totalExpense = categories.stream()
                    .map(ExpenseByExpenseCategoryReponse::amount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal estimatedFuelCost = first.get(trip.route.estimatedFuelCost) != null
                    ? first.get(trip.route.estimatedFuelCost)
                    : BigDecimal.ZERO;
            BigDecimal safeEstimated = estimatedFuelCost != null ? estimatedFuelCost : BigDecimal.ZERO;
            BigDecimal safeTotal = totalExpense != null ? totalExpense : BigDecimal.ZERO;

            BigDecimal residual = safeEstimated.subtract(safeTotal);
            ExpenseByTripResponse tripResponse = new ExpenseByTripResponse(
                    first.get(trip.driver.driverCode),
                    first.get(trip.driver.user.fullName),
                    first.get(trip.vehicle.licensePlate),
                    first.get(trip.vehicle.model),
                    estimatedFuelCost,
                    categories,
                    residual);
            expenses.add(tripResponse);
        }

        BigDecimal totalEstimatedFuelCost = expenses.stream()
                .map(ExpenseByTripResponse::estimatedFuelCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalResidual = expenses.stream()
                .map(ExpenseByTripResponse::residual)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExpenseReportResponse(expenses, totalEstimatedFuelCost, totalResidual);
    }
}
