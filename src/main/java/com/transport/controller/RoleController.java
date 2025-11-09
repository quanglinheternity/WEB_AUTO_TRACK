package com.transport.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transport.dto.ApiResponse;
import com.transport.dto.role.RoleRequest;
import com.transport.dto.role.RoleResponse;
import com.transport.service.role.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasAuthority('ROLE')")
@Tag(name = "Role Management", description = "APIs for managing system roles")
public class RoleController {
    RoleService roleService;

    @Operation(summary = "Get list of all roles")
    @GetMapping("/list")
    public ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .message("Lấy danh sách thành công")
                .data(roleService.getAll())
                .build();
    }

    @Operation(summary = "Create a new role")
    @PostMapping("/create")
    public ApiResponse<RoleResponse> create(@RequestBody @Valid RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .message("Tạo quyền thành công")
                .data(roleService.creatRoleResponse(request))
                .build();
    }

    @Operation(summary = "Delete a role by name")
    @DeleteMapping("/{roleName}/delete")
    public ApiResponse<Void> delete(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
        return ApiResponse.<Void>builder().message("Xóa quyền thành công").build();
    }
}
