package com.transport.service.authentication.token;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.transport.entity.domain.User;

public interface TokenService {
    String generateToken(User user);
    SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException;
    boolean isValidToken(String token) throws JOSEException, ParseException;
}
