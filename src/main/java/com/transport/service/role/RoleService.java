package com.transport.service.role;


import java.util.List;

import com.transport.dto.role.RoleRequest;
import com.transport.dto.role.RoleResponse;

public interface RoleService {
    RoleResponse creatRoleResponse(RoleRequest roleRequest);
    List<RoleResponse> getAll();
    void deleteRole(String RoleName);
}
