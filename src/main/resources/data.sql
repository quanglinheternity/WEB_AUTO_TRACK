-- ========================================
-- DATA.SQL - HỆ THỐNG QUẢN LÝ VẬN TẢI
-- ========================================

-- 1. PERMISSIONS (Quyền hạn)
-- INSERT INTO permissions (permission_name, description) VALUES
-- ('USER_CREATE', 'Tạo người dùng'),
-- ('USER_READ', 'Xem thông tin người dùng'),
-- ('USER_UPDATE', 'Cập nhật người dùng'),
-- ('USER_DELETE', 'Xóa người dùng'),

-- ('DRIVER_CREATE', 'Thêm tài xế'),
-- ('DRIVER_READ', 'Xem thông tin tài xế'),
-- ('DRIVER_UPDATE', 'Cập nhật tài xế'),
-- ('DRIVER_DELETE', 'Xóa tài xế'),

-- ('VEHICLE_CREATE', 'Thêm xe'),
-- ('VEHICLE_READ', 'Xem danh sách xe'),
-- ('VEHICLE_UPDATE', 'Cập nhật xe'),
-- ('VEHICLE_DELETE', 'Xóa xe'),

-- ('TRIP_CREATE', 'Tạo lịch trình'),
-- ('TRIP_READ', 'Xem lịch trình'),
-- ('TRIP_UPDATE', 'Cập nhật lịch trình'),
-- ('TRIP_DELETE', 'Xóa lịch trình'),
-- ('TRIP_APPROVE', 'Duyệt lịch trình'),

-- ('EXPENSE_CREATE', 'Tạo chi phí'),
-- ('EXPENSE_READ', 'Xem chi phí'),
-- ('EXPENSE_UPDATE', 'Cập nhật chi phí'),
-- ('EXPENSE_DELETE', 'Xóa chi phí'),
-- ('EXPENSE_APPROVE', 'Duyệt chi phí'),

-- ('SALARY_CREATE', 'Tạo bảng lương'),
-- ('SALARY_READ', 'Xem bảng lương'),
-- ('SALARY_UPDATE', 'Cập nhật bảng lương'),
-- ('SALARY_DELETE', 'Xóa bảng lương'),

-- ('ROUTE_CREATE', 'Tạo tuyến đường'),
-- ('ROUTE_READ', 'Xem tuyến đường'),
-- ('ROUTE_UPDATE', 'Cập nhật tuyến đường'),
-- ('ROUTE_DELETE', 'Xóa tuyến đường');
-- 2. ROLES (Vai trò)
-- INSERT INTO roles (role_name, description) VALUES
-- ('ADMIN', 'Quản trị viên hệ thống'),
-- ('MANAGER', 'Quản lý điều hành'),
-- ('ACCOUNTANT', 'Kế toán'),
-- ('DRIVER', 'Tài xế');

