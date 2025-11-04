package com.transport.service.user;

import org.springframework.stereotype.Component;

import com.transport.dto.driver.DriverRequest;
import com.transport.dto.user.UserCreateRequest;
import com.transport.entity.domain.User;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.user.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserValidator {
    UserRepository userRepository;

    public void validateBeforeCreate(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        validateDriverForCreate(request.getIsDriver(), request.getDriver());
    }

    public void validateBeforeUpdate(Long id, UserCreateRequest request) {
        validateAndGetExistingUser(id);
        validateDuplicateUsername(request.getUsername(), id);
        validateDriverForUpdate(request.getIsDriver(), request.getDriver());
    }

    public User validateAndGetExistingUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public void validateDuplicateUsername(String username, Long id) {
        if (userRepository.existsByUsernameAndIdNot(username, id)) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new AppException(ErrorCode.PASSWORD_REQUIRED);
        }
    }

    public void validateDriverForCreate(Boolean isDriver, DriverRequest driverRequest) {
        if (Boolean.TRUE.equals(isDriver) && driverRequest == null) {
            throw new AppException(ErrorCode.DRIVER_ALREADY_EMPTY);
        }
    }

    public void validateDriverForUpdate(Boolean isDriver, DriverRequest driverRequest) {
        if (Boolean.TRUE.equals(isDriver) && driverRequest == null) {
            throw new AppException(ErrorCode.DRIVER_ALREADY_EMPTY);
        }
    }
}
