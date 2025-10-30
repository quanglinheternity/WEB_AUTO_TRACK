package com.transport.service.authentication.auth;

import com.nimbusds.jose.JOSEException;
import com.transport.dto.authentication.auth.AuthenticationRequest;
import com.transport.dto.authentication.auth.AuthenticationResponse;
import com.transport.dto.authentication.introspect.IntrospectRequest;
import com.transport.dto.authentication.introspect.IntrospectResponse;
import com.transport.dto.authentication.logout.LogoutRequest;
import com.transport.dto.authentication.refresh.RefreshRequest;
import com.transport.entity.domain.User;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
    User getCurrentUser();
    Long getCurrentUserId();
    boolean hasRole(String roleName);
    boolean hasPermission(String permissionCode);
    void requirePermission(String permissionCode);
}