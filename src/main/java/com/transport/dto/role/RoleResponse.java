package com.transport.dto.role;

import java.util.Set;

import com.transport.dto.permission.PermissionResponse;

public record RoleResponse( String name, String description, Set<PermissionResponse> permissions) {
   
    
}
