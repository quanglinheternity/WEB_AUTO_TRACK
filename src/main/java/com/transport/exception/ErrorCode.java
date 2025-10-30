package com.transport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


import lombok.Getter;

@Getter
public enum ErrorCode {
    // General Errors (0xxx)
    UNCATEGORIZED_ERROR(9999, "Lỗi hệ thống, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST(1000, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1000, "Bạn không có quyền truy cập.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1000, "Quyền truy cập khóa", HttpStatus.FORBIDDEN),

    // User-related Errors (1xxx)
    USER_NOT_FOUND(1001, "Người dùng không tồn tại", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(1002, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_USERNAME_EMPTY(1003, "Tài khoản không được để trống", HttpStatus.BAD_REQUEST),
    USER_USERNAME_TOO_LONG(1004, "Tài khoản không được vượt quá 50 ký tự", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_EMPTY(1005, "Mật khẩu không được để trống", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_INVALID_LENGTH(1006, "Mật khẩu phải từ 6 đến 100 ký tự", HttpStatus.BAD_REQUEST),
    USER_EMAIL_INVALID(1007, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_PHONE_INVALID(1008, "Số điện thoại phải từ 9 đến 12 chữ số", HttpStatus.BAD_REQUEST),
    USER_FULLNAME_EMPTY(1009, "Họ tên không được để trống", HttpStatus.BAD_REQUEST),
    USER_IDENTITY_INVALID(1010, "CMND/CCCD phải từ 9 đến 12 chữ số", HttpStatus.BAD_REQUEST),
    USER_BIRTHDATE_INVALID(1011, "Ngày sinh phải là ngày trong quá khứ", HttpStatus.BAD_REQUEST),
    USER_AUTHENTICATION_FAILED(1012, "Sai tài khoản hoặc mật khẩu", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_NULL(1013, "Quyền người dùng không được để trống", HttpStatus.BAD_REQUEST),
    USER_ROLE_INVALID(1014, "Quyền người dùng không hợp lệ", HttpStatus.BAD_REQUEST),
    // Vehicle-related Errors (2xxx)
    VEHICLE_LICENSE_PLATE_EMPTY(2001, "Biển số xe không được để trống", HttpStatus.BAD_REQUEST),
    VEHICLE_LICENSE_PLATE_TOO_LONG(2002, "Biển số xe không được vượt quá 15 ký tự", HttpStatus.BAD_REQUEST),
    VEHICLE_LICENSE_PLATE_INVALID_FORMAT(2003, "Biển số xe chỉ được chứa chữ in hoa, số và dấu '-'", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_EMPTY(2004, "Loại xe không được để trống", HttpStatus.BAD_REQUEST),
    VEHICLE_NAME_TOO_LONG(2005, "Tên xe không được vượt quá 50 ký tự", HttpStatus.BAD_REQUEST),
    VEHICLE_MODEL_TOO_LONG(2005, "Tên xe không được vượt quá 50 ký tự", HttpStatus.BAD_REQUEST),
    VEHICLE_MANUFACTURE_YEAR_INVALID(2006, "Năm sản xuất phải từ 1990 đến 2025", HttpStatus.BAD_REQUEST),
    VEHICLE_COLOR_TOO_LONG(2007, "Màu sắc không được vượt quá 30 ký tự", HttpStatus.BAD_REQUEST),
    VEHICLE_CHASSIS_NUMBER_TOO_LONG(2008, "Số khung không được vượt quá 50 ký tự", HttpStatus.BAD_REQUEST),
    VEHICLE_ENGINE_NUMBER_TOO_LONG(2009, "Số máy không được vượt quá 50 ký tự", HttpStatus.BAD_REQUEST),
    VEHICLE_INSPECTION_DATE_INVALID(2010, "Ngày đăng kiểm không đúng định dạng (yyyy-MM-dd)", HttpStatus.BAD_REQUEST),
    VEHICLE_INSURANCE_DATE_INVALID(2011, "Ngày bảo hiểm không đúng định dạng (yyyy-MM-dd)", HttpStatus.BAD_REQUEST),
    VEHICLE_PURCHASE_DATE_INVALID(2011, "Ngày bảo hiểm không đúng định dạng (yyyy-MM-dd)", HttpStatus.BAD_REQUEST),
    VEHICLE_STATUS_EMPTY(2012, "Trạng thái hoạt động không được để trống", HttpStatus.BAD_REQUEST),
    VEHICLE_ALREADY_EXISTS(2013, "Biển số xe đã tồn tại", HttpStatus.BAD_REQUEST),
    VIN_ALREADY_EXISTS(2013, "Số khung xe đã tồn tại", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_NOT_FOUND(2014, "Loại xe không tồn tại", HttpStatus.BAD_REQUEST),
    VEHICLE_NOT_FOUND(2015, "Xe không tồn tại", HttpStatus.BAD_REQUEST),
    VEHICLE_INACTIVE(2016, "Xe đang không hoạt động", HttpStatus.BAD_REQUEST),

    // Driver-related Errors (3xxx)
    DRIVER_NOT_FOUND(3001, "Tài xế không tồn tại", HttpStatus.BAD_REQUEST),
    DRIVER_INACTIVE(3002, "Tài xế đang không hoạt động", HttpStatus.BAD_REQUEST),
    DRIVER_LICENSE_NUMBER_EMPTY(3003, "Số GPLX không được để trống", HttpStatus.BAD_REQUEST),
    DRIVER_LICENSE_TYPE_EMPTY(3004, "Loại GPLX không được để trống", HttpStatus.BAD_REQUEST),
    DRIVER_LICENSE_ISSUE_DATE_EMPTY(3005, "Ngày cấp GPLX không được để trống", HttpStatus.BAD_REQUEST),
    DRIVER_LICENSE_EXPIRY_DATE_EMPTY(3006, "Ngày hết hạn GPLX không được để trống", HttpStatus.BAD_REQUEST),
    DRIVER_EXPERIENCE_EMPTY(3007, "Kinh nghiệm làm việc không được để trống", HttpStatus.BAD_REQUEST),
    DRIVER_EXPERIENCE_INVALID(3008, "Kinh nghiệm làm việc không được âm", HttpStatus.BAD_REQUEST),
    DRIVER_STATUS_EMPTY(3009, "Trạng thái làm việc không được để trống", HttpStatus.BAD_REQUEST),
    DRIVER_SALARY_EMPTY(3010, "Mức lương cơ bản không được để trống", HttpStatus.BAD_REQUEST),
    DRIVER_SALARY_INVALID(3011, "Mức lương cơ bản phải lớn hơn 0", HttpStatus.BAD_REQUEST),
    DRIVER_USER_ID_EMPTY(3012, "ID người dùng không được để trống", HttpStatus.BAD_REQUEST),
    DRIVER_STATUS_INVALID(3013, "Trạng thái làm việc không hợp lệ", HttpStatus.BAD_REQUEST),
    DRIVER_ALREADY_EXISTS(3014, "Thông tin của tài xế này đã được tạo", HttpStatus.BAD_REQUEST),
    DRIVER_ALREADY_LICENSE_EXISTS(3014, "GPLX của tài xế này đã được tạo", HttpStatus.BAD_REQUEST),
    DRIVER_ALREADY_EMPTY(3015, "Thông tin của tài xế này không được để trống", HttpStatus.BAD_REQUEST),

    // Schedule-related Errors (4xxx)
    SCHEDULE_NOT_FOUND(4001, "Lịch trình không tồn tại", HttpStatus.BAD_REQUEST),
    SCHEDULE_ALREADY_PROCESSED(4002, "Lịch trình đã được xử lý", HttpStatus.BAD_REQUEST),
    SCHEDULE_NOT_APPROVED(4003, "Lịch trình chưa được duyệt", HttpStatus.BAD_REQUEST),
    SCHEDULE_INVALID_STATE_TRANSITION(4004, "Trạng thái không thể chuyển đổi", HttpStatus.BAD_REQUEST),
    SCHEDULE_VEHICLE_ALREADY_BOOKED(4005, "Xe đã có lịch trình trong khoảng thời gian này", HttpStatus.BAD_REQUEST),
    SCHEDULE_NOT_COMPLETED(4006, "Lịch trình chưa hoàn thành", HttpStatus.BAD_REQUEST),
    SCHEDULE_ID_EMPTY(4007, "Mã lịch trình không được để trống", HttpStatus.BAD_REQUEST),
    SCHEDULE_TRIP_CODE_EMPTY(4008, "Mã chuyến không được để trống", HttpStatus.BAD_REQUEST),
    SCHEDULE_ROUTE_EMPTY(4009, "Tuyến đường không được để trống", HttpStatus.BAD_REQUEST),
    SCHEDULE_VEHICLE_EMPTY(4010, "Xe không được để trống", HttpStatus.BAD_REQUEST),
    SCHEDULE_DRIVER_EMPTY(4011, "Tài xế chính không được để trống", HttpStatus.BAD_REQUEST),
    SCHEDULE_DEPARTURE_DATE_EMPTY(4012, "Ngày khởi hành không được để trống", HttpStatus.BAD_REQUEST),
    SCHEDULE_EXPECTED_ARRIVAL_DATE_EMPTY(4013, "Ngày dự kiến đến không được để trống", HttpStatus.BAD_REQUEST),
    SCHEDULE_GOODS_DESCRIPTION_EMPTY(4014, "Mô tả hàng hóa không được để trống", HttpStatus.BAD_REQUEST),
    SCHEDULE_WEIGHT_EMPTY(4015, "Trọng lượng hàng không được để trống", HttpStatus.BAD_REQUEST),
    SCHEDULE_WEIGHT_INVALID(4016, "Trọng lượng hàng phải lớn hơn 0", HttpStatus.BAD_REQUEST),
    SCHEDULE_NEW_STATUS_EMPTY(4017, "Trạng thái mới không được để trống", HttpStatus.BAD_REQUEST),

    // Route-related Errors (5xxx)
    ROUTE_NOT_FOUND(5001, "Tuyến đường không tồn tại", HttpStatus.BAD_REQUEST),
    ROUTE_INACTIVE(5002, "Tuyến đường đã dừng hoạt động", HttpStatus.BAD_REQUEST),
    ROUTE_NAME_EMPTY(5003, "Tên tuyến không được để trống", HttpStatus.BAD_REQUEST),
    ROUTE_START_LOCATION_EMPTY(5004, "Địa điểm đầu không được để trống", HttpStatus.BAD_REQUEST),
    ROUTE_END_LOCATION_EMPTY(5005, "Địa điểm cuối không được để trống", HttpStatus.BAD_REQUEST),
    ROUTE_DISTANCE_EMPTY(5006, "Quãng đường không được để trống", HttpStatus.BAD_REQUEST),
    ROUTE_DISTANCE_INVALID(5007, "Quãng đường phải lớn hơn 0", HttpStatus.BAD_REQUEST),
    ROUTE_EXPECTED_TIME_INVALID(5008, "Thời gian dự kiến không hợp lệ", HttpStatus.BAD_REQUEST),
    ROUTE_EXPECTED_COST_EMPTY(5009, "Chi phí dự kiến không được để trống", HttpStatus.BAD_REQUEST),
    ROUTE_EXPECTED_COST_INVALID(5010, "Chi phí dự kiến phải lớn hơn 0", HttpStatus.BAD_REQUEST),

    // Cost-related Errors (6xxx)
    COST_TYPE_NOT_FOUND(6001, "Loại chi phí không tồn tại", HttpStatus.BAD_REQUEST),
    COST_REQUEST_NOT_FOUND(6002, "Yêu cầu chi phí không tồn tại", HttpStatus.BAD_REQUEST),
    COST_TYPE_ID_EMPTY(6003, "Mã loại chi phí không được để trống", HttpStatus.BAD_REQUEST),
    COST_TYPE_NAME_EMPTY(6004, "Tên loại chi phí không được để trống", HttpStatus.BAD_REQUEST),
    COST_GROUP_EMPTY(6005, "Nhóm chi phí không được để trống", HttpStatus.BAD_REQUEST),
    COST_AMOUNT_EMPTY(6006, "Số tiền không được để trống", HttpStatus.BAD_REQUEST),
    COST_AMOUNT_INVALID(6007, "Số tiền phải lớn hơn 0", HttpStatus.BAD_REQUEST),
    COST_DATE_EMPTY(6008, "Ngày chi phí không được để trống", HttpStatus.BAD_REQUEST),
    COST_LOCATION_TOO_LONG(6009, "Địa điểm chi phí không được vượt quá 255 ký tự", HttpStatus.BAD_REQUEST),

    // Vehicle Type-related Errors (7xxx)
    VEHICLE_TYPE_CODE_EMPTY(7001, "Mã loại xe không được để trống", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_CODE_TOO_LONG(7002, "Mã loại xe không được vượt quá 20 ký tự", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_NAME_EMPTY(7003, "Tên loại xe không được để trống", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_NAME_TOO_LONG(7004, "Tên loại xe không được vượt quá 100 ký tự", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_WEIGHT_EMPTY(7005, "Tải trọng tối đa không được để trống", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_WEIGHT_INVALID(7006, "Tải trọng tối đa phải lớn hơn 0", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_DESCRIPTION_TOO_LONG(7007, "Mô tả không được vượt quá 255 ký tự", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_STATUS_EMPTY(7008, "Trạng thái không được để trống", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_STATUS_INVALID(7009, "Trạng thái không hợp lệ", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_ALREADY_EXISTS(7010, "Loại xe đã tạo", HttpStatus.BAD_REQUEST),

    EXPENSE_TYPE_ALREADY_EXISTS(7011, "Loại chi phí đã tạo", HttpStatus.BAD_REQUEST),
    EXPENSE_TYPE_NOT_FOUND(7012, "Loại chi phí không tồn tại", HttpStatus.BAD_REQUEST),
    // Authentication-related Errors (8xxx)
    AUTHENTICATION_REQUIRED(8001, "Vui lòng đăng nhập để tiếp tục", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(8002, "Phiên đăng nhập đã hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_ENUM_VALUE(8003, "Gia tri không hợp lệ", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(8004, "Mật khẩu không đc để trống", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(8005, "Tài khoản không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(8006, "Mật khuẩn không hợp lệ", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAILED(8007, "Sai tài khoản hoặc mật khẩu", HttpStatus.UNAUTHORIZED),
    ;
    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
