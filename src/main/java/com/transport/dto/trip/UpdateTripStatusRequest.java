package com.transport.dto.trip;

import com.transport.enums.TripStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTripStatusRequest {

    @NotNull(message = "NEW_STATUS_REQUIRED")
    TripStatus newStatus;
    
    String note;
}