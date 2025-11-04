package com.transport.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.transport.dto.ApiResponse;
import com.transport.dto.page.PageResponse;
import com.transport.dto.salary.PaySalaryRequest;
import com.transport.dto.salary.SalaryCalculationRequest;
import com.transport.dto.salary.SalaryCalculationResponse;
import com.transport.dto.salary.SalaryReportDetailResponse;
import com.transport.dto.salary.SalaryReportSearchRequest;
import com.transport.service.salary.SalaryCalculationService;
import com.transport.service.salary.SalaryReportService;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/salary")
@RequiredArgsConstructor
@Slf4j
public class SalaryController {

        private final SalaryCalculationService salaryCalculationService;
        private final SalaryReportService salaryReportService;
        @PostMapping("/calculate")
        public ResponseEntity<ApiResponse<SalaryCalculationResponse>> calculateSalary(
                @RequestBody SalaryCalculationRequest request) {
                
                log.info("Calculating salary for driver: {} in month: {}", 
                        request.getDriverId(), request.getMonth());
                
                SalaryCalculationResponse response = salaryCalculationService
                        .calculateSalary(request.getDriverId(), request.getMonth());
                
                return ResponseEntity.ok(ApiResponse.<SalaryCalculationResponse>builder()
                        .code(200)
                        .message("Tính lương thành công")
                        .data(response)
                        .build());
        }
        @PostMapping("/calculate-all")
        public ResponseEntity<ApiResponse<List<SalaryCalculationResponse>>> calculateSalaryForAll(
                @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
                
                log.info("Calculating salary for all drivers in month: {}", month);
                
                List<SalaryCalculationResponse> responses = salaryCalculationService
                        .calculateSalaryForAllDrivers(month);
                
                return ResponseEntity.ok(ApiResponse.<List<SalaryCalculationResponse>>builder()
                        .code(200)
                        .message("Tính lương cho " + responses.size() + " tài xế thành công")
                        .data(responses)
                        .build());
        }
        @GetMapping("/report/{reportId}")
        public ResponseEntity<ApiResponse<SalaryReportDetailResponse>> getSalaryReportDetail(
                @PathVariable Long reportId) {
                
                SalaryReportDetailResponse response = salaryReportService
                        .getSalaryReportDetail(reportId);
                
                return ResponseEntity.ok(ApiResponse.<SalaryReportDetailResponse>builder()
                        .code(200)
                        .message("Lấy chi tiết báo cáo lương thành công")
                        .data(response)
                        .build());
        }
        @PostMapping("/pay")
        public ResponseEntity<ApiResponse<String>> paySalary(
                @RequestBody PaySalaryRequest request) {
                
                
                salaryReportService.paySalaries(request);
                
                return ResponseEntity.ok(ApiResponse.<String>builder()
                        .code(200)
                        .message("Xác nhận thanh toán lương thành công")
                        .data("Đã thanh toán " + request.getSalaryReportIds().size() + " báo cáo lương")
                        .build());
        }

        @PutMapping("/report/{reportId}/pay")
        public ResponseEntity<ApiResponse<String>> paySingleSalary(
                @PathVariable Long reportId,
                @RequestParam(required = false) String note) {
                
                salaryCalculationService.markAsPaid(reportId);
                
                return ResponseEntity.ok(ApiResponse.<String>builder()
                        .code(200)
                        .message("Xác nhận thanh toán lương thành công")
                        .build());
        }
        @GetMapping("/statistics")
        public ResponseEntity<ApiResponse<?>> getSalaryStatistics(
                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
                
                var statistics = salaryReportService.getSalaryStatistics(month);
                
                return ResponseEntity.ok(ApiResponse.builder()
                        .code(200)
                        .message("Lấy thống kê lương thành công")
                        .data(statistics)
                        .build());
        }
        @GetMapping("/reports")
        public ResponseEntity<ApiResponse<PageResponse<SalaryCalculationResponse>>> searchSalaryReports(
                SalaryReportSearchRequest request,
                @PageableDefault(page = 0, size = 10, sort = "expenseDate", direction = Sort.Direction.DESC)
                Pageable pageable) {

        PageResponse<SalaryCalculationResponse> page = salaryReportService.searchSalaryReports(request, pageable);

        return ResponseEntity.ok(ApiResponse.<PageResponse<SalaryCalculationResponse>>builder()
                .code(200)
                .message("Lấy danh sách báo cáo lương thành công")
                .data(page)
                .build());
        }
}  