-- V7__Insert_Sample_Data.sql
-- Thêm dữ liệu mẫu cho các bảng còn lại

-- ============================================
-- 1. USERS (Admin, Manager, Accountant, Drivers)
-- ============================================
-- Password mặc định: "12345678" đã được mã hóa BCrypt
INSERT INTO users (id, username, password, phone, full_name, id_number, date_of_birth, address, is_active, created_by) VALUES
(1, 'admin', '$2a$10$zkeJEcOZ0O7X6wbPGfoC9.snS8eQeYN3Yk.J/wFd2iHnpAiTtP7p6', '0901234567', 'Nguyễn Văn Admin', '001234567890', '1985-01-15', 'Hà Nội', true, 'system'),
(2, 'manager01', '$2a$10$zkeJEcOZ0O7X6wbPGfoC9.snS8eQeYN3Yk.J/wFd2iHnpAiTtP7p6', '0902345678', 'Trần Thị Lan', '002234567890', '1987-03-20', 'Hà Nội', true, 'admin'),
(3, 'accountant01', '$2a$10$zkeJEcOZ0O7X6wbPGfoC9.snS8eQeYN3Yk.J/wFd2iHnpAiTtP7p6', '0903456789', 'Lê Văn Hùng', '003234567890', '1990-06-10', 'Hà Nội', true, 'admin'),
(4, 'driver01', '$2a$10$zkeJEcOZ0O7X6wbPGfoC9.snS8eQeYN3Yk.J/wFd2iHnpAiTtP7p6', '0904567890', 'Phạm Văn An', '004234567890', '1988-08-15', 'Hà Nội', true, 'manager01'),
(5, 'driver02', '$2a$10$zkeJEcOZ0O7X6wbPGfoC9.snS8eQeYN3Yk.J/wFd2iHnpAiTtP7p6', '0905678901', 'Hoàng Văn Bình', '005234567890', '1992-02-20', 'Hà Nội', true, 'manager01'),
(6, 'driver03', '$2a$10$zkeJEcOZ0O7X6wbPGfoC9.snS8eQeYN3Yk.J/wFd2iHnpAiTtP7p6', '0906789012', 'Đặng Văn Cường', '006234567890', '1989-11-05', 'Hải Phòng', true, 'manager01'),
(7, 'driver04', '$2a$10$zkeJEcOZ0O7X6wbPGfoC9.snS8eQeYN3Yk.J/wFd2iHnpAiTtP7p6', '0907890123', 'Vũ Văn Dũng', '007234567890', '1991-04-25', 'Đà Nẵng', true, 'manager01'),
(8, 'driver05', '$2a$10$zkeJEcOZ0O7X6wbPGfoC9.snS8eQeYN3Yk.J/wFd2iHnpAiTtP7p6', '0908901234', 'Bùi Văn Em', '008234567890', '1993-07-12', 'TP.HCM', true, 'manager01');

-- ============================================
-- 2. USER ROLES
-- ============================================
INSERT INTO user_roles (user_id, role_name) VALUES
(1, 'ADMIN'),
(2, 'MANAGER'),
(3, 'ACCOUNTANT'),
(4, 'DRIVER'),
(5, 'DRIVER'),
(6, 'DRIVER'),
(7, 'DRIVER'),
(8, 'DRIVER');

-- ============================================
-- 3. DRIVERS
-- ============================================
INSERT INTO drivers (id, driver_code, license_number, license_class, license_issue_date, license_expiry_date, years_of_experience, employment_status, base_salary, note, created_by) VALUES
(4, 'DRV001', 'B2-123456789', 'B2', '2015-05-10', '2030-05-10', 9, 'ACTIVE', 12000000, 'Tài xế kinh nghiệm, chạy tuyến Hà Nội - Hải Phòng', 'manager01'),
(5, 'DRV002', 'C-234567890', 'C', '2016-08-15', '2031-08-15', 8, 'ACTIVE', 13000000, 'Chuyên chạy xe tải nặng', 'manager01'),
(6, 'DRV003', 'D-345678901', 'D', '2018-03-20', '2033-03-20', 6, 'ACTIVE', 15000000, 'Tài xế xe container', 'manager01'),
(7, 'DRV004', 'C-456789012', 'C', '2017-11-05', '2032-11-05', 7, 'ACTIVE', 13500000, 'Tài xế tuyến dài Bắc Nam', 'manager01'),
(8, 'DRV005', 'B2-567890123', 'B2', '2019-06-30', '2034-06-30', 5, 'ACTIVE', 11000000, 'Tài xế mới, đang tích lũy kinh nghiệm', 'manager01');

