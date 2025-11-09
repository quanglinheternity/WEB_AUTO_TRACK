package com.transport.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import com.transport.dto.route.RouteRequest;
import com.transport.dto.route.RouteResponse;
import com.transport.dto.route.RouteSearchRequest;
import com.transport.dto.route.RouteUpdateRequest;
import com.transport.service.route.RouteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Route", description = "APIs for managing routes")
public class RouteController {
    RouteService routeService;

    @Operation(summary = "Get all routes with pagination")
    @GetMapping("/list")
    public ApiResponse<PageResponse<RouteResponse>> getAll(
            RouteSearchRequest request,
            @PageableDefault(page = 0, size = 10, sort = "expenseDate", direction = Sort.Direction.DESC)
                    Pageable pageable) {

        return ApiResponse.<PageResponse<RouteResponse>>builder()
                .message("Lấy danh sách tuyến đường thành công")
                .data(routeService.getAll(request, pageable))
                .build();
    }

    @Operation(summary = "Get route details by ID")
    @GetMapping("/{id}/detail")
    public ApiResponse<RouteResponse> getById(@PathVariable Long id) {
        return ApiResponse.<RouteResponse>builder()
                .message("Lấy chi tiết tuyến đường thành công.")
                .data(routeService.getById(id))
                .build();
    }

    @Operation(summary = "Create a new route")
    @PostMapping("/create")
    public ApiResponse<RouteResponse> create(@Valid @RequestBody RouteRequest request) {
        return ApiResponse.<RouteResponse>builder()
                .message("Tạo tuyến đường thành công.")
                .data(routeService.create(request))
                .build();
    }

    @Operation(summary = "Create a new route")
    @PutMapping("/{id}/update")
    public ApiResponse<RouteResponse> update(@PathVariable Long id, @Valid @RequestBody RouteUpdateRequest request) {
        return ApiResponse.<RouteResponse>builder()
                .message("Cập nhật tuyến đường thành công.")
                .data(routeService.update(id, request))
                .build();
    }

    @Operation(summary = "Delete a route by ID")
    @DeleteMapping("/{id}/delete")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Xóa tuyến đường thành công.")
                .build();
    }
}
