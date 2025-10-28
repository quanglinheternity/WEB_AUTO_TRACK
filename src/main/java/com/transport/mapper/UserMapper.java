package com.transport.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.transport.dto.user.UserCreateRequest;
import com.transport.dto.user.UserDetailResponse;
import com.transport.dto.user.UserResponse;
import com.transport.entity.domain.User;

@Mapper(componentModel = "spring", uses = { PermissionMapper.class })
public interface UserMapper {
    UserResponse toResponse(User user);
    
    // @Mapping(target = "permissions", source = "permissions")
    UserDetailResponse toDetailResponse(User user);

    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "approvedTrips", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "createdTrips", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toCreateEntity(UserCreateRequest request);

    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "approvedTrips", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "createdTrips", ignore = true)
    @Mapping(target = "id", ignore = true)
    // @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateFromRequest(UserCreateRequest request, @MappingTarget User user);

}