-- ============================================
-- 4. VEHICLE TYPES
-- ============================================
INSERT INTO vehicle_types (id, code, name, max_payload, description, is_active) VALUES
(1, 'TRUCK_1T', 'Xe tải 1 tấn', 1.00, 'Xe tải nhỏ phù hợp vận chuyển nội thành', true),
(2, 'TRUCK_3_5T', 'Xe tải 3.5 tấn', 3.50, 'Xe tải trung bình chạy liên tỉnh', true),
(3, 'TRUCK_5T', 'Xe tải 5 tấn', 5.00, 'Xe tải nặng chạy tuyến dài', true),
(4, 'TRUCK_8T', 'Xe tải 8 tấn', 8.00, 'Xe tải nặng chở hàng cồng kềnh', true),
(5, 'CONTAINER_20F', 'Container 20 feet', 18.00, 'Xe container tiêu chuẩn 20 feet', true),
(6, 'CONTAINER_40F', 'Container 40 feet', 26.00, 'Xe container lớn 40 feet', true);

-- ============================================
-- 5. VEHICLES
-- ============================================
INSERT INTO vehicles (id, license_plate, vehicle_type_id, brand, model, manufacture_year, color, vin, engine_number, registration_date, inspection_expiry_date, insurance_expiry_date, status, purchase_date, note, created_by) VALUES
(1, '29A-12345', 1, 'Hyundai', 'Porter H150', 2020, 'Trắng', 'VIN001HYU2020001', 'ENG001HYU', '2020-01-15', '2025-01-15', '2025-06-30', 'AVAILABLE', '2020-01-10', 'Xe tải nhỏ chạy nội thành', 'manager01'),
(2, '30A-23456', 2, 'Isuzu', 'QKR270', 2019, 'Xanh', 'VIN002ISU2019001', 'ENG002ISU', '2019-06-20', '2024-06-20', '2025-03-15', 'AVAILABLE', '2019-06-15', 'Xe tải liên tỉnh', 'manager01'),
(3, '31A-34567', 3, 'Hino', 'FC9JLSW', 2021, 'Trắng', 'VIN003HIN2021001', 'ENG003HIN', '2021-03-10', '2026-03-10', '2025-09-20', 'AVAILABLE', '2021-03-05', 'Xe chạy tuyến Bắc Nam', 'manager01'),
(4, '29B-45678', 4, 'Dongfeng', 'Hoàng Huy', 2018, 'Xanh đậm', 'VIN004DON2018001', 'ENG004DON', '2018-09-15', '2025-09-15', '2025-04-10', 'IN_USE', '2018-09-10', 'Xe chở hàng nặng', 'manager01'),
(5, '51A-56789', 5, 'Daewoo', 'Prima', 2020, 'Xám', 'VIN005DAE2020001', 'ENG005DAE', '2020-11-20', '2025-11-20', '2025-07-15', 'AVAILABLE', '2020-11-15', 'Container 20 feet', 'manager01'),
(6, '51B-67890', 6, 'Hyundai', 'Mighty EX8', 2022, 'Trắng', 'VIN006HYU2022001', 'ENG006HYU', '2022-05-10', '2027-05-10', '2025-12-30', 'AVAILABLE', '2022-05-05', 'Container 40 feet mới', 'manager01'),
(7, '29C-78901', 2, 'Thaco', 'Ollin 500B', 2017, 'Đỏ', 'VIN007THA2017001', 'ENG007THA', '2017-08-25', '2024-08-25', '2025-02-28', 'MAINTENANCE', '2017-08-20', 'Đang bảo dưỡng định kỳ', 'manager01'),
(8, '30B-89012', 3, 'Fuso', 'Canter', 2021, 'Xanh', 'VIN008FUS2021001', 'ENG008FUS', '2021-12-01', '2026-12-01', '2025-08-20', 'AVAILABLE', '2021-11-25', 'Xe tải trung chạy liên tỉnh', 'manager01');

