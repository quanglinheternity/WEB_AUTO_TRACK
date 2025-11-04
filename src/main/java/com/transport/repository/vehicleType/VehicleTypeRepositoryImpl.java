package com.transport.repository.vehicleType;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.entity.domain.QVehicleType;
import com.transport.entity.domain.VehicleType;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VehicleTypeRepositoryImpl implements VehicleTypeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QVehicleType qVehicleType = QVehicleType.vehicleType;

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        Integer count = queryFactory
                .selectOne()
                .from(qVehicleType)
                .where(qVehicleType.name.eq(name).and(qVehicleType.id.ne(id)))
                .fetchFirst();

        return count != null;
    }

    @Override
    public boolean existsByName(String name) {
        Integer count = queryFactory
                .selectOne()
                .from(qVehicleType)
                .where(qVehicleType.name.eq(name))
                .fetchFirst();

        return count != null;
    }

    @Override
    public Optional<VehicleType> findByIdAndIsActiveTrue(Long id) {
        VehicleType result = queryFactory
                .selectFrom(qVehicleType)
                .where(qVehicleType.id.eq(id).and(qVehicleType.isActive.isTrue()))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<VehicleType> search(String keyword, Pageable pageable) {
        BooleanExpression predicate = qVehicleType.isActive.isTrue();

        if (keyword != null && !keyword.trim().isEmpty()) {
            String likePattern = "%" + keyword.trim().toLowerCase() + "%";
            predicate = predicate.and(qVehicleType
                    .code
                    .lower()
                    .like(likePattern)
                    .or(qVehicleType.name.lower().like(likePattern)));
        }

        // Count query
        Long total = queryFactory
                .select(qVehicleType.id.count())
                .from(qVehicleType)
                .where(predicate)
                .fetchOne();

        // Data query
        var query = queryFactory
                .selectFrom(qVehicleType)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // Apply sorting
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                var path =
                        switch (order.getProperty()) {
                            case "code" -> qVehicleType.code;
                            case "name" -> qVehicleType.name;
                                // case "description" -> qVehicleType.description;
                            default -> qVehicleType.id;
                        };
                query.orderBy(order.isAscending() ? path.asc() : path.desc());
            });
        } else {
            query.orderBy(qVehicleType.id.desc());
        }

        var content = query.fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
