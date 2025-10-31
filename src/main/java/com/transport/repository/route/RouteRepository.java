package com.transport.repository.route;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transport.entity.domain.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Route findByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