-- ============================================
-- 6. ROUTES
-- ============================================
INSERT INTO routes (id, code, name, origin, destination, distance_km, estimated_duration_hours, estimated_fuel_cost, description, is_active, created_by) VALUES
(1, 'RT001', 'Hà Nội - Hải Phòng', 'Hà Nội', 'Hải Phòng', 120.00, 2.50, 600000, 'Tuyến Hà Nội - Hải Phòng qua cao tốc', true, 'manager01'),
(2, 'RT002', 'Hà Nội - Đà Nẵng', 'Hà Nội', 'Đà Nẵng', 760.00, 14.00, 3800000, 'Tuyến dài Bắc Trung qua quốc lộ 1A', true, 'manager01'),
(3, 'RT003', 'Đà Nẵng - TP.HCM', 'Đà Nẵng', 'TP. Hồ Chí Minh', 950.00, 16.00, 4750000, 'Tuyến Trung Nam qua quốc lộ 1A', true, 'manager01'),
(4, 'RT004', 'Hà Nội - TP.HCM', 'Hà Nội', 'TP. Hồ Chí Minh', 1710.00, 30.00, 8550000, 'Tuyến Bắc Nam xuyên Việt', true, 'manager01'),
(5, 'RT005', 'Hà Nội - Vinh', 'Hà Nội', 'Vinh, Nghệ An', 290.00, 5.50, 1450000, 'Tuyến Hà Nội - Vinh qua cao tốc', true, 'manager01'),
(6, 'RT006', 'Hải Phòng - Quảng Ninh', 'Hải Phòng', 'Hạ Long, Quảng Ninh', 85.00, 2.00, 425000, 'Tuyến Hải Phòng - Quảng Ninh ven biển', true, 'manager01'),
(7, 'RT007', 'TP.HCM - Cần Thơ', 'TP. Hồ Chí Minh', 'Cần Thơ', 170.00, 3.50, 850000, 'Tuyến Nam Bộ qua cao tốc Trung Lương', true, 'manager01'),
(8, 'RT008', 'Hà Nội - Thanh Hóa', 'Hà Nội', 'Thanh Hóa', 155.00, 3.00, 775000, 'Tuyến Hà Nội - Thanh Hóa', true, 'manager01');

-- ============================================
-- 7. EXPENSE CATEGORIES
-- ============================================
INSERT INTO expense_categories (id, code, name, description, category_group, is_active) VALUES
(1, 'FUEL', 'Nhiên liệu', 'Chi phí xăng dầu cho xe', 'FUEL', true),
(2, 'TOLL', 'Phí đường bộ', 'Phí cầu đường, cao tốc', 'TOLL', true),
(3, 'PARKING', 'Phí đỗ xe', 'Chi phí đỗ xe, gửi xe', 'PARKING', true),
(4, 'MEAL', 'Ăn uống', 'Chi phí ăn uống của tài xế', 'MEAL', true),
(5, 'ACCOMMODATION', 'Lưu trú', 'Chi phí khách sạn, nhà nghỉ', 'MEAL', true),
(6, 'MAINTENANCE', 'Bảo dưỡng', 'Chi phí bảo dưỡng xe định kỳ', 'MAINTENANCE', true),
(7, 'REPAIR', 'Sửa chữa', 'Chi phí sửa chữa đột xuất', 'MAINTENANCE', true),
(8, 'TIRE', 'Lốp xe', 'Chi phí thay lốp xe', 'MAINTENANCE', true),
(9, 'OIL', 'Nhớt', 'Chi phí thay nhớt động cơ', 'MAINTENANCE', true),
(10, 'INSURANCE', 'Bảo hiểm', 'Chi phí bảo hiểm xe', 'ADMINISTRATIVE', true),
(11, 'REGISTRATION', 'Đăng kiểm', 'Chi phí đăng kiểm xe', 'ADMINISTRATIVE', true),
(12, 'OTHER', 'Chi phí khác', 'Các chi phí phát sinh khác', 'OTHER', true);

