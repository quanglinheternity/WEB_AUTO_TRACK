package com.transport.service.user;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.transport.dto.driver.DriverRequest;
import com.transport.dto.user.UserCreateRequest;
import com.transport.dto.user.UserDetailResponse;
import com.transport.dto.user.UserResponse;
import com.transport.entity.domain.Driver;
import com.transport.entity.domain.Role;
import com.transport.entity.domain.User;
import com.transport.enums.EmploymentStatus;
import com.transport.enums.UserRole;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.mapper.DirverMapper;
import com.transport.mapper.UserMapper;
import com.transport.repository.driver.DriverRepository;
import com.transport.repository.role.RoleRepository;
import com.transport.repository.user.UserRepository;
import com.transport.util.CodeGenerator;

import jakarta.transaction.Transactional;
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
    
    @Override
    public Page<UserResponse> getAll(String keyword, Pageable pageable) {
        // return nguoiDungRepository.findAll();
        Page<UserResponse> page = userRepository.searchUsers(keyword, pageable);

        return page;
    }
    @Override
    public UserDetailResponse getById(Long id) {
        User nguoiDung = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDetailResponse(nguoiDung);
    }
    @Override
    public UserDetailResponse create(UserCreateRequest request) {
        userValidator.validateBeforeCreate(request);

        User user = userMapper.toCreateEntity(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);
        user = userRepository.save(user);

        // Nếu là tài xế → tạo thông tin tài xế
        if (UserRole.DRIVER.equals(request.getRole()) && request.getDriver() != null) {
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
        userMapper.updateFromRequest(request, user);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
         // Nếu vai trò là tài xế, tạo thêm thông tin tài xế
        if (UserRole.DRIVER.equals(request.getRole()) || UserRole.DRIVER.equals(user.getRole())) {
            DriverRequest driverRequest = request.getDriver();
            if (driverRequest  == null) {
                throw new AppException(ErrorCode.DRIVER_ALREADY_EMPTY);
            }
            
            Driver driver = user.getDriver();
            if (driver == null) {
                driver = driverMapper.toDriverFromCreateRequest(driverRequest);
                driver.setUser(user);
                driver.setDriverCode(CodeGenerator.generateCode("TX"));
            }else {
                driverMapper.updateDriverFromRequest(driverRequest, driver);
            }
            
            driver = driverRepository.save(driver);
            
            user.setDriver(driver);
            
        }else {
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

    public void delete(Long id){
        User user = userValidator.validateAndGetExistingUser(id);
        if (user.getDriver() != null) {
            driverRepository.delete(user.getDriver());
        }
        userRepository.deleteById(id);
    }
}
