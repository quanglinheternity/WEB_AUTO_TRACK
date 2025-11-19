package com.transport.controller;

import com.transport.dto.ApiResponse;
import com.transport.dto.page.PageResponse;
import com.transport.dto.user.UserCreateRequest;
import com.transport.dto.user.UserDetailResponse;
import com.transport.dto.user.UserResponse;
import com.transport.dto.user.UserSearchRequest;
import com.transport.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "User", description = "APIs for managing users")
public class UserController {
    UserService userService;

    @Operation(summary = "Get all users with pagination")
    // @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/list")
    public ApiResponse<PageResponse<UserResponse>> getAll(
            UserSearchRequest request,
            @PageableDefault( sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        log.info("controller: create");
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .message("Lấy danh sách thành công")
                .data(userService.getAll(request, pageable))
                .build();
    }

    @Operation(summary = "Get user details by ID")
    @GetMapping("/{id}/detail")
    public ApiResponse<UserDetailResponse> getById(@PathVariable Long id) {
        return ApiResponse.<UserDetailResponse>builder()
                .message("Lấy chi tiết thành công")
                .data(userService.getById(id))
                .build();
    }

    @Operation(summary = "Create a new user")
    // @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping("/create")
    public ApiResponse<UserDetailResponse> create(@RequestBody @Valid UserCreateRequest request) {
        log.info("controller: getAll");

        return ApiResponse.<UserDetailResponse>builder()
                .message("Tạo người dùng thành công")
                .data(userService.create(request))
                .build();
    }

    @Operation(summary = "Update a user by ID")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PutMapping("/{id}/update")
    public ApiResponse<UserDetailResponse> update(
            @PathVariable Long id, @RequestBody @Valid UserCreateRequest request) {
        return ApiResponse.<UserDetailResponse>builder()
                .message("Cập nhật người dùng thành công")
                .data(userService.update(id, request))
                .build();
    }

    @Operation(summary = "Delete a user by ID")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/{id}/delete")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.<Void>builder().message("Xóa người dùng thành công").build();
    }
}