-- ============================================
-- 8. TRIPS (Các chuyến đi trong tháng 10-11/2025)
-- ============================================
INSERT INTO trips (id, trip_code, route_id, vehicle_id, driver_id, departure_time, estimated_arrival_time, actual_arrival_time, status, cargo_description, cargo_weight, created_by, approval_status, note, created_at) VALUES
-- Chuyến đã hoàn thành
(1, 'TRIP2025100001', 1, 1, 4, '2025-10-01 06:00:00', '2025-10-01 08:30:00', '2025-10-01 08:45:00', 'ARRIVED', 'Hàng điện tử', 0.80, 2, true, 'Chuyến đi suôn sẻ', '2025-09-28 10:00:00'),
(2, 'TRIP2025100002', 5, 2, 5, '2025-10-02 05:00:00', '2025-10-02 10:30:00', '2025-10-02 10:15:00', 'ARRIVED', 'Vật liệu xây dựng', 3.20, 2, true, 'Giao hàng đúng hẹn', '2025-09-29 14:00:00'),
(3, 'TRIP2025100003', 2, 3, 6, '2025-10-05 04:00:00', '2025-10-05 18:00:00', '2025-10-05 19:30:00', 'ARRIVED', 'Máy móc công nghiệp', 4.50, 2, true, 'Giao hàng chậm 1.5h do mưa', '2025-10-02 09:00:00'),
(4, 'TRIP2025100004', 1, 1, 4, '2025-10-08 07:00:00', '2025-10-08 09:30:00', '2025-10-08 09:20:00', 'ARRIVED', 'Hàng thực phẩm đóng gói', 0.90, 2, true, NULL, '2025-10-05 11:00:00'),
(5, 'TRIP2025100005', 6, 5, 7, '2025-10-10 06:30:00', '2025-10-10 08:30:00', '2025-10-10 08:40:00', 'ARRIVED', 'Container hàng xuất khẩu', 16.00, 2, true, 'Đã giao hàng tại cảng', '2025-10-07 15:00:00'),
(6, 'TRIP2025100006', 8, 2, 5, '2025-10-12 05:30:00', '2025-10-12 08:30:00', '2025-10-12 08:25:00', 'ARRIVED', 'Hàng tiêu dùng', 2.80, 2, true, NULL, '2025-10-09 10:00:00'),
(7, 'TRIP2025100007', 3, 3, 6, '2025-10-15 03:00:00', '2025-10-15 19:00:00', '2025-10-15 18:45:00', 'ARRIVED', 'Phụ tùng ô tô', 4.20, 2, true, 'Giao hàng sớm 15 phút', '2025-10-12 13:00:00'),
(8, 'TRIP2025100008', 1, 1, 4, '2025-10-18 06:00:00', '2025-10-18 08:30:00', '2025-10-18 09:00:00', 'ARRIVED', 'Hàng may mặc', 0.70, 2, true, 'Chậm do tắc đường', '2025-10-15 16:00:00'),
(9, 'TRIP2025100009', 7, 8, 8, '2025-10-20 05:00:00', '2025-10-20 08:30:00', '2025-10-20 08:20:00', 'ARRIVED', 'Nông sản đồng bằng', 2.90, 2, true, NULL, '2025-10-17 09:00:00'),
(10, 'TRIP2025100010', 5, 2, 5, '2025-10-22 06:00:00', '2025-10-22 11:30:00', '2025-10-22 11:45:00', 'ARRIVED', 'Xi măng', 3.50, 2, true, 'Đường xấu, chậm 15 phút', '2025-10-19 14:00:00'),

-- Chuyến đang thực hiện
(11, 'TRIP2025110001', 2, 3, 6, '2025-11-08 04:00:00', '2025-11-08 18:00:00', NULL, 'IN_PROGRESS', 'Thiết bị y tế', 3.80, 2, true, 'Chuyến đang trên đường', '2025-11-05 10:00:00'),
(12, 'TRIP2025110002', 1, 1, 4, '2025-11-09 06:00:00', '2025-11-09 08:30:00', NULL, 'IN_PROGRESS', 'Hàng điện tử', 0.85, 2, true, NULL, '2025-11-06 15:00:00'),

-- Chuyến đã lên lịch
(13, 'TRIP2025110003', 4, 5, 7, '2025-11-12 03:00:00', '2025-11-13 09:00:00', NULL, 'NOT_STARTED', 'Container xuất khẩu', 18.00, 2, true, 'Tuyến dài Bắc Nam', '2025-11-07 11:00:00'),
(14, 'TRIP2025110004', 7, 8, 8, '2025-11-13 05:00:00', '2025-11-13 08:30:00', NULL, 'NOT_STARTED', 'Thực phẩm đông lạnh', 2.50, 2, true, NULL, '2025-11-08 09:00:00'),
(15, 'TRIP2025110005', 1, 1, 4, '2025-11-14 07:00:00', '2025-11-14 09:30:00', NULL, 'NOT_STARTED', 'Văn phòng phẩm', 0.60, 2, false, 'Chờ phê duyệt', '2025-11-09 14:00:00'),

-- Chuyến bị hủy
(16, 'TRIP2025100011', 2, 4, 6, '2025-10-25 04:00:00', '2025-10-25 18:00:00', NULL, 'CANCELLED', 'Hàng công nghiệp', 7.50, 2, false, NULL, '2025-10-22 10:00:00');

