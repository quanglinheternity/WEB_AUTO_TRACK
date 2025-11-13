package com.transport.controller;

import java.io.IOException;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.transport.dto.ApiResponse;
import com.transport.dto.page.PageResponse;
import com.transport.dto.salary.PaySalaryRequest;
import com.transport.dto.salary.SalaryCalculationRequest;
import com.transport.dto.salary.SalaryCalculationResponse;
import com.transport.dto.salary.SalaryCalculationDetailResponse;
import com.transport.dto.salary.SalaryReportSearchRequest;
import com.transport.service.salary.SalaryCalculationService;
import com.transport.service.salary.SalaryReportService;
import com.transport.util.excel.BaseExportMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/salary")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Salary", description = "APIs for managing driver salaries")
public class SalaryController {

    private final SalaryCalculationService salaryCalculationService;
    private final SalaryReportService salaryReportService;

    @Operation(summary = "Calculate salary for a specific driver")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<SalaryCalculationResponse>> calculateSalary(
            @RequestBody SalaryCalculationRequest request) {

        log.info("Calculating salary for driver: {} in month: {}", request.getDriverId(), request.getMonth());

        SalaryCalculationResponse response =
                salaryCalculationService.calculateSalary(request.getDriverId(), request.getMonth());

        return ResponseEntity.ok(ApiResponse.<SalaryCalculationResponse>builder()
                .code(200)
                .message("Tính lương thành công")
                .data(response)
                .build());
    }

    @Operation(summary = "Calculate salary for all drivers in a specific month")
    @PostMapping("/calculate-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SalaryCalculationResponse>>> calculateSalaryForAll(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        log.info("Calculating salary for all drivers in month: {}", month);

        List<SalaryCalculationResponse> responses = salaryCalculationService.calculateSalaryForAllDrivers(month);

        return ResponseEntity.ok(ApiResponse.<List<SalaryCalculationResponse>>builder()
                .code(200)
                .message("Tính lương cho " + responses.size() + " tài xế thành công")
                .data(responses)
                .build());
    }

    @Operation(summary = "Get salary report details by report ID")
    @GetMapping("/report/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<SalaryCalculationDetailResponse>> getSalaryReportDetail(@PathVariable Long reportId) {

        SalaryCalculationDetailResponse response = salaryCalculationService.calculateSalaryDetail(reportId);

        return ResponseEntity.ok(ApiResponse.<SalaryCalculationDetailResponse>builder()
                .code(200)
                .message("Lấy chi tiết báo cáo lương thành công")
                .data(response)
                .build());
    }

    @Operation(summary = "Pay salaries based on salary report IDs")
    @PostMapping("/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<String>> paySalary(@RequestBody PaySalaryRequest request) {

        salaryReportService.paySalaries(request);

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(200)
                .message("Xác nhận thanh toán lương thành công")
                .data("Đã thanh toán " + request.getSalaryReportIds().size() + " báo cáo lương")
                .build());
    }

    @Operation(summary = "Mark a single salary report as paid")
    @PutMapping("/report/{reportId}/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<String>> paySingleSalary(
            @PathVariable Long reportId, @RequestParam(required = false) String note) {

        salaryCalculationService.markAsPaid(reportId);

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(200)
                .message("Xác nhận thanh toán lương thành công")
                .build());
    }

    @Operation(summary = "Get salary statistics, optionally filtered by month")
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<?>> getSalaryStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        var statistics = salaryReportService.getSalaryStatistics(month);

        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .message("Lấy thống kê lương thành công")
                .data(statistics)
                .build());
    }

    @Operation(summary = "Export salary statistics to Excel, optionally filtered by month")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    @GetMapping("/statistics/export/excel")
    public void exportSalaryStatistics(@RequestParam(required = false) String month, HttpServletResponse response)
            throws IOException {

        YearMonth reportMonth = (month != null) ? YearMonth.parse(month) : YearMonth.now();
        if (reportMonth.isAfter(YearMonth.now())) {
            reportMonth = YearMonth.now();
        }

        Map<String, Object> stats = salaryReportService.getSalaryStatistics(reportMonth);
        List<Map<String, Object>> data = List.of(stats); // 1 dòng thống kê

        String[] headers = {
            "Tháng",
            "Tổng tài xế",
            "Đã thanh toán",
            "Chưa thanh toán",
            "Tổng lương",
            "Đã trả",
            "Chưa trả",
            "Tổng chuyến",
            "Tổng km",
            "Lương trung bình"
        };

        String[] keys = {
            "month",
            "totalDrivers",
            "paidDrivers",
            "unpaidDrivers",
            "totalSalary",
            "paidSalary",
            "unpaidSalary",
            "totalTrips",
            "totalDistance",
            "averageSalary"
        };

        new BaseExportMap(data)
                .writeHeaderLine(headers, "Thống kê lương tài xế")
                .writeDataLines(keys)
                .export(response, "Thong_ke_luong_tai_xe");
    }

    @Operation(summary = "Search salary reports with pagination")
    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<PageResponse<SalaryCalculationResponse>>> searchSalaryReports(
            SalaryReportSearchRequest request,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {

        PageResponse<SalaryCalculationResponse> page = salaryReportService.searchSalaryReports(request, pageable);

        return ResponseEntity.ok(ApiResponse.<PageResponse<SalaryCalculationResponse>>builder()
                .code(200)
                .message("Lấy danh sách báo cáo lương thành công")
                .data(page)
                .build());
    }
}
