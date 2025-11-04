package com.transport.service.permission;

import java.util.List;

import com.transport.dto.permission.PermissionRequest;
import com.transport.dto.permission.PermissionResponse;

public interface PermissionService {
    PermissionResponse creatPermissionResponse(PermissionRequest permissionRequest);

    List<PermissionResponse> getAll();

    void deletePermission(String permissionName);
}