-- Cập nhật trạng thái chuyến bị hủy
UPDATE trips SET 
    cancelled_at = '2025-10-24 16:00:00',
    cancelled_by = 2,
    cancellation_reason = 'Khách hàng hủy đơn hàng'
WHERE id = 16;

-- Cập nhật trạng thái hoàn thành cho các chuyến đã xong
UPDATE trips SET 
    completed_at = actual_arrival_time,
    approved_at = DATE_ADD(actual_arrival_time, INTERVAL 2 HOUR),
    approved_by = 2
WHERE status = 'ARRIVED';

-- ============================================
-- 9. EXPENSES (Chi phí cho các chuyến đi)
-- ============================================
-- Chi phí cho TRIP 1
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100001', 1, 1, 240000, '2025-10-01', 'Trạm xăng Petrolimex Hà Nội', 'Xăng cho chuyến đi', 'HD001', 'ACCOUNTANT_APPROVED', 4, 4, '2025-10-01 06:30:00'),
('EXP2025100002', 1, 2, 80000, '2025-10-01', 'Trạm thu phí Pháp Vân', 'Phí cao tốc', 'HD002', 'ACCOUNTANT_APPROVED', 4, 4, '2025-10-01 07:00:00'),
('EXP2025100003', 1, 4, 50000, '2025-10-01', 'Hải Phòng', 'Ăn trưa', 'HD003', 'ACCOUNTANT_APPROVED', 4, 4, '2025-10-01 12:00:00');

-- Chi phí cho TRIP 2
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100004', 2, 1, 580000, '2025-10-02', 'Trạm xăng Shell Hà Nội', 'Nhiên liệu', 'HD004', 'ACCOUNTANT_APPROVED', 5, 5, '2025-10-02 05:30:00'),
('EXP2025100005', 2, 2, 120000, '2025-10-02', 'Cao tốc Hà Nội - Vinh', 'Phí đường bộ', 'HD005', 'ACCOUNTANT_APPROVED', 5, 5, '2025-10-02 06:30:00'),
('EXP2025100006', 2, 4, 70000, '2025-10-02', 'Nghệ An', 'Chi phí ăn uống', 'HD006', 'ACCOUNTANT_APPROVED', 5, 5, '2025-10-02 11:00:00');

-- Chi phí cho TRIP 3 (tuyến dài)
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100007', 3, 1, 1520000, '2025-10-05', 'Trạm xăng Petrolimex', 'Nhiên liệu tuyến dài', 'HD007', 'ACCOUNTANT_APPROVED', 6, 6, '2025-10-05 04:30:00'),
('EXP2025100008', 3, 2, 380000, '2025-10-05', 'Các trạm thu phí', 'Phí cao tốc tuyến dài', 'HD008', 'ACCOUNTANT_APPROVED', 6, 6, '2025-10-05 08:00:00'),
('EXP2025100009', 3, 4, 150000, '2025-10-05', 'Quán ăn Quốc lộ 1A', 'Ăn 3 bữa', 'HD009', 'ACCOUNTANT_APPROVED', 6, 6, '2025-10-05 19:00:00'),
('EXP2025100010', 3, 5, 300000, '2025-10-05', 'Khách sạn Hải Vân', 'Nghỉ đêm', 'HD010', 'ACCOUNTANT_APPROVED', 6, 6, '2025-10-05 20:00:00');

-- Chi phí cho TRIP 4
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100011', 4, 1, 230000, '2025-10-08', 'Trạm xăng', 'Xăng', 'HD011', 'ACCOUNTANT_APPROVED', 4, 4, '2025-10-08 07:15:00'),
('EXP2025100012', 4, 2, 80000, '2025-10-08', 'Cao tốc', 'Phí đường', 'HD012', 'ACCOUNTANT_APPROVED', 4, 4, '2025-10-08 07:45:00');

-- Chi phí cho TRIP 5
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100013', 5, 1, 340000, '2025-10-10', 'Trạm xăng Caltex', 'Nhiên liệu', 'HD013', 'ACCOUNTANT_APPROVED', 7, 7, '2025-10-10 06:45:00'),
('EXP2025100014', 5, 2, 90000, '2025-10-10', 'Phí cảng', 'Phí vào cảng', 'HD014', 'ACCOUNTANT_APPROVED', 7, 7, '2025-10-10 08:00:00');

