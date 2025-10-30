package com.transport.repository.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transport.entity.domain.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, TripRepositoryCustom {

}
