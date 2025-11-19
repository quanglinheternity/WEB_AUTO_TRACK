package com.transport.controller.user;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.dto.driver.DriverRequest;
import com.transport.dto.driver.DriverResponse;
import com.transport.dto.permission.PermissionResponse;
import com.transport.dto.role.RoleResponse;
import com.transport.dto.user.UserCreateRequest;
import com.transport.dto.user.UserDetailResponse;
import com.transport.enums.EmploymentStatus;
import com.transport.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserCreateRequest request;
    private UserDetailResponse response;
    private RoleResponse roleManager;

    @BeforeEach
    void initData() {

        DriverRequest driverReq = DriverRequest.builder()
                .licenseNumber("A12345")
                .licenseClass("B2")
                .licenseIssueDate(Date.valueOf("2020-01-01"))
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
                .dateOfBirth(LocalDate.of(1990, 1, 23))
                .address("Ba Vì")
                .isActive(true)
                .isDriver(true)
                .driver(driverReq)
                .roles(Set.of("ADMIN", "DRIVER"))
                .build();

        DriverResponse driverResponse = new DriverResponse(
                1L,
                "DRV001",
                "A12345",
                "B2",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2030, 1, 1),
                3,
                EmploymentStatus.ACTIVE,
                BigDecimal.valueOf(15_000_000),
                "Good driver");

        roleManager = new RoleResponse(
                "MANAGER",
                "MANAGER role",
                Set.of(
                        new PermissionResponse("USER_CREATE", "Create user"),
                        new PermissionResponse("USER_DELETE", "Delete user")));
        RoleResponse roleDriver = new RoleResponse(
                "DRIVER", "Driver role", Set.of(new PermissionResponse("DRIVE_VEHICLE", "Drive vehicle")));

        response = new UserDetailResponse(
                1L,
                "QuangLinh",
                "0987654321",
                "Quang Linh",
                "123456789",
                LocalDate.of(1990, 1, 23),
                "Ba Vì",
                "avatar.png",
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                driverResponse,
                Set.of(roleDriver));
    }

    @Test
    @DisplayName("Tạo người dùng thành công, với vai trò là tài xế")
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void createUser_WithDriverRole_Success() throws Exception {

        // GIVEN
        String json = objectMapper.writeValueAsString(request);

        Mockito.when(userService.create(any())).thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Tạo người dùng thành công"))
                .andExpect(jsonPath("$.data.username").value("QuangLinh"))
                .andExpect(jsonPath("$.data.driver.licenseNumber").value("A12345"))
                .andExpect(jsonPath("$.data.roles[?(@.name=='DRIVER')]").exists())
                .andExpect(jsonPath("$.data.driver.driverCode").value("DRV001"));

        Mockito.verify(userService, Mockito.times(1)).create(any());
    }

    @Test
    @DisplayName("Tạo người dùng với username >50")
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void createUser_usernameInvalid_Success() throws Exception {
        request.setUsername("QuangLinhQuangLinhQuangLinhQuangLinhQuangLinhLinhQuangLinhLinhQuangLinh12");
        // GIVEN
        String json = objectMapper.writeValueAsString(request);

        // WHEN + THEN
        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dữ liệu không hợp lệ"))
                .andExpect(jsonPath("$.errors.username").value("Tài khoản không được vượt quá 50 ký tự"));
    }

    @Test
    @DisplayName("Password quá ngắn (<6 ký tự) - validate")
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void testPasswordTooShort() throws Exception {
        request.setPassword("123"); // <6
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").value("Mật khẩu phải từ 6 đến 100 ký tự"));
    }

    @Test
    @DisplayName("Phone không hợp lệ - validate")
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void testPhoneInvalid() throws Exception {
        request.setPhone("abc123"); // invalid
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.phone").value("Số điện thoại phải từ 9 đến 12 chữ số"));
    }

    @Test
    @DisplayName("Fullname trống - validate")
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void testFullnameEmpty() throws Exception {
        request.setFullName(""); // empty
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.fullName").value("Họ tên không được để trống"));
    }

    @Test
    @DisplayName("ID number không hợp lệ - validate")
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void testIdNumberInvalid() throws Exception {
        request.setIdNumber("abc"); // invalid
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.idNumber").value("CMND/CCCD phải từ 9 đến 12 chữ số"));
    }

    @Test
    @DisplayName("Ngày sinh tương lai - validate")
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void testDateOfBirthFuture() throws Exception {
        request.setDateOfBirth(LocalDate.now().plusDays(1)); // future date
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.dateOfBirth").value("Ngày sinh phải là ngày trong quá khứ"));
    }

    @Test
    @DisplayName("isDriver null - validate")
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void testIsDriverNull() throws Exception {
        request.setIsDriver(null);
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.isDriver").value("Bạn phải chọn là tài xế hay không"));
    }

    @Test
    @DisplayName("Roles null - validate")
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void testRolesNull() throws Exception {
        request.setRoles(null);
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.roles").value("Quyền người dùng không được để trống"));
    }

    @Test
    @DisplayName("Tạo người dùng thành công, với vai trò không là tài xế")
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void createUser_WithNoDriverRole_Success() throws Exception {
        UserCreateRequest requestNoDriver = request.toBuilder()
                .isDriver(false)
                .driver(null)
                .roles(Set.of("MANAGER"))
                .build();
        // GIVEN
        String json = objectMapper.writeValueAsString(requestNoDriver);
        UserDetailResponse responseNoDriver = new UserDetailResponse(
                1L,
                "QuangLinh",
                "0987654321",
                "Quang Linh",
                "123456789",
                LocalDate.of(1990, 1, 23),
                "Ba Vì",
                "avatar.png",
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                Set.of(roleManager));
        Mockito.when(userService.create(any())).thenReturn(responseNoDriver);

        // WHEN + THEN
        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Tạo người dùng thành công"))
                .andExpect(jsonPath("$.data.username").value("QuangLinh"))
                .andExpect(jsonPath("$.data.roles[?(@.name=='MANAGER')]").exists())
                .andExpect(jsonPath("$.data.driver").doesNotExist());
        Mockito.verify(userService, Mockito.times(1)).create(any());
    }
}
