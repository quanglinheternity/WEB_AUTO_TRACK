package com.transport.repository.invalidate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transport.entity.domain.InvalidatedToken;

@Repository
public interface InvalidateRepository extends JpaRepository<InvalidatedToken, String> {}
