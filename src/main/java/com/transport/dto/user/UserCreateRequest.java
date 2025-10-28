package com.transport.dto.user;

import java.time.LocalDate;
import java.util.Set;

import com.transport.dto.driver.DriverRequest;
import com.transport.enums.UserRole;
import com.transport.validation.ValidEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @NotBlank(message = "USER_USERNAME_EMPTY")
    @Size(max = 50, message = "USER_USERNAME_TOO_LONG")
    String username;

    @NotBlank(message = "USER_PASSWORD_EMPTY")
    @Size(min = 6, max = 100, message = "USER_PASSWORD_INVALID_LENGTH")
    String password;

    @Pattern(regexp = "\\d{9,12}", message = "USER_PHONE_INVALID")
    String phone;

    @NotBlank(message = "USER_FULLNAME_EMPTY")
    String fullName;

    @Pattern(regexp = "\\d{9,12}", message = "USER_IDENTITY_INVALID")
    String idNumber;

    @Past(message = "USER_BIRTHDATE_INVALID")
    LocalDate dateOfBirth;

    String address;

    Boolean isActive;
    
    @NotNull(message = "ROLE_NOT_NULL")
    @ValidEnum(enumClass = UserRole.class, message = "USER_ROLE_INVALID")
    UserRole role;
    
    @Valid
    DriverRequest driver;

    Set<String> roles;
    
}
