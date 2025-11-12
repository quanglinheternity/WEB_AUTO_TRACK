package com.transport.repository.vehicle;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.dto.vehicle.VehicleResponse;
import com.transport.dto.vehicle.VehicleSearchRequest;
import com.transport.entity.domain.QTrip;
import com.transport.entity.domain.QVehicle;
import com.transport.entity.domain.Vehicle;
import com.transport.mapper.VehicleMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VehicleRepositoryImpl implements VehicleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QVehicle vehicle = QVehicle.vehicle;

    private final VehicleMapper vehicleMapper;
    private final QTrip trip = QTrip.trip;

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
                .where(vehicle.licensePlate.equalsIgnoreCase(licensePlate).and(vehicle.id.ne(id)))
                .fetchFirst();
        return fetchOne != null;
    }

    @Override
    public boolean existsByVinAndIdNot(String vin, Long id) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(vehicle)
                .where(vehicle.vin.equalsIgnoreCase(vin).and(vehicle.id.ne(id)))
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

    @Override
    public Page<VehicleResponse> searchVehicles(VehicleSearchRequest request, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        // 🔍 Keyword: tìm theo biển số, brand, model, vin
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String kw = "%" + request.getKeyword().trim().toLowerCase() + "%";
            builder.and(vehicle.licensePlate
                    .lower()
                    .like(kw)
                    .or(vehicle.brand.lower().like(kw))
                    .or(vehicle.model.lower().like(kw))
                    .or(vehicle.vin.lower().like(kw)));
        }

        // 🔍 Lọc theo loại xe
        if (request.getVehicleTypeId() != null) {
            builder.and(vehicle.vehicleType.id.eq(request.getVehicleTypeId()));
        }

        // 🔍 Lọc theo trạng thái xe
        if (request.getStatus() != null) {
            builder.and(vehicle.status.eq(request.getStatus()));
        }

        // 🔍 Lọc theo ngày đăng ký (registrationDate)
        if (request.getRegistrationDateFrom() != null) {
            builder.and(vehicle.registrationDate.goe(request.getRegistrationDateFrom()));
        }
        if (request.getRegistrationDateTo() != null) {
            builder.and(vehicle.registrationDate.loe(request.getRegistrationDateTo()));
        }

        // ⚙️ Tạo truy vấn chính
        JPAQuery<Tuple> query = queryFactory
                .select(vehicle, trip.count())
                .from(vehicle)
                .leftJoin(trip)
                .on(trip.vehicle.id.eq(vehicle.id))
                .where(builder)
                .groupBy(vehicle.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // ⚙️ Sắp xếp động
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                switch (order.getProperty()) {
                    case "licensePlate" -> query.orderBy(
                            order.isAscending() ? vehicle.licensePlate.asc() : vehicle.licensePlate.desc());
                    case "brand" -> query.orderBy(order.isAscending() ? vehicle.brand.asc() : vehicle.brand.desc());
                    case "model" -> query.orderBy(order.isAscending() ? vehicle.model.asc() : vehicle.model.desc());
                    case "createdAt" -> query.orderBy(
                            order.isAscending() ? vehicle.createdAt.asc() : vehicle.createdAt.desc());
                    default -> query.orderBy(vehicle.createdAt.desc());
                }
            });
        } else {
            query.orderBy(vehicle.createdAt.desc());
        }

        // ⚙️ Lấy kết quả
        List<Tuple> vehicles = query.fetch();

        // ⚙️ Đếm tổng số bản ghi
        Long total = queryFactory
                .select(vehicle.id.count())
                .from(vehicle)
                .where(builder)
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        // ✅ Map sang DTO phản hồi
        List<VehicleResponse> responses = vehicles.stream()
                .map(veh -> {
                    Vehicle v = veh.get(vehicle);
                    Long tripCount = veh.get(trip.count());
                    VehicleResponse res = vehicleMapper.toVehicleResponse(v);
                    res.setTotalTrip(tripCount != null ? tripCount : 0L);
                    return res;
                })
                .toList();

        return new PageImpl<>(responses, pageable, totalCount);
    }
}
