package com.transport.controller;

import jakarta.validation.Valid;

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
import com.transport.dto.vehicle.VehicleAndTripResponse;
import com.transport.dto.vehicle.VehicleRequest;
import com.transport.dto.vehicle.VehicleResponse;
import com.transport.dto.vehicle.VehicleSearchRequest;
import com.transport.dto.vehicle.VehicleUpdateRequest;
import com.transport.service.vehicle.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Vehicle", description = "APIs for managing vehicles")
public class VehicleController {
    VehicleService vehicleService;

    @Operation(summary = "Get all vehicles with pagination")
    @PreAuthorize("hasAuthority('VEHICLE_READ')")
    @GetMapping("/list")
    public ApiResponse<PageResponse<VehicleResponse>> getAll(
            VehicleSearchRequest request,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.<PageResponse<VehicleResponse>>builder()
                .message("Lấy danh sách thành công")
                .data(vehicleService.getAll(request, pageable))
                .build();
    }

    @Operation(summary = "Get vehicle details by ID, including trips")
    @GetMapping("/{id}/detail")
    public ApiResponse<VehicleAndTripResponse> getById(@PathVariable Long id) {
        return ApiResponse.<VehicleAndTripResponse>builder()
                .message("Lấy chi tiết thành công")
                .data(vehicleService.getById(id))
                .build();
    }

    @Operation(summary = "Create a new vehicle")
    @PreAuthorize("hasAuthority('VEHICLE_CREATE')")
    @PostMapping("/create")
    public ApiResponse<VehicleResponse> create(@RequestBody @Valid VehicleRequest request) {
        return ApiResponse.<VehicleResponse>builder()
                .message("Tạo xe thành công")
                .data(vehicleService.create(request))
                .build();
    }

    @Operation(summary = "Update a vehicle by ID")
    @PreAuthorize("hasAuthority('VEHICLE_UPDATE')")
    @PutMapping("/{id}/update")
    public ApiResponse<VehicleResponse> update(
            @PathVariable Long id, @RequestBody @Valid VehicleUpdateRequest request) {
        return ApiResponse.<VehicleResponse>builder()
                .message("Cập nhật xe thành công")
                .data(vehicleService.update(id, request))
                .build();
    }

    @Operation(summary = "Delete a vehicle by ID")
    @PreAuthorize("hasAuthority('VEHICLE_DELETE')")
    @DeleteMapping("/{id}/delete")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ApiResponse.<Void>builder().message("Xóa xe thành công").build();
    }

    @Operation(summary = "Toggle vehicle active status by ID")
    @PutMapping("/{id}/status")
    public ApiResponse<VehicleResponse> toggleActiveStatus(@PathVariable Long id) {

        return ApiResponse.<VehicleResponse>builder()
                .code(1000)
                .message("Cập nhật trạng thái thành công")
                .data(vehicleService.toggleActiveStatus(id))
                .build();
    }
}