-- Chi phí cho TRIP 6
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100015', 6, 1, 310000, '2025-10-12', 'Trạm xăng', 'Xăng dầu', 'HD015', 'ACCOUNTANT_APPROVED', 5, 5, '2025-10-12 05:45:00'),
('EXP2025100016', 6, 2, 110000, '2025-10-12', 'Cao tốc', 'Phí đường', 'HD016', 'ACCOUNTANT_APPROVED', 5, 5, '2025-10-12 06:15:00'),
('EXP2025100017', 6, 4, 65000, '2025-10-12', 'Thanh Hóa', 'Ăn uống', 'HD017', 'ACCOUNTANT_APPROVED', 5, 5, '2025-10-12 12:30:00');

-- Chi phí cho TRIP 7 (tuyến dài)
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100018', 7, 1, 1900000, '2025-10-15', 'Các trạm xăng dọc đường', 'Nhiên liệu tuyến Đà Nẵng - TP.HCM', 'HD018', 'ACCOUNTANT_APPROVED', 6, 6, '2025-10-15 04:00:00'),
('EXP2025100019', 7, 2, 450000, '2025-10-15', 'Các trạm thu phí', 'Phí cao tốc', 'HD019', 'ACCOUNTANT_APPROVED', 6, 6, '2025-10-15 10:00:00'),
('EXP2025100020', 7, 4, 200000, '2025-10-15', 'Các điểm dừng', 'Ăn 3 bữa trên đường', 'HD020', 'ACCOUNTANT_APPROVED', 6, 6, '2025-10-15 18:00:00'),
('EXP2025100021', 7, 5, 350000, '2025-10-15', 'Nha Trang', 'Nghỉ đêm khách sạn', 'HD021', 'ACCOUNTANT_APPROVED', 6, 6, '2025-10-15 22:00:00');

-- Chi phí cho TRIP 8
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100022', 8, 1, 220000, '2025-10-18', 'Trạm xăng', 'Xăng', 'HD022', 'ACCOUNTANT_APPROVED', 4, 4, '2025-10-18 06:15:00'),
('EXP2025100023', 8, 2, 80000, '2025-10-18', 'Cao tốc', 'Phí đường', 'HD023', 'ACCOUNTANT_APPROVED', 4, 4, '2025-10-18 07:00:00'),
('EXP2025100024', 8, 3, 30000, '2025-10-18', 'Hải Phòng', 'Phí đỗ xe', 'HD024', 'ACCOUNTANT_APPROVED', 4, 4, '2025-10-18 08:30:00');

-- Chi phí cho TRIP 9
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100025', 9, 1, 340000, '2025-10-20', 'Trạm xăng Caltex', 'Nhiên liệu', 'HD025', 'ACCOUNTANT_APPROVED', 8, 8, '2025-10-20 05:15:00'),
('EXP2025100026', 9, 2, 120000, '2025-10-20', 'Cao tốc Trung Lương', 'Phí cao tốc', 'HD026', 'ACCOUNTANT_APPROVED', 8, 8, '2025-10-20 06:00:00'),
('EXP2025100027', 9, 4, 80000, '2025-10-20', 'Cần Thơ', 'Ăn uống', 'HD027', 'ACCOUNTANT_APPROVED', 8, 8, '2025-10-20 11:00:00');

-- Chi phí cho TRIP 10
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100028', 10, 1, 580000, '2025-10-22', 'Trạm xăng', 'Nhiên liệu', 'HD028', 'ACCOUNTANT_APPROVED', 5, 5, '2025-10-22 06:15:00'),
('EXP2025100029', 10, 2, 130000, '2025-10-22', 'Cao tốc', 'Phí đường bộ', 'HD029', 'ACCOUNTANT_APPROVED', 5, 5, '2025-10-22 07:30:00'),
('EXP2025100030', 10, 4, 75000, '2025-10-22', 'Nghệ An', 'Ăn uống', 'HD030', 'ACCOUNTANT_APPROVED', 5, 5, '2025-10-22 12:00:00');

-- Chi phí cho TRIP 11 (đang thực hiện)
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025110001', 11, 1, 1550000, '2025-11-08', 'Trạm xăng Petrolimex', 'Nhiên liệu tuyến dài', 'HD031', 'MANAGER_APPROVED', 6, 6, '2025-11-08 04:30:00'),
('EXP2025110002', 11, 2, 390000, '2025-11-08', 'Các trạm thu phí', 'Phí cao tốc', 'HD032', 'MANAGER_APPROVED', 6, 6, '2025-11-08 08:00:00'),
('EXP2025110003', 11, 4, 120000, '2025-11-08', 'Quán ăn', 'Ăn sáng và trưa', 'HD033', 'PENDING', 6, 6, '2025-11-08 12:00:00');

