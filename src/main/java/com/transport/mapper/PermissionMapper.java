package com.transport.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.transport.dto.permission.PermissionRequest;
import com.transport.dto.permission.PermissionResponse;
import com.transport.entity.domain.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    @Mapping(source = "permissionName", target = "name")
    PermissionResponse toResponse(Permission permission);

    @Mapping(source = "name", target = "permissionName")
    @Mapping(target = "roles", ignore = true)
    Permission toPermission(PermissionRequest permissionRequest);


}
