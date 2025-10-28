package com.transport.service.role;

import java.util.List;

import org.springframework.stereotype.Service;

import com.transport.dto.role.RoleRequest;
import com.transport.dto.role.RoleResponse;
import com.transport.entity.domain.Permission;
import com.transport.entity.domain.Role;
import com.transport.mapper.RoleMapper;
import com.transport.repository.permission.PermissionRepository;
import com.transport.repository.role.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.HashSet;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse creatRoleResponse(RoleRequest roleRequest) {
        Role role = roleMapper.toRole(roleRequest);
        role = roleRepository.save(role);
        List<Permission> permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toResponse(role);

    }
    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toResponse).toList();
    }
    @Override
    public void deleteRole(String RoleName) {
        roleRepository.deleteById(RoleName);
    }


}
