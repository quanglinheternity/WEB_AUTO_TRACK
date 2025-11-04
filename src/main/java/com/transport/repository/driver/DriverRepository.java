package com.transport.repository.driver;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transport.entity.domain.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>, DriverRepositoryCustom {
}