-- Chi phí cho TRIP 12 (đang thực hiện)
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025110004', 12, 1, 235000, '2025-11-09', 'Trạm xăng', 'Xăng', 'HD034', 'PENDING', 4, 4, '2025-11-09 06:20:00'),
('EXP2025110005', 12, 2, 80000, '2025-11-09', 'Cao tốc', 'Phí đường', NULL, 'PENDING', 4, 4, '2025-11-09 07:00:00');

-- ============================================
-- 10. CHI PHÍ BẢO DƯỠNG VÀ SỬA CHỮA XE
-- ============================================
-- Bảo dưỡng định kỳ xe 29C-78901 (đang MAINTENANCE)
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025110006', NULL, 6, 2500000, '2025-11-05', 'Garage Thăng Long', 'Bảo dưỡng 20,000km', 'BD001', 'ACCOUNTANT_APPROVED', NULL, 2, '2025-11-05 09:00:00'),
('EXP2025110007', NULL, 9, 800000, '2025-11-05', 'Garage Thăng Long', 'Thay nhớt và lọc dầu', 'BD002', 'ACCOUNTANT_APPROVED', NULL, 2, '2025-11-05 09:30:00');

-- Sửa chữa đột xuất cho xe 30A-23456
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100031', NULL, 7, 1500000, '2025-10-15', 'Garage Minh Khai', 'Sửa hệ thống phanh', 'SC001', 'ACCOUNTANT_APPROVED', NULL, 2, '2025-10-15 14:00:00');

-- Thay lốp xe cho xe 29A-12345
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100032', NULL, 8, 3200000, '2025-10-20', 'Lốp Xe Hùng Cường', 'Thay 4 lốp mới', 'LX001', 'ACCOUNTANT_APPROVED', NULL, 2, '2025-10-20 10:00:00');

-- ============================================
-- 11. CHI PHÍ HÀNH CHÍNH
-- ============================================
-- Bảo hiểm xe
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100033', NULL, 10, 5800000, '2025-10-10', 'Bảo Việt', 'Bảo hiểm xe 51A-56789', 'BH001', 'ACCOUNTANT_APPROVED', NULL, 3, '2025-10-10 14:00:00'),
('EXP2025100034', NULL, 10, 6200000, '2025-10-12', 'Bảo Việt', 'Bảo hiểm xe 51B-67890', 'BH002', 'ACCOUNTANT_APPROVED', NULL, 3, '2025-10-12 14:00:00');

-- Đăng kiểm xe
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100035', NULL, 11, 450000, '2025-10-08', 'Trung tâm Đăng kiểm 29-02V', 'Đăng kiểm xe 30A-23456', 'DK001', 'ACCOUNTANT_APPROVED', NULL, 3, '2025-10-08 09:00:00');

-- ============================================
-- 12. CẬP NHẬT TRẠNG THÁI PHÊ DUYỆT CHI PHÍ
-- ============================================
-- Phê duyệt của Manager cho các chi phí đã được duyệt
UPDATE expenses 
SET 
    manager_approved_at = DATE_ADD(expense_date, INTERVAL 2 HOUR),
    manager_approved_by_id = 2,
    manager_note = 'Đã xác nhận chi phí hợp lệ'
WHERE status IN ('MANAGER_APPROVED', 'ACCOUNTANT_APPROVED') 
    AND trip_id IS NOT NULL
    AND expense_date < '2025-11-08';

-- Phê duyệt của Accountant cho các chi phí đã được duyệt hoàn toàn
UPDATE expenses 
SET 
    accountant_approved_at = DATE_ADD(expense_date, INTERVAL 4 HOUR),
    accountant_approved_by_id = 3,
    accountant_note = 'Đã thanh toán'
WHERE status = 'ACCOUNTANT_APPROVED';

-- ============================================
-- 13. SALARY REPORTS (Báo cáo lương tài xế)
-- ============================================
-- Lương tháng 10/2025 cho các tài xế
INSERT INTO salary_reports (report_month, driver_id, total_trips, total_distance, base_salary, trip_bonus, allowance, deduction, total_salary, is_paid, paid_at, note, created_by) VALUES
-- Driver 4 (Phạm Văn An) - 4 chuyến trong tháng 10
('2025-10', 4, 4, 480.00, 12000000, 2000000, 500000, 0, 14500000, true, '2025-11-05 10:00:00', 'Hoàn thành tốt nhiệm vụ', 2),