-- -- 2. USERS (Người dùng)
-- INSERT INTO users (username, password, phone, full_name, id_number, date_of_birth, address, role, is_active, created_at, updated_at) VALUES
-- -- Admin
-- ('admin', '$2a$10$YourHashedPasswordHere', '0901234567', 'Nguyễn Văn Admin', '001234567890', '1985-01-15', 'Số 1 Đường Láng, Đống Đa, Hà Nội', 'ADMIN', true, NOW(), NOW()),
-- -- Manager
-- ('manager01', '$2a$10$YourHashedPasswordHere', '0912345678', 'Trần Thị Hương', '002345678901', '1988-03-20', 'Số 45 Phố Huế, Hai Bà Trưng, Hà Nội', 'MANAGER', true, NOW(), NOW()),
-- ('manager02', '$2a$10$YourHashedPasswordHere', '0923456789', 'Lê Văn Minh', '003456789012', '1990-07-10', 'Số 78 Giải Phóng, Thanh Xuân, Hà Nội', 'MANAGER', true, NOW(), NOW()),
-- -- Accountant
-- ('accountant01', '$2a$10$YourHashedPasswordHere', '0934567890', 'Phạm Thị Lan', '004567890123', '1992-05-25', 'Số 12 Trần Đại Nghĩa, Hai Bà Trưng, Hà Nội', 'ACCOUNTANT', true, NOW(), NOW()),
-- -- Drivers
-- ('driver01', '$2a$10$YourHashedPasswordHere', '0945678901', 'Nguyễn Văn Hùng', '005678901234', '1987-08-15', 'Số 23 Nguyễn Trãi, Thanh Xuân, Hà Nội', 'DRIVER', true, NOW(), NOW()),
-- ('driver02', '$2a$10$YourHashedPasswordHere', '0956789012', 'Hoàng Văn Đức', '006789012345', '1989-11-20', 'Số 56 Tây Sơn, Đống Đa, Hà Nội', 'DRIVER', true, NOW(), NOW()),
-- ('driver03', '$2a$10$YourHashedPasswordHere', '0967890123', 'Vũ Văn Tùng', '007890123456', '1991-02-28', 'Số 89 Lê Duẩn, Hoàn Kiếm, Hà Nội', 'DRIVER', true, NOW(), NOW()),
-- ('driver04', '$2a$10$YourHashedPasswordHere', '0978901234', 'Đỗ Văn Thắng', '008901234567', '1986-12-05', 'Số 34 Kim Mã, Ba Đình, Hà Nội', 'DRIVER', true, NOW(), NOW()),
-- ('driver05', '$2a$10$YourHashedPasswordHere', '0989012345', 'Bùi Văn Công', '009012345678', '1993-04-18', 'Số 67 Ngọc Khánh, Ba Đình, Hà Nội', 'DRIVER', true, NOW(), NOW());

-- -- 3. DRIVERS (Thông tin tài xế)
-- INSERT INTO drivers (id, driver_code, license_number, license_class, license_issue_date, license_expiry_date, years_of_experience, employment_status, base_salary, note, created_at, updated_at) VALUES
-- (5, 'DRV001', '012345678', 'C', '2018-03-15', '2028-03-15', 7, 'ACTIVE', 12000000, 'Tài xế có kinh nghiệm, lái xe an toàn', NOW(), NOW()),
-- (6, 'DRV002', '023456789', 'D', '2017-06-20', '2027-06-20', 8, 'ACTIVE', 13000000, 'Tài xế chuyên vận chuyển hàng nặng', NOW(), NOW()),
-- (7, 'DRV003', 'GPLX034567', 'C', '2019-01-10', '2029-01-10', 6, 'ACTIVE', 11500000, 'Tài xế nhiệt tình, chăm chỉ', NOW(), NOW()),
-- (8, 'DRV004', 'GPLX045678', 'D', '2016-08-25', '2026-08-25', 9, 'ACTIVE', 14000000, 'Tài xế kỳ cựu, am hiểu đường sá', NOW(), NOW()),
-- (9, 'DRV005', 'GPLX056789', 'C', '2020-05-12', '2030-05-12', 5, 'ACTIVE', 11000000, 'Tài xế mới, nhiệt huyết', NOW(), NOW());

-- -- 4. VEHICLE_TYPES (Loại xe)
-- INSERT INTO vehicle_types (code, name, max_payload, description, is_active) VALUES
-- ('TRK-5T', 'Xe tải 5 tấn', 5.00, 'Xe tải nhỏ, thích hợp vận chuyển hàng hóa trong thành phố', true),
-- ('TRK-8T', 'Xe tải 8 tấn', 8.00, 'Xe tải trung bình, vận chuyển liên tỉnh', true),
-- ('TRK-15T', 'Xe tải 15 tấn', 15.00, 'Xe tải lớn, vận chuyển hàng hóa nặng', true),
-- ('TRK-20T', 'Xe đầu kéo 20 tấn', 20.00, 'Xe đầu kéo container, vận chuyển hàng hóa lớn', true),
-- ('VAN-3T', 'Xe Van 3 tấn', 3.00, 'Xe Van nhỏ, thích hợp vận chuyển hàng nhẹ', true);

