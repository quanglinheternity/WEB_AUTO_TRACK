package com.transport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS_POST = {
        "/api/v1/users",
        "/api/v1/auth/login",
        "/api/v1/auth/introspect",
        "/api/v1/auth/logout",
        "/api/v1/auth/refresh",
        "/api/v1/auth/{userId}/verify",
        "/api/v1/auth/{userId}/resend",
    };

    @Autowired
    private CustomJWTDecoder jwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS_POST).permitAll()
                        // .requestMatchers(HttpMethod.GET, "/user").hasAnyAuthority(Role.ADMIN.name())
                        // .requestMatchers(HttpMethod.PUT, "/user/{userId}").permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(
                        uauth2 -> uauth2.jwt(jwtConfigurer -> jwtConfigurer
                                        .decoder(jwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // xử lý auth lỗi
                        )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean // dùng để cho file khác gọi với nhau
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOriginPattern("http://localhost:8080");
        corsConfiguration.addAllowedOriginPattern("http://127.0.0.1:5500");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            System.out.println("=== Extracted Authorities from JWT ===");
            authorities.forEach(a -> System.out.println(" -> " + a.getAuthority()));
            return authorities;
        });
        return jwtAuthenticationConverter;
    }
}
