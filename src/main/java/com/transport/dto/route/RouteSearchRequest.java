package com.transport.dto.route;

import lombok.Data;

@Data
public class RouteSearchRequest {
    private String keyword; // tìm theo code, name, origin, destination
    private Boolean isActive; // lọc theo trạng thái hoạt động
}
