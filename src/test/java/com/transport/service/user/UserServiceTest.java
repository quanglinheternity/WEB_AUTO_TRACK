package com.transport.service.user;

import com.transport.dto.driver.DriverRequest;
import com.transport.dto.user.UserCreateRequest;
import com.transport.dto.user.UserDetailResponse;
import com.transport.entity.domain.Driver;
import com.transport.entity.domain.Role;
import com.transport.entity.domain.User;
import com.transport.enums.EmploymentStatus;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.mapper.DirverMapper;
import com.transport.mapper.UserMapper;
import com.transport.repository.driver.DriverRepository;
import com.transport.repository.role.RoleRepository;
import com.transport.repository.user.UserRepository;
import com.transport.service.authentication.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private DriverRepository driverRepository;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private DirverMapper driverMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private UserCreateRequest request;
    private Role roleAdmin, roleDriver;
    private Driver driverEntity;
    private User userEntity;

    @BeforeEach
    void init() {

        DriverRequest driverReq = DriverRequest.builder()
                .licenseNumber("A12345")
                .licenseClass("B2")
                .licenseIssueDate(Date.valueOf("2024-01-01"))
                .licenseExpiryDate(Date.valueOf("2030-01-01"))
                .yearsOfExperience(3)
                .employmentStatus(EmploymentStatus.ACTIVE)
                .baseSalary(BigDecimal.valueOf(15_000_000))
                .note("Good driver")
                .build();

        request = UserCreateRequest.builder()
                .username("QuangLinh")
                .password("12345678")
                .phone("0987654321")
                .fullName("Quang Linh")
                .idNumber("123456789")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Ba Vì")
                .isActive(true)
                .isDriver(true)
                .driver(driverReq)
                .roles(Set.of("DRIVER"))
                .build();

        roleAdmin = Role.builder()
                .roleName("ADMIN")
                .description("ADMIN Role")
                .build();

        roleDriver = Role.builder()
                .roleName("DRIVER")
                .description("Driver role")
                .build();

        driverEntity = Driver.builder()
                .licenseNumber("A12345")
                .licenseClass("B2")
                .build();

        userEntity = User.builder()
                .username("QuangLinh")
                .phone("0987654321")
                .fullName("Quang Linh")
                .idNumber("123456789")
                .isActive(true)
                .roles(Set.of(roleDriver))
                .build();
    }

    // CASE 1: ADMIN tạo user thành công
    @Test
    void createUser_admin_success() {

        Mockito.when(authenticationService.hasRole("ADMIN")).thenReturn(true);

        Mockito.when(roleRepository.findAllById(any()))
                .thenReturn(List.of(roleDriver));

        Mockito.when(userMapper.toCreateEntity(any()))
                .thenReturn(userEntity);

        Mockito.when(passwordEncoder.encode(anyString()))
                .thenReturn("encoded");

        Mockito.when(userRepository.save(any()))
                .thenReturn(userEntity);

        Mockito.when(driverRepository.existsBylicenseNumber("A12345"))
                .thenReturn(false);

        Mockito.when(driverMapper.toDriverFromCreateRequest(any()))
                .thenReturn(driverEntity);

        Mockito.when(driverRepository.save(any()))
                .thenReturn(driverEntity);

        Mockito.when(userMapper.toDetailResponse(any()))
                .thenReturn(
                        new UserDetailResponse(
                                1L,
                                "QuangLinh",
                                "0987654321",
                                "Quang Linh",
                                "123456789",
                                LocalDate.of(2000, 1, 1),
                                "Ba Vì",
                                null,
                                true,
                                null, null,
                                null,
                                Set.of()
                        )
                );

        UserDetailResponse res = userService.create(request);

        assertNotNull(res);
        assertEquals("QuangLinh", res.username());
        assertTrue(res.isActive());
    }

    // CASE 2: MANAGER không được tạo ADMIN → bị chặn
    @Test
    void createUser_managerCreateAdmin_fail() {

        request.setRoles(Set.of("ADMIN"));

        Mockito.when(authenticationService.hasRole("ADMIN")).thenReturn(false);
        Mockito.when(authenticationService.hasRole("MANAGER")).thenReturn(true);

        Mockito.when(roleRepository.findAllById(any()))
                .thenReturn(List.of(roleAdmin));

        AppException ex = assertThrows(AppException.class,
                () -> userService.create(request));

        assertEquals(ErrorCode.ACCESS_DENIED_CREATE_USER, ex.getErrorCode());
    }

    // CASE 3: Driver license bị trùng → lỗi
    @Test
    void createUser_duplicateLicense_fail() {

        Mockito.when(authenticationService.hasRole("ADMIN")).thenReturn(true);

        Mockito.when(roleRepository.findAllById(any()))
                .thenReturn(List.of(roleDriver));

        Mockito.when(userMapper.toCreateEntity(any()))
                .thenReturn(userEntity);

        Mockito.when(passwordEncoder.encode(anyString()))
                .thenReturn("encoded");

        Mockito.when(userRepository.save(any()))
                .thenReturn(userEntity);

        Mockito.when(driverRepository.existsBylicenseNumber("A12345"))
                .thenReturn(true);

        AppException ex = assertThrows(AppException.class,
                () -> userService.create(request));

        assertEquals(ErrorCode.DRIVER_ALREADY_LICENSE_EXISTS, ex.getErrorCode());
        assertEquals(ErrorCode.DRIVER_ALREADY_LICENSE_EXISTS.getMessage(), ex.getMessage());
    }

    // CASE 4: Không phải ADMIN/MANAGER → bị chặn
    @Test
    void createUser_noPermission_fail() {

        Mockito.when(authenticationService.hasRole("ADMIN")).thenReturn(false);
        Mockito.when(authenticationService.hasRole("MANAGER")).thenReturn(false);

        AppException ex = assertThrows(AppException.class,
                () -> userService.create(request));

        assertEquals(ErrorCode.ACCESS_DENIED_CREATE_USER, ex.getErrorCode());
    }

    // CASE 5: isDriver = false → không tạo Driver
    @Test
    void createUser_noDriver_success() {

        request.setIsDriver(false);
        request.setDriver(null);

        Mockito.when(authenticationService.hasRole("ADMIN")).thenReturn(true);

        Mockito.when(roleRepository.findAllById(any()))
                .thenReturn(List.of(roleDriver));

        Mockito.when(userMapper.toCreateEntity(any()))
                .thenReturn(userEntity);

        Mockito.when(passwordEncoder.encode(anyString()))
                .thenReturn("encoded");

        Mockito.when(userRepository.save(any()))
                .thenReturn(userEntity);

        Mockito.when(userMapper.toDetailResponse(any()))
                .thenReturn(
                        new UserDetailResponse(
                                1L,
                                "QuangLinh",
                                "0987654321",
                                "Quang Linh",
                                "123456789",
                                LocalDate.of(2000, 1, 1),
                                "Ba Vì",
                                null,
                                true,
                                null, null,
                                null,
                                Set.of()
                        )
                );

        UserDetailResponse res = userService.create(request);

        assertNotNull(res);
        assertTrue(res.isActive());
    }
}
