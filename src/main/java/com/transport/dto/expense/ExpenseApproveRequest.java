package com.transport.dto.expense;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpenseApproveRequest {
    Boolean isApproved;
    String reason;
}
