package com.transport.repository.trip;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.entity.domain.QTrip;
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

}
