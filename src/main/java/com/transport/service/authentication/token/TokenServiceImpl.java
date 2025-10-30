package com.transport.service.authentication.token;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import com.transport.entity.domain.User;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.repository.invalidate.InvalidateRepository;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    final InvalidateRepository invalidateRepository;

    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Value("${jwt.vaild-duration}")
    Long VALID_DURATION;

    @Value("${jwt.refresh-duration}")
    Long REFRESH_DURATION;

    @Override
    public String generateToken(User user) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("com.transport")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", buildScope(user))
                    .build();

            JWSObject jwsObject = new JWSObject(header, new Payload(claims.toJSONObject()));
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error generating token", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        Date expiry = isRefresh
                ? Date.from(signedJWT.getJWTClaimsSet().getExpirationTime().toInstant().plus(REFRESH_DURATION, ChronoUnit.SECONDS))
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);
        if (!(verified && expiry.after(new Date()))) {
            throw new AppException(isRefresh ? ErrorCode.TOKEN_EXPIRED : ErrorCode.AUTHENTICATION_FAILED);
        }

        if (invalidateRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        return signedJWT;
    }

    @Override
    public boolean isValidToken(String token) throws JOSEException, ParseException {
        try {
            verifyToken(token, false);
            return true;
        } catch (AppException | ParseException | JOSEException e) {
            log.warn("Invalid token: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error validating token", e);
            return false;
        }
    }
    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getRoleName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> joiner.add(permission.getPermissionName()));
                }
            });
        }
        return joiner.toString();
    }
}