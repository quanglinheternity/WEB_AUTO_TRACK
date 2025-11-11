-- V5__Create_Salary_Report_Table.sql
-- Tạo bảng báo cáo lương

-- Bảng salary_reports
CREATE TABLE salary_reports (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    report_month VARBINARY(255) NOT NULL,
    driver_id BIGINT NOT NULL,
    total_trips INTEGER NOT NULL,
    total_distance DECIMAL(15, 2),
    base_salary DECIMAL(15, 2),
    trip_bonus DECIMAL(15, 2),
    allowance DECIMAL(15, 2),
    deduction DECIMAL(15, 2),
    total_salary DECIMAL(15, 2) NOT NULL,
    is_paid BOOLEAN DEFAULT FALSE NOT NULL,
    paid_at TIMESTAMP,
    note VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY (driver_id) REFERENCES drivers(id)
);

CREATE INDEX idx_driver_month ON salary_reports(driver_id, report_month);