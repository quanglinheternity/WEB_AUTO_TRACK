package com.transport.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExpenseGroup {
    FUEL("Chi phí nhiên liệu (xăng, dầu, điện, v.v.)"),
    TOLL("Phí đường bộ, cầu, phà, cao tốc"),
    PARKING("Chi phí gửi xe, bãi đỗ"),
    MAINTENANCE("Chi phí bảo dưỡng, sửa chữa phương tiện"),
    MEAL("Chi phí ăn uống trong chuyến đi"),
    ACCOMMODATION("Chi phí lưu trú, khách sạn, nhà nghỉ"),
    ADMINISTRATIVE("Chi phí hành chính, văn phòng, giấy tờ"),
    OTHER("Chi phí khác không thuộc các nhóm trên");

    private final String description;
}
