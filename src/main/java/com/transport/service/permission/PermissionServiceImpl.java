package com.transport.service.permission;

import java.util.List;

import org.springframework.stereotype.Service;

import com.transport.dto.permission.PermissionRequest;
import com.transport.dto.permission.PermissionResponse;
import com.transport.entity.domain.Permission;
import com.transport.mapper.PermissionMapper;
import com.transport.repository.permission.PermissionRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse creatPermissionResponse(PermissionRequest permissionRequest){
        Permission permission = permissionMapper.toPermission(permissionRequest);
        permission = permissionRepository.save(permission);
        return permissionMapper.toResponse(permission);

    }
    @Override
    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream().map(permissionMapper::toResponse).toList();
    }
    @Override
    public void deletePermission(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }


}
