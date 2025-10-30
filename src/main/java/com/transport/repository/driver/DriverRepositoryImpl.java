package com.transport.repository.driver;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.transport.entity.domain.QDriver;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DriverRepositoryImpl implements DriverRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QDriver qDriver = QDriver.driver;

    @Override
    public boolean existsBylicenseNumber(String licenseNumber) {
        Integer count = queryFactory
                .selectOne()
                .from(qDriver)
                .where(qDriver.licenseNumber.eq(licenseNumber))
                .fetchFirst();

        return count != null;
    }

}