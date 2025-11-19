package com.transport.service.salary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.dto.route.RouteBySalary;
import com.transport.dto.salary.SalaryCalculationDetailResponse;
import com.transport.dto.salary.SalaryCalculationResponse;
import com.transport.entity.domain.*;
import com.transport.enums.TripStatus;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.driver.DriverRepository;
import com.transport.repository.route.RouteRepository;
import com.transport.repository.salary.SalaryReportRepository;
import com.transport.repository.trip.TripRepository;
import com.transport.service.redis.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryServiceTest {

    @Mock private SalaryReportRepository salaryReportRepository;
    @Mock private TripRepository tripRepository;
    @Mock private DriverRepository driverRepository;
    @Mock private RouteRepository routeRepository;
    @Mock private RedisService<String, Object, Object> redisService;

    @Spy private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private SalaryCalculationServiceImpl salaryCalculationService;

    private Driver driver;
    private final YearMonth month = YearMonth.of(2025, 10);
    private Trip trip1, trip2;
    private SalaryReport report;

    @BeforeEach
    void setUp() {
        // User
        User user = new User();
        user.setId(1L);
        user.setFullName("Nguyễn Văn A");

        // Driver
        driver = new Driver();
        driver.setId(100L);
        driver.setUser(user);
        driver.setDriverCode("DRV001");
        driver.setBaseSalary(new BigDecimal("10000000"));
        driver.setYearsOfExperience(8);
        driver.setLicenseClass("D");

        // Route cho trip
        Route route1 = new Route();
        route1.setDistanceKm(new BigDecimal("400"));

        Route route2 = new Route();
        route2.setDistanceKm(new BigDecimal("600"));

        // Trip 1: đúng giờ, 400km
        trip1 = new Trip();
        trip1.setId(1L);
        trip1.setStatus(TripStatus.ARRIVED);
        trip1.setApprovalStatus(true);
        trip1.setEstimatedArrivalTime(LocalDateTime.now().minusHours(3));
        trip1.setActualArrivalTime(LocalDateTime.now().minusHours(1));
        trip1.setDriver(driver);
        trip1.setRoute(route1);

        // Trip 2: trễ 3 tiếng, 600km (chuyến xa)
        trip2 = new Trip();
        trip2.setId(2L);
        trip2.setStatus(TripStatus.ARRIVED);
        trip2.setApprovalStatus(true);
        trip2.setEstimatedArrivalTime(LocalDateTime.now().minusHours(5));
        trip2.setActualArrivalTime(trip2.getEstimatedArrivalTime().plusHours(3));
        trip2.setDriver(driver);
        trip2.setRoute(route2);

        // SalaryReport
        report = SalaryReport.builder()
                .id(999L)
                .driver(driver)
                .reportMonth(month)
                .isPaid(false)
                .build();
    }

    @Test
    @DisplayName("calculateSalaryDetail - Lấy từ cache thành công")
    void calculateSalaryDetail_FromCache() {
        String cacheKey = "salary:detail:999";
        SalaryCalculationDetailResponse cached = SalaryCalculationDetailResponse.builder()
                .reportId(999L)
                .netSalary(new BigDecimal("25000000"))
                .build();

        when(redisService.get(cacheKey)).thenReturn(cached);

        SalaryCalculationDetailResponse result = salaryCalculationService.calculateSalaryDetail(999L);

        assertEquals(new BigDecimal("25000000"), result.getNetSalary());
        verify(redisService).get(cacheKey);
        verify(salaryReportRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("calculateSalaryDetail - Cache lỗi → tính mới và lưu cache")
    void calculateSalaryDetail_CacheError_CalculateAndCache() {
        when(redisService.get(anyString())).thenThrow(new RuntimeException("Redis down"));
        when(salaryReportRepository.findById(999L)).thenReturn(Optional.of(report));
        when(driverRepository.findById(100L)).thenReturn(Optional.of(driver));
        when(routeRepository.findRoutesByDriverAndMonth(100L, month))
                .thenReturn(List.of(new RouteBySalary("HN-HCM", 2L, new BigDecimal("1000"), null)));

        SalaryCalculationDetailResponse result = salaryCalculationService.calculateSalaryDetail(999L);

        assertNotNull(result);
        assertEquals(100L, result.getDriverId());

        verify(redisService).delete("salary:detail:999");
        verify(redisService).setValue(eq("salary:detail:999"), any());
        verify(redisService).setTimeToLive(eq("salary:detail:999"), eq(3600L));
    }

    @Test
    @DisplayName("calculateSalary - Thành công (hạng D, 8 năm kinh nghiệm)")
    void calculateSalary_Success() {
        when(driverRepository.findById(100L)).thenReturn(Optional.of(driver));
        when(salaryReportRepository.existsByDriverIdAndReportMonth(100L, month)).thenReturn(false);
        when(tripRepository.findCompletedTripsByDriverAndMonth(100L, month)).thenReturn(List.of(trip1, trip2));
        when(tripRepository.countTripsByDriverAndMonth(100L, month)).thenReturn(2L);

        SalaryCalculationResponse result = salaryCalculationService.calculateSalary(100L, month);

        assertEquals(2, result.getTotalTrips());
        assertEquals(new BigDecimal("3000000"), result.getTripBonus()); // 500k x 2 + 2k x 1000km
        assertEquals(new BigDecimal("1700000"), result.getAllowance()); // 500k + 1tr + 200k
        assertTrue(result.getDeduction().compareTo(new BigDecimal("1250000")) >= 0); // BH + phạt trễ
        verify(salaryReportRepository).save(any(SalaryReport.class));
    }

    @Test
    @DisplayName("calculateSalary - Tháng chưa kết thúc → ném lỗi")
    void calculateSalary_MonthNotEnded() {
        YearMonth future = YearMonth.now();

        AppException ex = assertThrows(AppException.class,
                () -> salaryCalculationService.calculateSalary(100L, future));

        assertEquals(ErrorCode.SALARY_MONTH_NOT_ENDED, ex.getErrorCode());
    }

    @Test
    @DisplayName("calculateSalary - Đã tính lương rồi → ném lỗi")
    void calculateSalary_AlreadyCalculated() {
        when(driverRepository.findById(100L)).thenReturn(Optional.of(driver));
        when(salaryReportRepository.existsByDriverIdAndReportMonth(100L, month)).thenReturn(true);

        AppException ex = assertThrows(AppException.class,
                () -> salaryCalculationService.calculateSalary(100L, month));

        assertEquals(ErrorCode.SALARY_ALREADY_CALCULATED, ex.getErrorCode());
    }

    @Test
    @DisplayName("calculateAllowance - Đủ điều kiện: trách nhiệm + an toàn + đường xa")
    void calculateAllowance_AllBonuses() {
        driver.setYearsOfExperience(6);
        BigDecimal allowance = salaryCalculationService.calculateAllowance(driver, List.of(trip1, trip2), new BigDecimal("1000"));

        assertEquals(new BigDecimal("1700000"), allowance); // 500k + 1tr + 200k
    }

    @Test
    @DisplayName("calculateAllowance - Mất thưởng an toàn vì tự hủy chuyến")
    void calculateAllowance_NoSafetyBonus() {
        trip1.setStatus(TripStatus.CANCELLED);
        trip1.setCancelledByUser(driver.getUser());

        BigDecimal allowance = salaryCalculationService.calculateAllowance(driver, List.of(trip1), BigDecimal.ZERO);

        // Chỉ có phụ cấp trách nhiệm (8 năm >= 5 → 500k), mất thưởng an toàn
        assertEquals(new BigDecimal("500000"), allowance);
    }

    @Test
    @DisplayName("calculateDeduction - Có phạt trễ + bảo hiểm")
    void calculateDeduction_WithLatePenalty() {
        BigDecimal deduction = salaryCalculationService.calculateDeduction(
                driver, List.of(trip2), new BigDecimal("10000000"));

        // BH 10.5% = 1.05tr + phạt trễ 200k = 1.25tr
        assertEquals(new BigDecimal("1250000"), deduction);
    }

    @Test
    @DisplayName("getTripRate - Hạng D/E → 500k/chuyến")
    void getTripRate_ClassDE() {
        driver.setLicenseClass("D");
        assertEquals(new BigDecimal("500000"), salaryCalculationService.getTripRate(driver));

        driver.setLicenseClass("E");
        assertEquals(new BigDecimal("500000"), salaryCalculationService.getTripRate(driver));
    }

    @Test
    @DisplayName("getDistanceRate - >=10 năm kinh nghiệm → 2500/km")
    void getDistanceRate_Experienced() {
        driver.setYearsOfExperience(10);
        assertEquals(new BigDecimal("2500"), salaryCalculationService.getDistanceRate(driver));

        driver.setYearsOfExperience(9);
        assertEquals(new BigDecimal("2000"), salaryCalculationService.getDistanceRate(driver));
    }

    @Test
    @DisplayName("markAsPaid - Thành công")
    void markAsPaid_Success() {
        when(salaryReportRepository.findById(999L)).thenReturn(Optional.of(report));

        salaryCalculationService.markAsPaid(999L);

        assertTrue(report.getIsPaid());
        assertNotNull(report.getPaidAt());
        verify(salaryReportRepository).save(report);
    }
}