-- Driver 5 (Hoàng Văn Bình) - 3 chuyến trong tháng 10
('2025-10', 5, 3, 855.00, 13000000, 1500000, 400000, 0, 14900000, true, '2025-11-05 10:00:00', NULL, 2),

-- Driver 6 (Đặng Văn Cường) - 2 chuyến trong tháng 10 (tuyến dài)
('2025-10', 6, 2, 1710.00, 15000000, 3000000, 800000, 0, 18800000, true, '2025-11-05 10:00:00', 'Thưởng thêm do chạy tuyến dài', 2),

-- Driver 7 (Vũ Văn Dũng) - 1 chuyến trong tháng 10
('2025-10', 7, 1, 85.00, 13500000, 500000, 200000, 0, 14200000, false, NULL, 'Chưa thanh toán', 2),

-- Driver 8 (Bùi Văn Em) - 1 chuyến trong tháng 10
('2025-10', 8, 1, 170.00, 11000000, 500000, 200000, 0, 11700000, false, NULL, 'Chưa thanh toán', 2);

-- ============================================
-- 14. CHI PHÍ KHÁC (OTHER)
-- ============================================
INSERT INTO expenses (expense_code, trip_id, category_id, amount, expense_date, location, description, receipt_number, status, driver_by_id, created_by, created_at) VALUES
('EXP2025100036', NULL, 12, 500000, '2025-10-15', 'Văn phòng công ty', 'Mua thiết bị an toàn lao động', 'VP001', 'ACCOUNTANT_APPROVED', NULL, 2, '2025-10-15 15:00:00'),
('EXP2025100037', NULL, 12, 800000, '2025-10-20', 'Công ty vệ sinh', 'Vệ sinh xe sau chuyến dài', 'VS001', 'ACCOUNTANT_APPROVED', NULL, 2, '2025-10-20 16:00:00'),
('EXP2025110008', 11, 12, 150000, '2025-11-08', 'Đường đi', 'Chi phí phát sinh khác', NULL, 'PENDING', 6, 6, '2025-11-08 15:00:00');

-- ============================================
-- 15. THÊM MỘT SỐ CHUYẾN ĐI TRONG TƯƠNG LAI
-- ============================================
INSERT INTO trips (trip_code, route_id, vehicle_id, driver_id, departure_time, estimated_arrival_time, actual_arrival_time, status, cargo_description, cargo_weight, created_by, approval_status, note, created_at) VALUES
('TRIP2025110006', 2, 3, 6, '2025-11-15 04:00:00', '2025-11-15 18:00:00', NULL, 'NOT_STARTED', 'Hàng điện tử', 4.00, 2, true, NULL, '2025-11-09 16:00:00'),
('TRIP2025110007', 5, 2, 5, '2025-11-16 06:00:00', '2025-11-16 11:30:00', NULL, 'NOT_STARTED', 'Vật liệu xây dựng', 3.40, 2, false, 'Chờ phê duyệt', '2025-11-10 09:00:00'),
('TRIP2025110008', 7, 8, 8, '2025-11-17 05:00:00', '2025-11-17 08:30:00', NULL, 'NOT_STARTED', 'Nông sản', 2.60, 2, true, NULL, '2025-11-10 10:00:00'),
('TRIP2025110009', 1, 1, 4, '2025-11-18 07:00:00', '2025-11-18 09:30:00', NULL, 'NOT_STARTED', 'Hàng tiêu dùng', 0.75, 2, false, 'Chờ xác nhận', '2025-11-10 11:00:00'),
('TRIP2025110010', 4, 5, 7, '2025-11-20 03:00:00', '2025-11-21 09:00:00', NULL, 'NOT_STARTED', 'Container hàng xuất khẩu', 17.50, 2, true, 'Tuyến dài Bắc Nam', '2025-11-10 14:00:00');

-- ============================================
-- SUMMARY: Tổng kết dữ liệu mẫu đã tạo
-- ============================================
-- Users: 8 (1 Admin, 1 Manager, 1 Accountant, 5 Drivers)
-- Vehicles: 8 xe với các loại khác nhau
-- Vehicle Types: 6 loại xe
-- Routes: 8 tuyến đường
-- Expense Categories: 12 loại chi phí
-- Trips: 21 chuyến (10 hoàn thành, 2 đang chạy, 8 đã lên lịch, 1 bị hủy)
-- Expenses: 45+ chi phí (bao gồm chi phí chuyến đi, bảo dưỡng, hành chính)
-- Salary Reports: 5 báo cáo lương tháng 10/2025