-- -- 5. VEHICLES (Phương tiện)
-- INSERT INTO vehicles (license_plate, vehicle_type_id, brand, model, manufacture_year, color, vin, engine_number, registration_date, inspection_expiry_date, insurance_expiry_date, status, purchase_date, note, created_at, updated_at) VALUES
-- ('29C-12345', 1, 'Hino', 'XZU730L', 2020, 'Trắng', 'VIN001XZU730L2020', 'ENG001XZU', '2020-01-15', '2025-01-15', '2025-06-30', 'AVAILABLE', '2020-01-10', 'Xe tải 5 tấn, tình trạng tốt', NOW(), NOW()),
-- ('30G-23456', 2, 'Isuzu', 'FRR90N', 2019, 'Xanh', 'VIN002FRR90N2019', 'ENG002FRR', '2019-03-20', '2024-12-20', '2025-03-20', 'AVAILABLE', '2019-03-15', 'Xe tải 8 tấn, đã qua bảo dưỡng', NOW(), NOW()),
-- ('51C-34567', 3, 'Hyundai', 'HD360', 2021, 'Xám', 'VIN003HD3602021', 'ENG003HD3', '2021-05-10', '2026-05-10', '2025-12-31', 'AVAILABLE', '2021-05-05', 'Xe tải 15 tấn, mới', NOW(), NOW()),
-- ('92A-45678', 4, 'Kamaz', '65117', 2018, 'Đỏ', 'VIN004KMZ651172018', 'ENG004KMZ', '2018-07-25', '2024-11-25', '2025-07-25', 'AVAILABLE', '2018-07-20', 'Xe đầu kéo, phù hợp đường dài', NOW(), NOW()),
-- ('29A-56789', 5, 'Ford', 'Transit', 2022, 'Bạc', 'VIN005TRANSIT2022', 'ENG005TRA', '2022-02-15', '2027-02-15', '2026-02-15', 'AVAILABLE', '2022-02-10', 'Xe Van mới, tiện nghi', NOW(), NOW()),
-- ('30C-67890', 1, 'Hino', 'XZU730L', 2019, 'Trắng', 'VIN006XZU730L2019', 'ENG006XZU', '2019-08-20', '2024-12-20', '2025-08-20', 'IN_USE', '2019-08-15', 'Xe tải 5 tấn, đang vận hành', NOW(), NOW()),
-- ('51B-78901', 2, 'Isuzu', 'FRR90N', 2020, 'Xanh', 'VIN007FRR90N2020', 'ENG007FRR', '2020-11-10', '2025-11-10', '2025-11-10', 'MAINTENANCE', '2020-11-05', 'Xe đang bảo trì định kỳ', NOW(), NOW());

-- -- 6. ROUTES (Tuyến đường)
-- INSERT INTO routes (code, name, origin, destination, distance_km, estimated_duration_hours, estimated_fuel_cost, description, is_active) VALUES
-- ('RT001', 'Hà Nội - Hải Phòng', 'Hà Nội', 'Hải Phòng', 120.00, 2.50, 600000, 'Tuyến cao tốc Hà Nội - Hải Phòng, đường tốt', true),
-- ('RT002', 'Hà Nội - Thanh Hóa', 'Hà Nội', 'Thanh Hóa', 160.00, 3.00, 800000, 'Tuyến đường quốc lộ 1A, thường xuyên vận chuyển', true),
-- ('RT003', 'Hà Nội - Nghệ An', 'Hà Nội', 'Nghệ An', 290.00, 5.50, 1450000, 'Tuyến đường dài, cần chuẩn bị kỹ', true),
-- ('RT004', 'Hà Nội - Đà Nẵng', 'Hà Nội', 'Đà Nẵng', 760.00, 14.00, 3800000, 'Tuyến đường xa, vận chuyển liên tỉnh miền Trung', true),
-- ('RT005', 'Hà Nội - TP HCM', 'Hà Nội', 'TP Hồ Chí Minh', 1710.00, 30.00, 8550000, 'Tuyến Bắc - Nam, cần 2-3 tài xế thay phiên', true),
-- ('RT006', 'Hà Nội - Hải Dương', 'Hà Nội', 'Hải Dương', 60.00, 1.50, 300000, 'Tuyến ngắn, trong ngày hoàn thành', true),
-- ('RT007', 'Hà Nội - Nam Định', 'Hà Nội', 'Nam Định', 90.00, 2.00, 450000, 'Tuyến gần, đường tốt', true),
-- ('RT008', 'Hà Nội - Quảng Ninh', 'Hà Nội', 'Quảng Ninh', 150.00, 3.50, 750000, 'Tuyến vào khu công nghiệp Quảng Ninh', true);

