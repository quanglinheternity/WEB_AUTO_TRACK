package com.transport.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transport.dto.ApiResponse;
import com.transport.dto.vehicleType.VehicleTypeRequest;
import com.transport.dto.vehicleType.VehicleTypeResponse;
import com.transport.service.vehicleType.VehicleTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/loai-xe")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('VEHICLE_TYPE')")
public class VehicleTypeController {

    private final VehicleTypeService service;

    @PostMapping
    public ApiResponse<VehicleTypeResponse> create(@Valid @RequestBody VehicleTypeRequest request) {
        return ApiResponse.<VehicleTypeResponse>builder()
                .message("Tạo loại xe thành công")
                .data(service.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<VehicleTypeResponse> update(
            @PathVariable Long id, @Valid @RequestBody VehicleTypeRequest request) {
        return ApiResponse.<VehicleTypeResponse>builder()
                .message("Cập nhật loại xe thành công")
                .data(service.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.<Void>builder().message("Xóa loại xe thành công").build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleTypeResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ApiResponse<Page<VehicleTypeResponse>> search(
            @RequestParam(required = false) String keyword, Pageable pageable) {
        return ApiResponse.<Page<VehicleTypeResponse>>builder()
                .message("Lấy danh sách thành cônng")
                .data(service.search(keyword, pageable))
                .build();
    }
}
