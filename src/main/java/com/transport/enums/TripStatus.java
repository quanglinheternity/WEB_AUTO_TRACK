package com.transport.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TripStatus {
    NOT_STARTED("Chưa khởi hành"),
    IN_PROGRESS("Đang vận chuyển"),
    PAUSED("Tạm dừng"),
    ARRIVED("Đã đến nơi"),
    CANCELLED("Đã hủy chuyến");

    private final String description;
}
