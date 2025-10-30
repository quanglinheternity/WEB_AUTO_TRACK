package com.transport.service.authentication.auth;

import java.text.ParseException;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.transport.dto.authentication.auth.AuthenticationRequest;
import com.transport.dto.authentication.auth.AuthenticationResponse;
import com.transport.dto.authentication.introspect.IntrospectRequest;
import com.transport.dto.authentication.introspect.IntrospectResponse;
import com.transport.dto.authentication.logout.LogoutRequest;
import com.transport.dto.authentication.refresh.RefreshRequest;
import com.transport.entity.domain.InvalidatedToken;
import com.transport.entity.domain.User;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.invalidate.InvalidateRepository;
import com.transport.repository.user.UserRepository;
import com.transport.service.authentication.token.TokenService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    InvalidateRepository invalidateRepository;
    PasswordEncoder passwordEncoder;
    TokenService tokenService;
    AuthenticationValidation validation;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        validation.validateLoginRequest(request);

        User user = userRepository.findByName(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.AUTHENTICATION_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
        }

        String token = tokenService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        boolean valid = tokenService.isValidToken(request.getToken());
        return IntrospectResponse.builder().valid(valid).build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT jwt = tokenService.verifyToken(request.getToken(), true);
        Date issueTime = jwt.getJWTClaimsSet().getIssueTime();
        InvalidatedToken invalidated = InvalidatedToken.builder()
                .id(jwt.getJWTClaimsSet().getJWTID())
                .expiryTime(issueTime)
                .build();

        invalidateRepository.save(invalidated);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = tokenService.verifyToken(request.getToken(), true);

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiry = signedJWT.getJWTClaimsSet().getExpirationTime();

        invalidateRepository.save(
                InvalidatedToken.builder().id(jwtId).expiryTime(expiry).build()
        );

        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String newToken = tokenService.generateToken(user);
        return AuthenticationResponse.builder().token(newToken).authenticated(true).build();
    }
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String username = authentication.getName();

        return userRepository.findByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
     public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    @Override
    public boolean hasRole(String roleName) {
        return getCurrentUser().getRole().name().equalsIgnoreCase(roleName);
    }

    @Override
    public boolean hasPermission(String permissionCode) {
        User currentUser = getCurrentUser();
        return currentUser.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(p -> p.getPermissionName().equalsIgnoreCase(permissionCode));
    }
    @Override
    public void requirePermission(String permissionCode) {
        if (!hasPermission(permissionCode)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
    }
}