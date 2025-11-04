package com.transport.service.authentication.auth;

import org.springframework.stereotype.Component;

import com.transport.dto.authentication.auth.AuthenticationRequest;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;

@Component
public class AuthenticationValidation {

    public void validateLoginRequest(AuthenticationRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new AppException(ErrorCode.INVALID_USERNAME);
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
