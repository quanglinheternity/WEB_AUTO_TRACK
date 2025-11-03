package com.transport.controller;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transport.dto.ApiResponse;
import com.transport.dto.page.PageResponse;
import com.transport.dto.user.UserCreateRequest;
import com.transport.dto.user.UserDetailResponse;
import com.transport.dto.user.UserResponse;
import com.transport.dto.user.UserSearchRequest;
import com.transport.service.user.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/nguoi-dung")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> getAll(
            UserSearchRequest request,
            @PageableDefault(page = 0, size = 10, sort = "expenseDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .message("Lấy danh sách thành công")
                .data(userService.getAll(request, pageable))
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getById(@PathVariable Long id) {
        return ApiResponse.<UserDetailResponse>builder()
                .message("Lấy chi tiết thành công")
                .data(userService.getById(id))
                .build();
    }
    // @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping
    public ApiResponse<UserDetailResponse> create(@RequestBody @Valid UserCreateRequest request) {
        return ApiResponse.<UserDetailResponse>builder()
                .message("Tạo người dùng thành công")
                .data(userService.create(request))
                .build();
    }
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PutMapping("/{id}")
    public ApiResponse<UserDetailResponse> update(@PathVariable Long id,@RequestBody @Valid UserCreateRequest request) {
        return ApiResponse.<UserDetailResponse>builder()
                .message("Cập nhật người dùng thành công")
                .data(userService.update(id, request))
                .build();
    }
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Xóa người dùng thành công")
                .build();
    }
}
