package com.transport.dto.trip;

import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApproveTripRequest {
    @NotNull(message = "APPROVAL_REQUIRED")
    Boolean approved;

    String reason;
}
