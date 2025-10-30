package com.transport.repository.vehicle;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.entity.domain.QVehicle;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VehicleRepositoryImpl implements VehicleRepositoryCustom {

        private final JPAQueryFactory queryFactory;

        private static final QVehicle vehicle = QVehicle.vehicle;

        @Override
        public boolean existsByLicensePlate(String licensePlate) {
                Integer fetchOne = queryFactory
                        .selectOne()
                        .from(vehicle)
                        .where(vehicle.licensePlate.equalsIgnoreCase(licensePlate))
                        .fetchFirst();
                return fetchOne != null;
        }

        @Override
        public boolean existsByVin(String vin) {
                Integer fetchOne = queryFactory
                        .selectOne()
                        .from(vehicle)
                        .where(vehicle.vin.equalsIgnoreCase(vin))
                        .fetchFirst();
                return fetchOne != null;
        }

        @Override
        public boolean existsByLicensePlateAndIdNot(String licensePlate, Long id) {
                Integer fetchOne = queryFactory
                        .selectOne()
                        .from(vehicle)
                        .where(
                                vehicle.licensePlate.equalsIgnoreCase(licensePlate)
                                        .and(vehicle.id.ne(id))
                        )
                        .fetchFirst();
                return fetchOne != null;
        }

        @Override
        public boolean existsByVinAndIdNot(String vin, Long id) {
                Integer fetchOne = queryFactory
                        .selectOne()
                        .from(vehicle)
                        .where(
                                vehicle.vin.equalsIgnoreCase(vin)
                                        .and(vehicle.id.ne(id))
                        )
                        .fetchFirst();
                return fetchOne != null;
        }
        @Override
        public BigDecimal findMaxPayloadByVehicleId(Long vehicleId) {
        return queryFactory
                .select(vehicle.vehicleType.maxPayload)
                .from(vehicle)
                .where(vehicle.id.eq(vehicleId))
                .fetchOne();
        }
}