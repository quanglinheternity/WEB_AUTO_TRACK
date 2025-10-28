package com.transport.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    String message() default "INVALID_ENUM_VALUE";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // Chỉ định enum nào sẽ được kiểm tra
    Class<? extends Enum<?>> enumClass();
}
