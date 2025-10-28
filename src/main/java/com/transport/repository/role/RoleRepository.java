package com.transport.repository.role;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transport.entity.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
