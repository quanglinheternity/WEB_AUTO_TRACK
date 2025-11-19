package com.transport.service.trip;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.dto.page.PageResponse;
import com.transport.dto.trip.*;
import com.transport.entity.domain.*;
import com.transport.enums.TripStatus;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.mapper.TripMapper;
import com.transport.repository.trip.TripRepository;
import com.transport.service.authentication.auth.AuthenticationService;
import com.transport.service.redis.RedisService;
import com.transport.util.CodeGenerator;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {
    @Mock
    private TripRepository tripRepository;

    @Mock
    private TripMapper tripMapper;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private RedisService<String, Object, Object> redisService;

    @Mock
    private TripValidator tripValidator;

    @Getter
    @Setter
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private TripServiceImpl tripService;

    private User currentUser;
    private Driver driver;
    private Vehicle vehicle;
    private Route route;
    private Trip trip;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(99L);
        currentUser.setFullName("Admin User");

        User driverUser = new User();
        driverUser.setId(99L);
        driverUser.setFullName("Admin User");
        driver = new Driver();
        driver.setId(100L);
        driver.setUser(driverUser);

        vehicle = new Vehicle();
        vehicle.setId(10L);
        vehicle.setLicensePlate("51H-12345");

        route = new Route();
        route.setId(20L);
        route.setName("HN - HCM");

        trip = new Trip();
        trip.setId(1L);
        trip.setTripCode("LT20250001");
        trip.setStatus(TripStatus.NOT_STARTED);
        trip.setDriver(driver);
        trip.setVehicle(vehicle);
        trip.setRoute(route);
        trip.setCreatedBy(currentUser);
    }

    @Test
    @DisplayName("getAll - Có cache → trả về từ Redis")
    void getAll_FromCache() {
        TripSearchRequest request = new TripSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);
        when(authenticationService.hasPermission("TRIP_READ")).thenReturn(true);
        String cacheKey = "trip:list:null:null:-1:0:10";

        PageResponse<TripResponse> cachedResponse = PageResponse.<TripResponse>builder()
                .content(List.of(new TripResponse()))
                .totalElements(1L)
                .build();

        when(redisService.get(cacheKey)).thenReturn(cachedResponse);

        PageResponse<TripResponse> result = tripService.getAll(request, pageable);
        assertEquals(1L, result.totalElements());
        verify(tripRepository, never()).searchTrips(any(), any());
    }

    @Test
    @DisplayName("getAll - Không có quyền TRIP_READ → chỉ xem chuyến của mình")
    void getAll_NoGlobalPermission_OnlyOwnTrips() {
        TripSearchRequest request = new TripSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);

        when(authenticationService.hasPermission("TRIP_READ")).thenReturn(false);
        when(authenticationService.getCurrentUser()).thenReturn(currentUser);

        Page<Trip> tripsPage = new PageImpl<>(List.of(trip));
        when(tripRepository.searchTrips(any(), eq(pageable))).thenReturn(tripsPage);
        when(tripMapper.toTripResponse(any())).thenReturn(new TripResponse());

        PageResponse<TripResponse> result = tripService.getAll(request, pageable);

        assertEquals(1, result.totalElements());
        verify(tripRepository)
                .searchTrips(
                        argThat(r -> r.getDriverId() != null && r.getDriverId().equals(99L)), eq(pageable));
    }

    @Test
    @DisplayName("createTrip - Thành công")
    void createTrip_Success() {
        TripCreateRequest request = TripCreateRequest.builder()
                .routeId(20L)
                .vehicleId(10L)
                .driverId(100L)
                .departureTime(LocalDateTime.now().plusDays(1))
                .estimatedArrivalTime(LocalDateTime.now().plusDays(2))
                .cargoWeight(new BigDecimal("5000"))
                .build();
        when(authenticationService.getCurrentUser()).thenReturn(currentUser);
        when(tripValidator.validateRoute(20L)).thenReturn(route);
        when(tripValidator.validateVehicle(10L)).thenReturn(vehicle);
        when(tripValidator.validateDriver(100L)).thenReturn(driver);
        when(tripMapper.toCreateTrip(request)).thenReturn(trip);
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);
        when(tripMapper.toTripResponse(trip)).thenReturn(new TripResponse());
        try (MockedStatic<CodeGenerator> mocked = mockStatic(CodeGenerator.class)) {
            mocked.when(() -> CodeGenerator.generateCode("LT")).thenReturn("LT20250001");

            TripResponse response = tripService.createTrip(request);

            assertNotNull(response);
            verify(tripRepository)
                    .save(argThat(t -> t.getTripCode().equals("LT20250001")
                            && t.getStatus() == TripStatus.NOT_STARTED
                            && t.getCreatedBy().equals(currentUser)));
        }
    }

    @Test
    @DisplayName("updateTrip - Chỉ cho phép sửa khi NOT_STARTED")
    void updateTrip_OnlyNotStarted() {
        TripUpdateRequest request = new TripUpdateRequest();
        request.setRouteId(20L);

        trip.setStatus(TripStatus.IN_PROGRESS);

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        AppException ex = assertThrows(AppException.class, () -> tripService.updateTrip(1L, request));
        assertEquals(ErrorCode.ONLY_NOT_STARTED_TRIP_CAN_BE_UPDATED, ex.getErrorCode());
    }

    @Test
    @DisplayName("updateTripStatus - Chuyển sang ARRIVED")
    void updateTripStatus_ToArrived() {
        UpdateTripStatusRequest request = new UpdateTripStatusRequest();
        request.setNewStatus(TripStatus.ARRIVED);

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(tripMapper.toTripResponse(any())).thenReturn(new TripResponse());

        TripResponse result = tripService.updateTripStatus(1L, request);

        assertNotNull(result);
        verify(tripRepository).save(argThat(t -> t.getActualArrivalTime() != null));
    }

    @Test
    @DisplayName("updateTripStatus - Hủy chuyến không có lý do → lỗi")
    void updateTripStatus_CancelWithoutReason_Fail() {
        UpdateTripStatusRequest request = new UpdateTripStatusRequest();
        request.setNewStatus(TripStatus.CANCELLED);
        request.setNote(""); // trống

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        AppException ex = assertThrows(AppException.class, () -> tripService.updateTripStatus(1L, request));

        assertEquals(ErrorCode.CANCELLATION_REASON_REQUIRED, ex.getErrorCode());
    }

    @Test
    @DisplayName("updateTripStatus - Hủy chuyến thành công")
    void updateTripStatus_CancelSuccess() {
        UpdateTripStatusRequest request = new UpdateTripStatusRequest();
        request.setNewStatus(TripStatus.CANCELLED);
        request.setNote("Khách hủy đơn");

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(authenticationService.getCurrentUser()).thenReturn(currentUser);
        when(tripMapper.toTripResponse(any())).thenReturn(new TripResponse());

        tripService.updateTripStatus(1L, request);

        verify(tripRepository)
                .save(argThat(t -> t.getStatus() == TripStatus.CANCELLED
                        && t.getCancelledByUser().equals(currentUser)
                        && t.getCancellationReason().equals("Khách hủy đơn")));
    }

    @Test
    @DisplayName("approveTrip - Duyệt thành công")
    void approveTrip_ApproveSuccess() {
        ApproveTripRequest request = ApproveTripRequest.builder().approved(true).build();

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(authenticationService.getCurrentUser()).thenReturn(currentUser);
        when(tripMapper.toTripResponse(any())).thenReturn(new TripResponse());

        tripService.approveTrip(1L, request);

        verify(tripRepository)
                .save(argThat(t -> t.getApprovalStatus() == Boolean.TRUE
                        && t.getApprovedByUser().equals(currentUser)
                        && t.getNote() == null));
    }

    @Test
    @DisplayName("approveTrip - Từ chối không có lý do → lỗi")
    void approveTrip_RejectWithoutReason_Fail() {
        ApproveTripRequest request =
                ApproveTripRequest.builder().approved(false).reason("").build();

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        AppException ex = assertThrows(AppException.class, () -> tripService.approveTrip(1L, request));

        assertEquals(ErrorCode.REJECTION_REASON_REQUIRED, ex.getErrorCode());
    }

    @Test
    @DisplayName("delete - Chỉ xóa được khi NOT_STARTED")
    void delete_OnlyNotStarted() {
        trip.setStatus(TripStatus.IN_PROGRESS);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        AppException ex = assertThrows(AppException.class, () -> tripService.delete(1L));
        assertEquals(ErrorCode.ONLY_NOT_STARTED_TRIP_CAN_BE_DELETED, ex.getErrorCode());
    }

    @Test
    @DisplayName("delete - Xóa thành công")
    void delete_Success() {
        trip.setStatus(TripStatus.NOT_STARTED);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        tripService.delete(1L);

        verify(tripRepository).delete(trip);
    }

    @Test
    @DisplayName("listAll - Trả về tất cả chuyến")
    void listAll_Success() {
        when(tripRepository.findAll()).thenReturn(List.of(trip));
        when(tripMapper.toTripResponse(trip)).thenReturn(new TripResponse());

        List<TripResponse> result = tripService.listAll();

        assertEquals(1, result.size());
        verify(tripRepository).findAll();
    }

    @Test
    @DisplayName("findReportTripByVehicle - Gọi đúng repo")
    void findReportTripByVehicle() {
        YearMonth month = YearMonth.of(2025, 10);
        TripReport expected = new TripReport(null, 0);
        when(tripRepository.findReportTripByVehicle(10L, month)).thenReturn(expected);

        TripReport result = tripService.findReportTripByVehicle(10L, month);

        assertEquals(expected, result);
    }
}