-- -- 7. EXPENSE_CATEGORIES (Danh mục chi phí)
-- INSERT INTO expense_categories (code, name, description, category_group, is_active) VALUES
-- ('FUEL', 'Nhiên liệu', 'Chi phí xăng dầu cho xe', 'FUEL', true),
-- ('TOLL', 'Phí đường bộ', 'Phí qua trạm thu phí, cầu đường', 'TOLL', true),
-- ('PARKING', 'Phí đỗ xe', 'Phí đỗ xe tại bãi, kho hàng', 'PARKING', true),
-- ('MEAL', 'Ăn uống', 'Chi phí ăn uống cho tài xế trong chuyến đi', 'MEAL', true),
-- ('REPAIR', 'Sửa chữa', 'Chi phí sửa chữa xe đột xuất', 'MAINTENANCE', true),
-- ('MAINT', 'Bảo dưỡng', 'Chi phí bảo dưỡng định kỳ', 'MAINTENANCE', true),
-- ('INSURANCE', 'Bảo hiểm', 'Chi phí bảo hiểm xe', 'ADMINISTRATIVE', true),
-- ('REGISTRATION', 'Đăng kiểm', 'Chi phí đăng kiểm xe', 'ADMINISTRATIVE', true),
-- ('OTHER', 'Chi phí khác', 'Các chi phí phát sinh khác', 'OTHER', true);

-- -- 8. TRIPS (Chuyến đi)
-- INSERT INTO trips (trip_code, route_id, vehicle_id, driver_id, departure_time, estimated_arrival_time, actual_arrival_time, status, cargo_description, cargo_weight, created_by, approved_at, approved_by, completed_at, note, created_at, updated_at) VALUES
-- -- Chuyến đã hoàn thành
-- ('TRIP001', 1, 1, 5, '2024-10-01 06:00:00', '2024-10-01 08:30:00', '2024-10-01 08:45:00', 'COMPLETED', 'Thiết bị điện tử', 3.50, 2, '2024-10-01 15:00:00', 2, '2024-10-01 09:00:00', 'Giao hàng thành công', '2024-09-30 14:00:00', '2024-10-01 09:00:00'),
-- ('TRIP002', 2, 2, 6, '2024-10-02 05:00:00', '2024-10-02 08:00:00', '2024-10-02 08:15:00', 'COMPLETED', 'Vật liệu xây dựng', 7.20, 2, '2024-10-02 14:00:00', 2, '2024-10-02 09:00:00', 'Hoàn thành tốt', '2024-10-01 16:00:00', '2024-10-02 09:00:00'),
-- ('TRIP003', 3, 3, 7, '2024-10-03 04:00:00', '2024-10-03 09:30:00', '2024-10-03 09:45:00', 'COMPLETED', 'Hàng tiêu dùng', 12.50, 3, '2024-10-03 16:00:00', 3, '2024-10-03 10:00:00', 'Chuyến đi suôn sẻ', '2024-10-02 15:00:00', '2024-10-03 10:00:00'),
-- ('TRIP004', 6, 1, 5, '2024-10-05 07:00:00', '2024-10-05 08:30:00', '2024-10-05 08:40:00', 'COMPLETED', 'Thực phẩm tươi sống', 2.80, 2, '2024-10-05 13:00:00', 2, '2024-10-05 09:00:00', 'Giao hàng đúng giờ', '2024-10-04 16:00:00', '2024-10-05 09:00:00'),
-- ('TRIP005', 7, 5, 9, '2024-10-06 08:00:00', '2024-10-06 10:00:00', '2024-10-06 10:10:00', 'COMPLETED', 'Văn phòng phẩm', 1.50, 3, '2024-10-06 15:00:00', 3, '2024-10-06 10:30:00', 'Không có vấn đề', '2024-10-05 14:00:00', '2024-10-06 10:30:00'),

