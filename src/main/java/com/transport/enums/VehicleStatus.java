package com.transport.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VehicleStatus {
    AVAILABLE("Sẵn sàng"),
    IN_USE("Đang sử dụng"),
    MAINTENANCE("Đang bảo trì"),
    BROKEN("Hỏng hóc"),
    INACTIVE("Không hoạt động");

    private final String description;

}
