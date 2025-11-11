package com.transport.service.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.transport.dto.driver.DriverRequest;
import com.transport.dto.page.PageResponse;
import com.transport.dto.user.UserCreateRequest;
import com.transport.dto.user.UserDetailResponse;
import com.transport.dto.user.UserResponse;
import com.transport.dto.user.UserSearchRequest;
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
import com.transport.util.CodeGenerator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    UserValidator userValidator;

    RoleRepository roleRepository;
    DirverMapper driverMapper;
    DriverRepository driverRepository;

    AuthenticationService authenticationService;

    @Override
    public PageResponse<UserResponse> getAll(UserSearchRequest request, Pageable pageable) {
        // return nguoiDungRepository.findAll();
        Page<UserResponse> page = userRepository.searchUsers(request, pageable);

        return PageResponse.from(page);
    }

    @Override
    public UserDetailResponse getById(Long id) {
        User targetUser = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (authenticationService.hasRole("ADMIN")) return userMapper.toDetailResponse(targetUser);

        if (authenticationService.hasRole("MANAGER")
                && "ADMIN".equals(targetUser.getRoles().iterator().next().getRoleName()))
            throw new AppException(ErrorCode.ACCESS_DENIED);

        if ((authenticationService.hasRole("ACCOUNTANT") || authenticationService.hasRole("DRIVER"))
                && !authenticationService.getCurrentUserId().equals(id))
            throw new AppException(ErrorCode.ACCESS_DENIED);
        return userMapper.toDetailResponse(targetUser);
    }

    @Override
    public UserDetailResponse create(UserCreateRequest request) {

        // --- PHÂN QUYỀN TẠO USER ---
        // Lấy danh sách role được tạo
        List<Role> rolesToAssign = roleRepository.findAllById(request.getRoles());
        Set<String> roleNames = rolesToAssign.stream().map(Role::getRoleName).collect(Collectors.toSet());

        // Kiểm tra vai trò của người tạo
        if (authenticationService.hasRole("ADMIN")) {
            // ADMIN có thể tạo bất kỳ ai
        } else if (authenticationService.hasRole("MANAGER")) {
            // MANAGER không được tạo ADMIN hoặc MANAGER
            if (roleNames.contains("ADMIN") || roleNames.contains("MANAGER")) {
                throw new AppException(ErrorCode.ACCESS_DENIED_CREATE_USER);
            }
        } else {
            // ACCOUNTANT, DRIVER không được tạo user nào
            throw new AppException(ErrorCode.ACCESS_DENIED_CREATE_USER);
        }
        userValidator.validateBeforeCreate(request);

        User user = userMapper.toCreateEntity(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);
        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        user = userRepository.save(user);

        // Nếu là tài xế → tạo thông tin tài xế
        if (Boolean.TRUE.equals(request.getIsDriver()) && request.getDriver() != null) {
            if (driverRepository.existsBylicenseNumber(request.getDriver().getLicenseNumber())) {
                throw new AppException(ErrorCode.DRIVER_ALREADY_LICENSE_EXISTS);
            }
            DriverRequest ttDriver = request.getDriver();
            Driver driver = driverMapper.toDriverFromCreateRequest(ttDriver);
            driver.setDriverCode(CodeGenerator.generateCode("TX"));
            driver.setEmploymentStatus(EmploymentStatus.ACTIVE);
            driver.setUser(user);
            driverRepository.save(driver);
            user.setDriver(driver);
        }

        return userMapper.toDetailResponse(user);
    }

    public UserDetailResponse update(Long id, UserCreateRequest request) {
        userValidator.validateBeforeUpdate(id, request);
        User user = userValidator.validateAndGetExistingUser(id);
        User currentUser = authenticationService.getCurrentUser();
        if (authenticationService.hasRole("ADMIN")) {
            // ADMIN: full quyền → không giới hạn
        } else if (authenticationService.hasRole("MANAGER")) {
            // MANAGER: không được sửa ADMIN
            boolean targetIsAdmin = user.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getRoleName()));
            if (targetIsAdmin) throw new AppException(ErrorCode.ACCESS_DENIED);
        } else if (authenticationService.hasRole("ACCOUNTANT") || authenticationService.hasRole("DRIVER")) {
            // Chỉ được sửa chính mình
            if (!currentUser.getId().equals(id)) throw new AppException(ErrorCode.ACCESS_DENIED);
        } else {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        userMapper.updateFromRequest(request, user);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        // Nếu vai trò là tài xế, tạo thêm thông tin tài xế
        if (Boolean.TRUE.equals(request.getIsDriver()) && request.getDriver() == null) {
            DriverRequest driverRequest = request.getDriver();
            if (driverRequest == null) {
                throw new AppException(ErrorCode.DRIVER_ALREADY_EMPTY);
            }

            Driver driver = user.getDriver();
            if (driver == null) {
                driver = driverMapper.toDriverFromCreateRequest(driverRequest);
                driver.setUser(user);
                driver.setDriverCode(CodeGenerator.generateCode("TX"));
            } else {
                driverMapper.updateDriverFromRequest(driverRequest, driver);
            }

            driver = driverRepository.save(driver);

            user.setDriver(driver);

        } else {
            if (user.getDriver() != null) {
                driverRepository.delete(user.getDriver());
                user.setDriver(null);
            }
        }
        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        user = userRepository.save(user);
        return userMapper.toDetailResponse(user);
    }

    public void delete(Long id) {
        User user = userValidator.validateAndGetExistingUser(id);
        if (user.getDriver() != null) {
            driverRepository.delete(user.getDriver());
        }
        userRepository.deleteById(id);
    }
}