-- -- Chuyến đang thực hiện
-- ('TRIP006', 4, 3, 7, '2024-10-20 05:00:00', '2024-10-20 19:00:00', NULL, 'IN_PROGRESS', 'Máy móc công nghiệp', 14.00, 2, NULL, NULL, NULL, 'Đang vận chuyển đến Đà Nẵng', '2024-10-19 15:00:00', '2024-10-20 05:00:00'),
-- ('TRIP007', 1, 6, 8, '2024-10-21 06:00:00', '2024-10-21 08:30:00', NULL, 'IN_PROGRESS', 'Hàng hóa tổng hợp', 4.50, 3, NULL, NULL, NULL, 'Đang trên đường đến Hải Phòng', '2024-10-20 16:00:00', '2024-10-21 06:00:00'),

-- -- Chuyến đã phê duyệt, chưa khởi hành
-- ('TRIP008', 8, 2, 6, '2024-10-24 06:00:00', '2024-10-24 09:30:00', NULL, 'APPROVED', 'Thiết bị y tế', 6.80, 2, NULL, NULL, NULL, 'Đã xếp hàng, sẵn sàng khởi hành', '2024-10-22 10:00:00', '2024-10-23 14:00:00'),
-- ('TRIP009', 2, 4, 8, '2024-10-25 05:00:00', '2024-10-25 08:00:00', NULL, 'APPROVED', 'Phân bón nông nghiệp', 18.00, 3, NULL, NULL, NULL, 'Chuẩn bị khởi hành', '2024-10-22 11:00:00', '2024-10-23 15:00:00'),

-- -- Chuyến đang chờ duyệt
-- ('TRIP010', 3, 1, 5, '2024-10-26 04:30:00', '2024-10-26 10:00:00', NULL, 'PENDING', 'Hàng điện tử', 4.20, 2, NULL, NULL, NULL, 'Chờ phê duyệt từ quản lý', '2024-10-23 09:00:00', '2024-10-23 09:00:00'),

-- -- Chuyến đã hủy
-- ('TRIP011', 5, 3, 7, '2024-10-15 04:00:00', '2024-10-16 10:00:00', NULL, 'CANCELLED', 'Hàng dệt may', 13.00, 3, NULL, NULL, NULL, 'Khách hàng hủy đơn hàng', '2024-10-14 10:00:00', '2024-10-15 08:00:00');

-- -- 9. EXPENSES (Chi phí chuyến đi)
-- INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, manager_approved_at, manager_approved_by_id, manager_note, accountant_approved_at, accountant_approved_by_id, accountant_note, created_at, updated_at) VALUES
-- -- Chi phí TRIP001 (Đã duyệt hoàn toàn)
-- ('EXP001', 1, 1, 450000, '2024-10-01', 'Trạm xăng Shell, Hà Nội', 'Đổ xăng trước khi khởi hành', 'HD001234', 'ACCOUNTANT_APPROVED', 5, '2024-10-01 16:00:00', 2, 'Hợp lý', '2024-10-01 17:00:00', 4, 'Đã thanh toán', '2024-10-01 06:15:00', '2024-10-01 17:00:00'),
-- ('EXP002', 1, 2, 80000, '2024-10-01', 'Trạm thu phí Pháp Vân - Cầu Giẽ', 'Phí đường cao tốc', 'TOLL001', 'ACCOUNTANT_APPROVED', 5, '2024-10-01 16:00:00', 2, 'OK', '2024-10-01 17:00:00', 4, 'Đã thanh toán', '2024-10-01 06:45:00', '2024-10-01 17:00:00'),
-- ('EXP003', 1, 4, 50000, '2024-10-01', 'Quán cơm Hải Phòng', 'Ăn trưa', NULL, 'ACCOUNTANT_APPROVED', 5, '2024-10-01 16:00:00', 2, 'Đúng định mức', '2024-10-01 17:00:00', 4, 'Đã thanh toán', '2024-10-01 12:00:00', '2024-10-01 17:00:00'),

