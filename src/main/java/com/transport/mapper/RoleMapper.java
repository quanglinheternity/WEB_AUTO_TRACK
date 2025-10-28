package com.transport.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.transport.dto.role.RoleRequest;
import com.transport.dto.role.RoleResponse;
import com.transport.entity.domain.Role;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    @Mapping(source = "roleName", target = "name")
    // @Mapping(target = "permissions", ignore = true)
    RoleResponse toResponse(Role role);

    @Mapping(source = "name", target = "roleName")
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role toRole(RoleRequest roleRequest);


}
