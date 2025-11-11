-- V2__Create_Vehicle_And_Driver_Tables.sql
-- Tạo các bảng liên quan đến phương tiện và tài xế

-- Bảng vehicle_types
CREATE TABLE vehicle_types (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE,
    name VARCHAR(255),
    max_payload DECIMAL(15, 2),
    description VARCHAR(1000),
    is_active BOOLEAN DEFAULT TRUE
);

-- Bảng vehicles
CREATE TABLE vehicles (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(50),
    vehicle_type_id BIGINT,
    brand VARCHAR(100),
    model VARCHAR(100),
    manufacture_year INTEGER,
    color VARCHAR(50),
    vin VARCHAR(50) UNIQUE,
    engine_number VARCHAR(50),
    registration_date DATE,
    inspection_expiry_date DATE,
    insurance_expiry_date DATE,
    status VARCHAR(50),
    purchase_date DATE,
    note VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY (vehicle_type_id) REFERENCES vehicle_types(id)
);

CREATE INDEX idx_license_plate ON vehicles(license_plate);
CREATE INDEX idx_status ON vehicles(status);

-- Bảng drivers
CREATE TABLE drivers (
    id BIGINT PRIMARY KEY,
    driver_code VARCHAR(50) UNIQUE,
    license_number VARCHAR(50) UNIQUE,
    license_class VARCHAR(10) NOT NULL,
    license_issue_date DATE NOT NULL,
    license_expiry_date DATE NOT NULL,
    years_of_experience INTEGER,
    employment_status VARCHAR(50) NOT NULL,
    base_salary DECIMAL(15, 2),
    note VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_driver_code ON drivers(driver_code);
CREATE INDEX idx_license_number ON drivers(license_number);