-- -- Chi phí TRIP002 (Đã duyệt hoàn toàn)
-- ('EXP004', 2, 1, 650000, '2024-10-02', 'Trạm xăng Petrolimex Hà Nội', 'Xăng cho chuyến đi', 'HD002345', 'ACCOUNTANT_APPROVED', 6, '2024-10-02 15:00:00', 2, 'Phù hợp', '2024-10-02 16:00:00', 4, 'Đã chi trả', '2024-10-02 05:20:00', '2024-10-02 16:00:00'),
-- ('EXP005', 2, 2, 120000, '2024-10-02', 'Trạm thu phí Bỉm Sơn', 'Phí đường bộ', 'TOLL002', 'ACCOUNTANT_APPROVED', 6, '2024-10-02 15:00:00', 2, 'Chuẩn', '2024-10-02 16:00:00', 4, 'Đã chi trả', '2024-10-02 07:30:00', '2024-10-02 16:00:00'),
-- ('EXP006', 2, 4, 60000, '2024-10-02', 'Nhà hàng Thanh Hóa', 'Bữa trưa', NULL, 'ACCOUNTANT_APPROVED', 6, '2024-10-02 15:00:00', 2, 'OK', '2024-10-02 16:00:00', 4, 'Đã chi trả', '2024-10-02 12:30:00', '2024-10-02 16:00:00'),

-- -- Chi phí TRIP003 (Đã duyệt hoàn toàn)
-- ('EXP007', 3, 1, 1200000, '2024-10-03', 'Trạm xăng Total Hà Nội', 'Xăng cho chuyến xa', 'HD003456', 'ACCOUNTANT_APPROVED', 7, '2024-10-03 17:00:00', 3, 'Hợp lệ', '2024-10-03 18:00:00', 4, 'Đã thanh toán', '2024-10-03 04:30:00', '2024-10-03 18:00:00'),
-- ('EXP008', 3, 2, 200000, '2024-10-03', 'Các trạm thu phí tuyến Hà Nội - Nghệ An', 'Phí cao tốc', 'TOLL003', 'ACCOUNTANT_APPROVED', 7, '2024-10-03 17:00:00', 3, 'Đúng', '2024-10-03 18:00:00', 4, 'Đã thanh toán', '2024-10-03 06:00:00', '2024-10-03 18:00:00'),
-- ('EXP009', 3, 4, 80000, '2024-10-03', 'Quán ăn Vinh, Nghệ An', 'Ăn uống trên đường', NULL, 'ACCOUNTANT_APPROVED', 7, '2024-10-03 17:00:00', 3, 'Phù hợp', '2024-10-03 18:00:00', 4, 'Đã thanh toán', '2024-10-03 11:30:00', '2024-10-03 18:00:00'),

-- -- Chi phí TRIP006 (Chờ duyệt kế toán)
-- ('EXP010', 6, 1, 3200000, '2024-10-20', 'Trạm xăng Hà Tĩnh', 'Xăng cho chuyến đi xa', 'HD004567', 'MANAGER_APPROVED', 7, '2024-10-20 20:00:00', 2, 'Số tiền hợp lý cho chuyến xa', NULL, NULL, NULL, '2024-10-20 10:30:00', '2024-10-20 20:00:00'),
-- ('EXP011', 6, 2, 350000, '2024-10-20', 'Các trạm thu phí đường dài', 'Phí đường bộ', 'TOLL004', 'MANAGER_APPROVED', 7, '2024-10-20 20:00:00', 2, 'OK', NULL, NULL, NULL, '2024-10-20 08:00:00', '2024-10-20 20:00:00'),
-- ('EXP012', 6, 4, 150000, '2024-10-20', 'Nhà hàng Quảng Bình', 'Ăn trưa trên đường', NULL, 'MANAGER_APPROVED', 7, '2024-10-20 20:00:00', 2, 'Phù hợp', NULL, NULL, NULL, '2024-10-20 13:00:00', '2024-10-20 20:00:00'),

-- -- Chi phí TRIP007 (Chờ duyệt quản lý)
-- ('EXP013', 7, 1, 380000, '2024-10-21', 'Trạm xăng Hải Dương', 'Xăng đổ thêm', 'HD005678', 'PENDING', 8, NULL, NULL, NULL, NULL, NULL, NULL, '2024-10-21 07:30:00', '2024-10-21 07:30:00'),
-- ('EXP014', 7, 2, 70000, '2024-10-21', 'Trạm thu phí Hà Nội - Hải Phòng', 'Phí đường', 'TOLL005', 'PENDING', 8, NULL, NULL, NULL, NULL, NULL, NULL, '2024-10-21 06:30:00', '2024-10-21 06:30:00'),

