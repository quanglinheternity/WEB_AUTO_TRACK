package com.transport.service.salary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.dto.route.RouteBySalary;
import com.transport.dto.salary.SalaryCalculationDetailResponse;
import com.transport.dto.salary.SalaryCalculationResponse;
import com.transport.entity.domain.Driver;
import com.transport.entity.domain.SalaryReport;
import com.transport.entity.domain.Trip;
import com.transport.enums.TripStatus;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.driver.DriverRepository;
import com.transport.repository.route.RouteRepository;
import com.transport.repository.salary.SalaryReportRepository;
import com.transport.repository.trip.TripRepository;
import com.transport.service.redis.RedisService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class SalaryCalculationServiceImpl implements SalaryCalculationService {
    SalaryReportRepository salaryReportRepository;
    TripRepository tripRepository;
    DriverRepository driverRepository;
    RouteRepository routeRepository;
    RedisService<String, Object, Object> redisService;
    ObjectMapper objectMapper;

    static BigDecimal DEFAULT_TRIP_RATE = new BigDecimal("300000"); // 300k/chuyến
    static BigDecimal DEFAULT_DISTANCE_RATE = new BigDecimal("2000"); // 2k/km
    static BigDecimal RESPONSIBILITY_ALLOWANCE = new BigDecimal("500000"); // 500k
    static BigDecimal SAFETY_BONUS = new BigDecimal("1000000"); // 1 triệu
    static BigDecimal INSURANCE_RATE = new BigDecimal("0.105"); // 10.5%
    static BigDecimal UNION_FEE = new BigDecimal("0.01"); // 1%

    public SalaryCalculationDetailResponse calculateSalaryDetail(Long reportId) {
        String cacheKey = "salary:detail:" + reportId;

        // 1. Kiểm tra cache
        try {
            Object cachedData = redisService.get(cacheKey);
            if (cachedData != null) {
                System.out.println("🚀 Lấy chi tiết lương từ Redis cache");
                return objectMapper.convertValue(cachedData, SalaryCalculationDetailResponse.class);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi khi đọc Redis cache: " + e.getMessage());
            redisService.delete(cacheKey);
        }
        SalaryReport report = salaryReportRepository
                .findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.SALARY_REPORT_NOT_FOUND));
        Long driverId = report.getDriver().getId();
        Driver driver =
                driverRepository.findById(driverId).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        YearMonth month = report.getReportMonth();
        List<RouteBySalary> baseRoutes = routeRepository.findRoutesByDriverAndMonth(driverId, month);
        List<RouteBySalary> routes = baseRoutes.stream()
                .map(route -> {
                    BigDecimal calculatedSalary = calculateTripBonus(route.totalTrips(), route.totalDistance(), driver);
                    return new RouteBySalary(route.name(), route.totalTrips(), route.totalDistance(), calculatedSalary);
                })
                .toList();
        Map<String, BigDecimal> incomes = Map.of(
                "Base Salary",
                driver.getBaseSalary(),
                "Job Allowance",
                report.getDeduction() != null ? report.getDeduction() : BigDecimal.ZERO);
        BigDecimal routeIncome =
                routes.stream().map(RouteBySalary::totalSalary).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal otherIncome = incomes.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIncome = routeIncome.add(otherIncome);
        BigDecimal insuranceDeduction =
                driver.getBaseSalary().multiply(INSURANCE_RATE).setScale(0, RoundingMode.HALF_UP);
        BigDecimal unionFeeDeduction =
                driver.getBaseSalary().multiply(UNION_FEE).setScale(0, RoundingMode.HALF_UP);

        Map<String, BigDecimal> deductions = Map.of(
                "Insurance", insuranceDeduction,
                "Union Fee", unionFeeDeduction);
        BigDecimal totalDeductions = deductions.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netSalary = totalIncome.subtract(totalDeductions);
        SalaryCalculationDetailResponse response = SalaryCalculationDetailResponse.builder()
                .reportId(report.getId())
                .driverId(driver.getId())
                .driverName(driver.getUser().getFullName())
                .driverCode(driver.getDriverCode())
                .month(month)
                .totalTrips(
                        routes.stream().mapToInt(r -> r.totalTrips().intValue()).sum())
                .totalIncome(totalIncome)
                .totalDeductions(totalDeductions)
                .netSalary(netSalary)
                .incomes(incomes)
                .salaryByroutes(routes)
                .deductions(deductions)
                .isPaid(report.getIsPaid())
                .paidAt(report.getPaidAt())
                .note(report.getNote())
                .build();
        try {
            redisService.setValue(cacheKey, response);
            redisService.setTimeToLive(cacheKey, 3600);
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi khi ghi Redis cache: " + e.getMessage());
            redisService.delete(cacheKey);
        }
        return response;
    }

    public SalaryCalculationResponse calculateSalary(Long driverId, YearMonth month) {
        if (!month.isBefore(month.plusMonths(1))) {
            throw new AppException(ErrorCode.SALARY_MONTH_NOT_ENDED);
        }
        Driver driver =
                driverRepository.findById(driverId).orElseThrow(() -> new AppException(ErrorCode.DRIVER_NOT_FOUND));
        boolean exists = salaryReportRepository.existsByDriverIdAndReportMonth(driverId, month);
        if (exists) {
            throw new AppException(ErrorCode.SALARY_ALREADY_CALCULATED);
        }
        List<Trip> trips = tripRepository.findCompletedTripsByDriverAndMonth(driverId, month);

        BigDecimal baseSalary = driver.getBaseSalary() != null ? driver.getBaseSalary() : BigDecimal.ZERO;
        Long totalTrips = tripRepository.countTripsByDriverAndMonth(driverId, month);
        Integer totalTripsInt = totalTrips.intValue();
        BigDecimal totalDistance = calculateTotalDistance(trips);

        BigDecimal tripBonus = calculateTripBonus(totalTrips, totalDistance, driver);

        BigDecimal allowance = calculateAllowance(driver, trips, totalDistance);

        BigDecimal deduction = calculateDeduction(driver, trips, baseSalary);

        BigDecimal totalSalary = baseSalary.add(tripBonus).add(allowance).subtract(deduction);

        SalaryReport report = SalaryReport.builder()
                .reportMonth(month)
                .driver(driver)
                .totalTrips(totalTripsInt)
                .totalDistance(totalDistance)
                .baseSalary(baseSalary)
                .tripBonus(tripBonus)
                .allowance(allowance)
                .deduction(deduction)
                .totalSalary(totalSalary)
                .isPaid(false)
                .build();
        salaryReportRepository.save(report);
        return SalaryCalculationResponse.builder()
                .driverId(driverId)
                .driverName(driver.getUser().getFullName())
                .month(month)
                .baseSalary(baseSalary)
                .totalTrips(totalTripsInt)
                .totalDistance(totalDistance)
                .tripBonus(tripBonus)
                .allowance(allowance)
                .deduction(deduction)
                .totalSalary(totalSalary)
                .build();
    }

    public List<SalaryCalculationResponse> calculateSalaryForAllDrivers(YearMonth month) {
        List<Driver> activeDrivers = driverRepository.findAllActiveDriversExcludePaid(month);

        return activeDrivers.stream()
                .map(driver -> calculateSalary(driver.getId(), month))
                .toList();
    }

    public void markAsPaid(Long salaryReportId) {
        SalaryReport report = salaryReportRepository
                .findById(salaryReportId)
                .orElseThrow(() -> new AppException(ErrorCode.SALARY_REPORT_NOT_FOUND));

        report.setIsPaid(true);
        report.setPaidAt(java.time.LocalDateTime.now());
        salaryReportRepository.save(report);
    }
    // Tính tổng quãng đường
    private BigDecimal calculateTotalDistance(List<Trip> trips) {
        return trips.stream()
                .filter(trip -> trip.getStatus() == TripStatus.ARRIVED)
                .filter(trip -> trip.getApprovalStatus() == Boolean.TRUE)
                .filter(trip -> trip.getRoute() != null && trip.getRoute().getDistanceKm() != null)
                .map(trip -> trip.getRoute().getDistanceKm())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    // Tính phụ cấp
    private BigDecimal calculateAllowance(Driver driver, List<Trip> trips, BigDecimal totalDistance) {
        BigDecimal totalAllowance = BigDecimal.ZERO;

        if (driver.getYearsOfExperience() != null && driver.getYearsOfExperience() >= 5) {
            totalAllowance = totalAllowance.add(RESPONSIBILITY_ALLOWANCE);
        }

        boolean hasSafetyIssue = trips.stream()
                .anyMatch(trip -> trip.getStatus() == TripStatus.CANCELLED
                        && trip.getCancelledByUser() != null
                        && trip.getDriver() != null
                        && trip.getCancelledByUser()
                                .getId()
                                .equals(trip.getDriver().getUser().getId()));

        if (!hasSafetyIssue && trips.size() > 0) {
            totalAllowance = totalAllowance.add(SAFETY_BONUS);
        }

        // Phụ cấp đường xa (nếu có chuyến > 500km)
        long longDistanceTrips = trips.stream()
                .filter(trip -> trip.getRoute() != null
                        && trip.getRoute().getDistanceKm().compareTo(new BigDecimal("500")) > 0)
                .count();

        if (longDistanceTrips > 0) {
            BigDecimal longDistanceAllowance = new BigDecimal("200000").multiply(new BigDecimal(longDistanceTrips));
            totalAllowance = totalAllowance.add(longDistanceAllowance);
        }

        return totalAllowance;
    }
    // Tính khấu trừ
    private BigDecimal calculateDeduction(Driver driver, List<Trip> trips, BigDecimal baseSalary) {
        BigDecimal totalDeduction = BigDecimal.ZERO;

        BigDecimal insuranceDeduction = baseSalary.multiply(INSURANCE_RATE).setScale(0, RoundingMode.HALF_UP);
        totalDeduction = totalDeduction.add(insuranceDeduction);

        long lateTrips = trips.stream()
                .filter(trip -> trip.getActualArrivalTime() != null
                        && trip.getEstimatedArrivalTime() != null
                        && trip.getActualArrivalTime()
                                .isAfter(trip.getEstimatedArrivalTime().plusHours(2)))
                .count();

        if (lateTrips > 0) {
            BigDecimal latePenalty = new BigDecimal("200000").multiply(new BigDecimal(lateTrips));
            totalDeduction = totalDeduction.add(latePenalty);
        }

        return totalDeduction;
    }

    // Tính thưởng chuyến đi
    private BigDecimal calculateTripBonus(Long totalTrips, BigDecimal totalDistance, Driver driver) {
        BigDecimal tripRate = getTripRate(driver);
        BigDecimal distanceRate = getDistanceRate(driver);

        BigDecimal bonusFromTrips = tripRate.multiply(new BigDecimal(totalTrips));
        BigDecimal bonusFromDistance = distanceRate.multiply(totalDistance);

        return bonusFromTrips.add(bonusFromDistance);
    }
    // Lấy đơn giá thưởng theo chuyến
    private BigDecimal getTripRate(Driver driver) {
        // Có thể tùy chỉnh theo hạng bằng lái
        String licenseClass = driver.getLicenseClass();
        if (licenseClass != null) {
            switch (licenseClass) {
                case "D":
                case "E":
                    return new BigDecimal("500000");
                case "C":
                    return new BigDecimal("400000");
                default:
                    return DEFAULT_TRIP_RATE;
            }
        }
        return DEFAULT_TRIP_RATE;
    }
    // Lấy đơn giá thưởng theo km
    private BigDecimal getDistanceRate(Driver driver) {
        Integer experience = driver.getYearsOfExperience();
        if (experience != null && experience >= 10) {
            return new BigDecimal("2500");
        }
        return DEFAULT_DISTANCE_RATE;
    }
}
