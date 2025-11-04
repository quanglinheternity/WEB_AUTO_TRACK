package com.transport.repository.route;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transport.entity.domain.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>, RouteRepositoryCustom {
    Route findByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