-- -- Chi phí bảo dưỡng (Không thuộc chuyến đi cụ thể)
-- ('EXP015', NULL, 6, 2500000, '2024-10-10', 'Gara Thăng Long, Hà Nội', 'Bảo dưỡng định kỳ 10000km xe 29C-12345', 'GR001234', 'ACCOUNTANT_APPROVED', NULL, '2024-10-11 10:00:00', 2, 'Bảo dưỡng đầy đủ', '2024-10-11 14:00:00', 4, 'Đã thanh toán', '2024-10-10 15:00:00', '2024-10-11 14:00:00'),
-- ('EXP016', NULL, 5, 1800000, '2024-10-12', 'Gara Isuzu Hà Nội', 'Thay lốp xe 30G-23456', 'GR002345', 'ACCOUNTANT_APPROVED', NULL, '2024-10-13 09:00:00', 3, 'Chi phí hợp lý', '2024-10-13 11:00:00', 4, 'Đã chi trả', '2024-10-12 14:00:00', '2024-10-13 11:00:00'),
-- ('EXP017', NULL, 7, 4500000, '2024-10-08', 'Công ty Bảo hiểm PTI', 'Gia hạn bảo hiểm xe 29C-12345', 'BH001234', 'ACCOUNTANT_APPROVED', NULL, '2024-10-09 10:00:00', 2, 'Thanh toán bảo hiểm năm 2025', '2024-10-09 15:00:00', 4, 'Đã chuyển khoản', '2024-10-08 10:00:00', '2024-10-09 15:00:00');

-- -- 10. SALARY_REPORTS (Báo cáo lương)
-- INSERT INTO salary_reports (report_month, driver_id, total_trips, total_distance, base_salary, trip_bonus, allowance, deduction, total_salary, is_paid, paid_at, note, created_at, updated_at) VALUES
-- -- Lương tháng 9/2024 (Đã thanh toán)
-- ('2024-09', 5, 22, 2640.00, 12000000, 2200000, 500000, 0, 14700000, true, '2024-10-05 14:00:00', 'Tháng 9 hoàn thành tốt nhiệm vụ', '2024-10-01 09:00:00', '2024-10-05 14:00:00'),
-- ('2024-09', 6, 18, 2520.00, 13000000, 1800000, 500000, 0, 15300000, true, '2024-10-05 14:00:00', 'Làm việc chăm chỉ', '2024-10-01 09:00:00', '2024-10-05 14:00:00'),
-- ('2024-09', 7, 20, 3100.00, 11500000, 2000000, 500000, 0, 14000000, true, '2024-10-05 14:00:00', 'Hoàn thành chuyến đi xa nhiều', '2024-10-01 09:00:00', '2024-10-05 14:00:00'),
-- ('2024-09', 8, 16, 2200.00, 14000000, 1600000, 500000, 200000, 15900000, true, '2024-10-05 14:00:00', 'Trừ 200k do đến muộn 1 lần', '2024-10-01 09:00:00', '2024-10-05 14:00:00'),
-- ('2024-09', 9, 15, 1350.00, 11000000, 1500000, 500000, 0, 13000000, true, '2024-10-05 14:00:00', 'Tài xế mới, làm việc tốt', '2024-10-01 09:00:00', '2024-10-05 14:00:00'),

