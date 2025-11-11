-- V6__Insert_Initial_Data.sql
-- Thêm dữ liệu khởi tạo cho roles và permissions

-- Insert Roles
INSERT INTO roles (role_name, description) VALUES
('ADMIN', 'Quản trị viên hệ thống'),
('MANAGER', 'Quản lý'),
('ACCOUNTANT', 'Kế toán'),
('DRIVER', 'Tài xế'),
('VIEWER', 'Người xem');

-- Insert Permissions - User Management
INSERT INTO permissions (permission_name, description) VALUES
('USER_CREATE', 'Tạo người dùng mới'),
('USER_READ', 'Xem thông tin người dùng'),
('USER_UPDATE', 'Cập nhật thông tin người dùng'),
('USER_DELETE', 'Xóa người dùng'),
('USER_LIST', 'Xem danh sách người dùng');

-- Insert Permissions - Role & Permission Management
INSERT INTO permissions (permission_name, description) VALUES
('ROLE_CREATE', 'Tạo vai trò mới'),
('ROLE_READ', 'Xem thông tin vai trò'),
('ROLE_UPDATE', 'Cập nhật vai trò'),
('ROLE_DELETE', 'Xóa vai trò'),
('ROLE', 'Quản lý vai trò'),
('PERMISSION', 'Quản lý phân quyền');


-- Insert Permissions - Driver Management
INSERT INTO permissions (permission_name, description) VALUES
('DRIVER_CREATE', 'Tạo tài xế mới'),
('DRIVER_READ', 'Xem thông tin tài xế'),
('DRIVER_UPDATE', 'Cập nhật thông tin tài xế'),
('DRIVER_DELETE', 'Xóa tài xế'),
('DRIVER_LIST', 'Xem danh sách tài xế');

-- Insert Permissions - Vehicle Management
INSERT INTO permissions (permission_name, description) VALUES
('VEHICLE_CREATE', 'Tạo phương tiện mới'),
('VEHICLE_READ', 'Xem thông tin phương tiện'),
('VEHICLE_UPDATE', 'Cập nhật thông tin phương tiện'),
('VEHICLE_DELETE', 'Xóa phương tiện'),
('VEHICLE_LIST', 'Xem danh sách phương tiện'),
('VEHICLE_TYPE', 'Quản lý loại phương tiện');

-- Insert Permissions - Route Management
INSERT INTO permissions (permission_name, description) VALUES
('ROUTE_CREATE', 'Tạo tuyến đường mới'),
('ROUTE_READ', 'Xem thông tin tuyến đường'),
('ROUTE_UPDATE', 'Cập nhật tuyến đường'),
('ROUTE_DELETE', 'Xóa tuyến đường'),
('ROUTE_LIST', 'Xem danh sách tuyến đường');

-- Insert Permissions - Trip Management
INSERT INTO permissions (permission_name, description) VALUES
('TRIP_CREATE', 'Tạo chuyến đi mới'),
('TRIP_READ', 'Xem thông tin chuyến đi'),
('TRIP_UPDATE', 'Cập nhật chuyến đi'),
('TRIP_DELETE', 'Xóa chuyến đi'),
('TRIP_LIST', 'Xem danh sách chuyến đi'),
('TRIP_APPROVE', 'Phê duyệt chuyến đi'),
('TRIP_CANCEL', 'Hủy chuyến đi'),
('TRIP_COMPLETE', 'Hoàn thành chuyến đi');

-- Insert Permissions - Expense Management
INSERT INTO permissions (permission_name, description) VALUES
('EXPENSE_CREATE', 'Tạo chi phí mới'),
('EXPENSE_READ', 'Xem thông tin chi phí'),
('EXPENSE_UPDATE', 'Cập nhật chi phí'),
('EXPENSE_DELETE', 'Xóa chi phí'),
('EXPENSE_LIST', 'Xem danh sách chi phí'),
('EXPENSE_APPROVE_MANAGER', 'Phê duyệt chi phí (Quản lý)'),
('EXPENSE_APPROVE_ACCOUNTANT', 'Phê duyệt chi phí (Kế toán)'),
('EXPENSE_CATEGORY', 'Tất cả quyền loại chi phí'),
('EXPENSE_APPROVE','Phê duyệt chi phí');
-- Insert Permissions - Salary Management
INSERT INTO permissions (permission_name, description) VALUES
('SALARY_CREATE', 'Tạo báo cáo lương'),
('SALARY_READ', 'Xem báo cáo lương'),
('SALARY_UPDATE', 'Cập nhật báo cáo lương'),
('SALARY_DELETE', 'Xóa báo cáo lương'),
('SALARY_LIST', 'Xem danh sách báo cáo lương'),
('SALARY_APPROVE', 'Phê duyệt thanh toán lương');

-- Insert Permissions - Report
INSERT INTO permissions (permission_name, description) VALUES
('REPORT_VIEW', 'Xem báo cáo'),
('REPORT_EXPORT', 'Xuất báo cáo');

