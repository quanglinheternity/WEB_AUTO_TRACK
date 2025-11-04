package com.transport.service.salary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.transport.dto.page.PageResponse;
import com.transport.dto.salary.PaySalaryRequest;
import com.transport.dto.salary.SalaryCalculationResponse;
import com.transport.dto.salary.SalaryReportDetailResponse;
import com.transport.dto.salary.SalaryReportSearchRequest;
import com.transport.entity.domain.SalaryReport;
import com.transport.entity.domain.Trip;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.salary.SalaryReportRepository;
import com.transport.repository.trip.TripRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalaryReportService {
    private final SalaryReportRepository salaryReportRepository;
    private final TripRepository tripRepository;
     public SalaryReportDetailResponse getSalaryReportDetail(Long reportId) {
        SalaryReport report = salaryReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy báo cáo lương"));

        // Lấy danh sách chuyến đi trong tháng
        YearMonth month = report.getReportMonth();
        List<Trip> trips = tripRepository.findCompletedTripsByDriverAndMonth(
                report.getDriver().getId(),
                month
        );

        List<SalaryReportDetailResponse.TripSummary> tripSummaries = trips.stream()
                .map(trip -> SalaryReportDetailResponse.TripSummary.builder()
                        .tripId(trip.getId())
                        .tripCode(trip.getTripCode())
                        .routeName(trip.getRoute() != null ? trip.getRoute().getName() : "N/A")
                        .distance(trip.getRoute() != null ? trip.getRoute().getDistanceKm() : BigDecimal.ZERO)
                        .departureTime(trip.getDepartureTime())
                        .actualArrivalTime(trip.getActualArrivalTime())
                        .status(trip.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        return SalaryReportDetailResponse.builder()
                .reportId(report.getId())
                .driverId(report.getDriver().getId())
                .driverName(report.getDriver().getUser().getFullName())
                .driverCode(report.getDriver().getDriverCode())
                .month(report.getReportMonth())
                .baseSalary(report.getBaseSalary())
                .totalTrips(report.getTotalTrips())
                .totalDistance(report.getTotalDistance())
                .tripBonus(report.getTripBonus())
                .allowance(report.getAllowance())
                .deduction(report.getDeduction())
                .totalSalary(report.getTotalSalary())
                .isPaid(report.getIsPaid())
                .paidAt(report.getPaidAt())
                .note(report.getNote())
                .trips(tripSummaries)
                .build();
    }
    public void paySalaries(PaySalaryRequest request) {
        LocalDateTime now = LocalDateTime.now();

        List<SalaryReport> reports = salaryReportRepository.findAllByIdIn(request.getSalaryReportIds());
        if (reports.isEmpty()) {
            throw new AppException(ErrorCode.SALARY_REPORT_NOT_FOUND);
        }

        for (SalaryReport report : reports) {

            if (Boolean.TRUE.equals(report.getIsPaid())) {
                
                continue;
            }

            // Đánh dấu là đã thanh toán
            report.setIsPaid(true);
            report.setPaidAt(now);

            // Ghi chú thêm nếu có
            if (request.getNote() != null && !request.getNote().isBlank()) {
                String existingNote = report.getNote() != null ? report.getNote() + "; " : "";
                report.setNote(existingNote + request.getNote());
            }

        }

        // 3️⃣ Lưu toàn bộ
        salaryReportRepository.saveAll(reports);
    }
    public Map<String, Object> getSalaryStatistics(YearMonth month) {
        if(month == null || month.isAfter(YearMonth.now())) {
            month = YearMonth.now();
        }
        List<SalaryReport> reports = salaryReportRepository.findAllByReportMonth(month);
        
        BigDecimal totalSalary = reports.stream()
                .map(SalaryReport::getTotalSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal paidSalary = reports.stream()
                .filter(SalaryReport::getIsPaid)
                .map(SalaryReport::getTotalSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal unpaidSalary = totalSalary.subtract(paidSalary);
        
        long totalDrivers = reports.size();
        long paidDrivers = reports.stream().filter(SalaryReport::getIsPaid).count();
        long unpaidDrivers = totalDrivers - paidDrivers;
        
        Integer totalTrips = reports.stream()
                .map(SalaryReport::getTotalTrips)
                .reduce(0, Integer::sum);
        
        BigDecimal totalDistance = reports.stream()
                .map(SalaryReport::getTotalDistance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("month", month);
        statistics.put("totalDrivers", totalDrivers);
        statistics.put("paidDrivers", paidDrivers);
        statistics.put("unpaidDrivers", unpaidDrivers);
        statistics.put("totalSalary", totalSalary);
        statistics.put("paidSalary", paidSalary);
        statistics.put("unpaidSalary", unpaidSalary);
        statistics.put("totalTrips", totalTrips);
        statistics.put("totalDistance", totalDistance);
        statistics.put("averageSalary", totalDrivers > 0 ? 
                totalSalary.divide(BigDecimal.valueOf(totalDrivers), 2, RoundingMode.HALF_UP) : 
                BigDecimal.ZERO);
        
        return statistics;
     
    }
    public PageResponse<SalaryCalculationResponse> searchSalaryReports(SalaryReportSearchRequest request, Pageable pageable) {
        Page<SalaryCalculationResponse> page = salaryReportRepository.searchSalaryReports(request, pageable);
        return PageResponse.from(page);
    }
}