-- -- Lương tháng 10/2024 (Chưa thanh toán - tính đến ngày 23/10)
-- ('2024-10', 5, 18, 2160.00, 12000000, 1800000, 500000, 0, 14300000, false, NULL, 'Tính lương đến 23/10/2024', '2024-10-23 09:00:00', '2024-10-23 09:00:00'),
-- ('2024-10', 6, 15, 2100.00, 13000000, 1500000, 500000, 0, 15000000, false, NULL, 'Tính lương đến 23/10/2024', '2024-10-23 09:00:00', '2024-10-23 09:00:00'),
-- ('2024-10', 7, 17, 2700.00, 11500000, 1700000, 500000, 0, 13700000, false, NULL, 'Tính lương đến 23/10/2024, đang thực hiện chuyến đi xa', '2024-10-23 09:00:00', '2024-10-23 09:00:00'),
-- ('2024-10', 8, 14, 1920.00, 14000000, 1400000, 500000, 0, 15900000, false, NULL, 'Tính lương đến 23/10/2024', '2024-10-23 09:00:00', '2024-10-23 09:00:00'),
-- ('2024-10', 9, 12, 1080.00, 11000000, 1200000, 500000, 0, 12700000, false, NULL, 'Tính lương đến 23/10/2024', '2024-10-23 09:00:00', '2024-10-23 09:00:00');

-- 3. ROLE_PERMISSIONS (Gán quyền cho vai trò)
-- Admin - tất cả quyền
-- INSERT INTO role_permissions (role_name, permission_name)
-- SELECT 'ADMIN', permission_name FROM permissions;

-- -- Manager - quyền quản lý
-- INSERT INTO role_permissions (role_name, permission_name) VALUES
-- ('MANAGER', 'USER_READ'),
-- ('MANAGER', 'DRIVER_READ'),
-- ('MANAGER', 'DRIVER_UPDATE'),
-- ('MANAGER', 'VEHICLE_READ'),
-- ('MANAGER', 'VEHICLE_UPDATE'),
-- ('MANAGER', 'TRIP_CREATE'),
-- ('MANAGER', 'TRIP_READ'),
-- ('MANAGER', 'TRIP_UPDATE'),
-- ('MANAGER', 'TRIP_APPROVE'),
-- ('MANAGER', 'EXPENSE_READ'),
-- ('MANAGER', 'EXPENSE_APPROVE'),
-- ('MANAGER', 'SALARY_READ'),
-- ('MANAGER', 'ROUTE_READ'),
-- ('MANAGER', 'ROUTE_CREATE'),
-- ('MANAGER', 'ROUTE_UPDATE');

-- -- Accountant - quyền kế toán
-- INSERT INTO role_permissions (role_name, permission_name) VALUES
-- ('ACCOUNTANT', 'EXPENSE_READ'),
-- ('ACCOUNTANT', 'EXPENSE_APPROVE'),
-- ('ACCOUNTANT', 'SALARY_CREATE'),
-- ('ACCOUNTANT', 'SALARY_READ'),
-- ('ACCOUNTANT', 'SALARY_UPDATE'),
-- ('ACCOUNTANT', 'TRIP_READ'),
-- ('ACCOUNTANT', 'DRIVER_READ'),
-- ('ACCOUNTANT', 'VEHICLE_READ');

-- Driver - quyền tài xế
-- INSERT INTO role_permissions (role_name, permission_name) VALUES
-- ('DRIVER', 'TRIP_READ'),
-- ('DRIVER', 'EXPENSE_CREATE'),
-- ('DRIVER', 'EXPENSE_READ'),
-- ('DRIVER', 'SALARY_READ');
INSERT INTO user_roles (user_id, role_name) VALUES
-- Admin
(1, 'ADMIN'),
-- Managers
(2, 'MANAGER'),
(3, 'MANAGER'),
-- Accountant
(4, 'ACCOUNTANT'),
-- Drivers
(5, 'DRIVER'),
(6, 'DRIVER'),
(7, 'DRIVER'),
(8, 'DRIVER'),
(9, 'DRIVER');
-- ========================================
-- KẾT THÚC DATA.SQL
-- ========================================

-- Thống kê dữ liệu đã insert:
-- - 30 Permissions
-- - 9 Users (1 Admin, 2 Managers, 1 Accountant, 5 Drivers)
-- - 5 Driver records
-- - 5 Vehicle Types
-- - 7 Vehicles
-- - 8 Routes
-- - 9 Expense Categories
-- - 11 Trips (Completed: 5, In Progress: 2, Approved: 2, Pending: 1, Cancelled: 1)
-- - 17 Expenses (với nhiều trạng thái khác nhau)
-- - 10 Salary Reports (5 đã thanh toán tháng 9, 5 chưa thanh toán tháng 10)
-- - User Permissions được phân quyền theo vai trò