package com.transport.controller;

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
import com.transport.dto.trip.ApproveTripRequest;
import com.transport.dto.trip.TripCreateRequest;
import com.transport.dto.trip.TripResponse;
import com.transport.dto.trip.TripSearchRequest;
import com.transport.dto.trip.TripUpdateRequest;
import com.transport.dto.trip.UpdateTripStatusRequest;
import com.transport.service.trip.TripService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/lich-trinh")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TripController {
    TripService tripService;
    @GetMapping
    public ApiResponse<PageResponse<TripResponse>> getAll(
        TripSearchRequest request,
        @PageableDefault(page = 0, size = 10, sort = "expenseDate", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        return ApiResponse.<PageResponse<TripResponse>>builder()
                .message("Lấy danh sách thành công")
                .data(tripService.getAll(request, pageable))
                .build();
    }
    @PostMapping
    public ApiResponse<TripResponse> create(@RequestBody TripCreateRequest request) {
        return ApiResponse.<TripResponse>builder()
                .message("Tạo lịch trình thành công")
                .data(tripService.createTrip(request))
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<TripResponse> update(@PathVariable Long id,@RequestBody TripUpdateRequest request) {
        return ApiResponse.<TripResponse>builder()
                .message("Cập nhật lịch trình thành công")
                .data(tripService.updateTrip(id, request))
                .build();
    }
    @PutMapping("/{id}/trang-thai-van-chuyen")
    public ApiResponse<TripResponse> updateTripStatus (
            @PathVariable Long id,
            @Valid @RequestBody UpdateTripStatusRequest request
    ) {
        return ApiResponse.<TripResponse>builder()
                .data(tripService.updateTripStatus(id, request))
                .build();
    }
    @PutMapping("/{id}/phe-duyet")
    public ApiResponse<TripResponse> approve(@PathVariable Long id, @RequestBody @Valid ApproveTripRequest request) {
        return ApiResponse.<TripResponse>builder()
                .data(tripService.approveTrip(id, request))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        tripService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Xóa lịch trình thành công")
                .build();
    }
}
