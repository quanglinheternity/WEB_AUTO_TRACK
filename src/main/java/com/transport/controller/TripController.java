package com.transport.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
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
import com.transport.dto.trip.ApproveTripRequest;
import com.transport.dto.trip.TripCreateRequest;
import com.transport.dto.trip.TripResponse;
import com.transport.dto.trip.TripSearchRequest;
import com.transport.dto.trip.TripUpdateRequest;
import com.transport.dto.trip.UpdateTripStatusRequest;
import com.transport.service.trip.TripService;
import com.transport.util.excel.BaseExport;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Trip", description = "APIs for managing trips")
public class TripController {
    TripService tripService;

    @Operation(summary = "Get all trips with pagination")
    @GetMapping("/list")
    public ApiResponse<PageResponse<TripResponse>> getAll(
            TripSearchRequest request,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.<PageResponse<TripResponse>>builder()
                .message("Lấy danh sách thành công")
                .data(tripService.getAll(request, pageable))
                .build();
    }

    @Operation(summary = "Create a new trip")
    @PostMapping("/create")
    public ApiResponse<TripResponse> create(@RequestBody TripCreateRequest request) {
        return ApiResponse.<TripResponse>builder()
                .message("Tạo lịch trình thành công")
                .data(tripService.createTrip(request))
                .build();
    }

    @Operation(summary = "Update a trip by ID")
    @PutMapping("/{id}/update")
    public ApiResponse<TripResponse> update(@PathVariable Long id, @RequestBody TripUpdateRequest request) {
        return ApiResponse.<TripResponse>builder()
                .message("Cập nhật lịch trình thành công")
                .data(tripService.updateTrip(id, request))
                .build();
    }

    @Operation(summary = "Update the status of a trip")
    @PutMapping("/{id}/update-status")
    public ApiResponse<TripResponse> updateTripStatus(
            @PathVariable Long id, @Valid @RequestBody UpdateTripStatusRequest request) {
        return ApiResponse.<TripResponse>builder()
                .data(tripService.updateTripStatus(id, request))
                .build();
    }

    @Operation(summary = "Approve a trip by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}/approve")
    public ApiResponse<TripResponse> approve(@PathVariable Long id, @RequestBody @Valid ApproveTripRequest request) {
        return ApiResponse.<TripResponse>builder()
                .data(tripService.approveTrip(id, request))
                .build();
    }

    @Operation(summary = "Delete a trip by ID")
    @DeleteMapping("/{id}/delete")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        tripService.delete(id);
        return ApiResponse.<Void>builder().message("Xóa lịch trình thành công").build();
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        List<TripResponse> listUsers = tripService.listAll();
        String[] headers = new String[] {
            "ID",
            "Mã chuyến",
            "Tuyến",
            "Biển số xe",
            "Tên tài xế",
            "Thời gian khởi hành",
            "Thời gian dự kiến đến",
            "Thời gian thực tế đến",
            "Trạng thái",
            "Mô tả hàng hóa",
            "Khối lượng hàng",
            "Người tạo",
            "Thời gian duyệt",
            "Người duyệt",
            "Thời gian hoàn thành",
            "Thời gian hủy",
            "Lý do hủy",
            "Ghi chú"
        };
        String[] fields = new String[] {
            "id",
            "tripCode",
            "routeName",
            "vehiclePlateNumber",
            "driverName",
            "departureTime",
            "estimatedArrivalTime",
            "actualArrivalTime",
            "status",
            "cargoDescription",
            "cargoWeight",
            "createdBy",
            "approvedAt",
            "approvedBy",
            "completedAt",
            "cancelledAt",
            "cancellationReason",
            "note"
        };
        new BaseExport<>(listUsers)
                .writeHeaderLine(headers, "Danh sách Chuyến đi")
                .writeDataLines(fields, TripResponse.class)
                .export(response, "Danh_sach_chuyen_di");
    }
}