-- Assign Permissions to ADMIN Role (Full access)
INSERT INTO role_permissions (role_name, permission_name)
SELECT 'ADMIN', permission_name FROM permissions;

-- Assign Permissions to MANAGER Role
INSERT INTO role_permissions (role_name, permission_name) VALUES
-- User management (read only)
('MANAGER', 'USER_READ'),
('MANAGER', 'USER_LIST'),
-- Driver management
('MANAGER', 'DRIVER_CREATE'),
('MANAGER', 'DRIVER_READ'),
('MANAGER', 'DRIVER_UPDATE'),
('MANAGER', 'DRIVER_LIST'),
-- Vehicle management
('MANAGER', 'VEHICLE_CREATE'),
('MANAGER', 'VEHICLE_READ'),
('MANAGER', 'VEHICLE_UPDATE'),
('MANAGER', 'VEHICLE_LIST'),
('MANAGER', 'VEHICLE_TYPE'),
-- Route management
('MANAGER', 'ROUTE_CREATE'),
('MANAGER', 'ROUTE_READ'),
('MANAGER', 'ROUTE_UPDATE'),
('MANAGER', 'ROUTE_LIST'),
-- Trip management (full)
('MANAGER', 'TRIP_CREATE'),
('MANAGER', 'TRIP_READ'),
('MANAGER', 'TRIP_UPDATE'),
('MANAGER', 'TRIP_LIST'),
('MANAGER', 'TRIP_APPROVE'),
('MANAGER', 'TRIP_CANCEL'),
('MANAGER', 'TRIP_COMPLETE'),
-- Expense management
('MANAGER', 'EXPENSE_READ'),
('MANAGER', 'EXPENSE_LIST'),
('MANAGER', 'EXPENSE_APPROVE_MANAGER'),
-- Salary management
('MANAGER', 'SALARY_READ'),
('MANAGER', 'SALARY_LIST'),
-- Reports
('MANAGER', 'REPORT_VIEW'),
('MANAGER', 'REPORT_EXPORT');

-- Assign Permissions to ACCOUNTANT Role
INSERT INTO role_permissions (role_name, permission_name) VALUES
-- User (read only)
('ACCOUNTANT', 'USER_READ'),
('ACCOUNTANT', 'USER_LIST'),
-- Driver (read only)
('ACCOUNTANT', 'DRIVER_READ'),
('ACCOUNTANT', 'DRIVER_LIST'),
-- Vehicle (read only)
('ACCOUNTANT', 'VEHICLE_READ'),
('ACCOUNTANT', 'VEHICLE_LIST'),
-- Trip (read only)
('ACCOUNTANT', 'TRIP_READ'),
('ACCOUNTANT', 'TRIP_LIST'),
-- Expense management (full)
('ACCOUNTANT', 'EXPENSE_CREATE'),
('ACCOUNTANT', 'EXPENSE_READ'),
('ACCOUNTANT', 'EXPENSE_UPDATE'),
('ACCOUNTANT', 'EXPENSE_LIST'),
('ACCOUNTANT', 'EXPENSE_APPROVE_ACCOUNTANT'),
-- Salary management (full)
('ACCOUNTANT', 'SALARY_CREATE'),
('ACCOUNTANT', 'SALARY_READ'),
('ACCOUNTANT', 'SALARY_UPDATE'),
('ACCOUNTANT', 'SALARY_LIST'),
('ACCOUNTANT', 'SALARY_APPROVE'),
-- Reports
('ACCOUNTANT', 'REPORT_VIEW'),
('ACCOUNTANT', 'REPORT_EXPORT');

-- Assign Permissions to DRIVER Role
INSERT INTO role_permissions (role_name, permission_name) VALUES
-- Trip (read own trips)
('DRIVER', 'TRIP_READ'),
('DRIVER', 'TRIP_LIST'),
-- Expense (create and view own expenses)
('DRIVER', 'EXPENSE_CREATE'),
('DRIVER', 'EXPENSE_READ'),
('DRIVER', 'EXPENSE_LIST'),
-- Salary (view own salary)
('DRIVER', 'SALARY_READ');

-- Assign Permissions to VIEWER Role
INSERT INTO role_permissions (role_name, permission_name) VALUES
('VIEWER', 'USER_READ'),
('VIEWER', 'USER_LIST'),
('VIEWER', 'DRIVER_READ'),
('VIEWER', 'DRIVER_LIST'),
('VIEWER', 'VEHICLE_READ'),
('VIEWER', 'VEHICLE_LIST'),
('VIEWER', 'ROUTE_READ'),
('VIEWER', 'ROUTE_LIST'),
('VIEWER', 'TRIP_READ'),
('VIEWER', 'TRIP_LIST'),
('VIEWER', 'EXPENSE_READ'),
('VIEWER', 'EXPENSE_LIST'),
('VIEWER', 'REPORT_VIEW');