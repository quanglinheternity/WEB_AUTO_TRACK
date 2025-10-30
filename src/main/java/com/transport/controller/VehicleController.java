package com.transport.controller;

import java.util.List;

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
import com.transport.dto.vehicle.VehicleAndTripResponse;
import com.transport.dto.vehicle.VehicleRequest;
import com.transport.dto.vehicle.VehicleResponse;
import com.transport.dto.vehicle.VehicleUpdateRequest;
import com.transport.service.vehicle.VehicleService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/xes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VehicleController {
    VehicleService vehicleService;

    @PreAuthorize("hasAuthority('VEHICLE_READ')")
    @GetMapping
    public ApiResponse<List<VehicleResponse>> getAll() {
        return ApiResponse.<List<VehicleResponse>>builder()
                .message("Lấy danh sách thành công")
                .data(vehicleService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<VehicleAndTripResponse> getById(@PathVariable Long id) {
        return ApiResponse.<VehicleAndTripResponse>builder()
                .message("Lấy chi tiết thành công")
                .data(vehicleService.getById(id))
                .build();
    }
    @PreAuthorize("hasAuthority('VEHICLE_CREATE')")
    @PostMapping
    public ApiResponse<VehicleResponse> create(@RequestBody @Valid VehicleRequest request) {
        return ApiResponse.<VehicleResponse>builder()
                .message("Tạo xe thành công")
                .data(vehicleService.create(request))
                .build();
    }
    @PreAuthorize("hasAuthority('VEHICLE_UPDATE')")
    @PutMapping("/{id}")
    public ApiResponse<VehicleResponse> update(@PathVariable Long id,@RequestBody @Valid VehicleUpdateRequest request) {
        return ApiResponse.<VehicleResponse>builder()
                .message("Cập nhật xe thành công")
                .data(vehicleService.update(id, request))
                .build();
    }
    @PreAuthorize("hasAuthority('VEHICLE_DELETE')")
    @DeleteMapping("/{id}")
     public ApiResponse<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Xóa xe thành công")
                .build();
    }
    @PutMapping("/{id}/trang-thai")
    public ApiResponse<VehicleResponse> updateByTrangThai(@PathVariable Long id) {
        
        return ApiResponse.<VehicleResponse>builder()
                .code(1000)
                .message("Cập nhật trạng thái thành công")
                .data(vehicleService.toggleActiveStatus(id))
                .build();
    }
}
