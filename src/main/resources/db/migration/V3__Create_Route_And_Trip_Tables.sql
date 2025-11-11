-- V3__Create_Route_And_Trip_Tables.sql
-- Tạo các bảng liên quan đến tuyến đường và chuyến đi

-- Bảng routes
CREATE TABLE routes (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE,
    name VARCHAR(200) NOT NULL,
    origin VARCHAR(200) NOT NULL,
    destination VARCHAR(200) NOT NULL,
    distance_km DECIMAL(10, 2) NOT NULL,
    estimated_duration_hours DECIMAL(5, 2),
    estimated_fuel_cost DECIMAL(15, 2),
    description VARCHAR(1000),
    is_active BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

-- Bảng trips
CREATE TABLE trips (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    trip_code VARCHAR(50) UNIQUE,
    route_id BIGINT,
    vehicle_id BIGINT,
    driver_id BIGINT,
    departure_time TIMESTAMP,
    estimated_arrival_time TIMESTAMP,
    actual_arrival_time TIMESTAMP,
    status VARCHAR(50),
    cargo_description VARCHAR(1000),
    cargo_weight DECIMAL(15, 2),
    created_by BIGINT,
    approved_at TIMESTAMP,
    approved_by BIGINT,
    approval_status BOOLEAN,
    completed_at TIMESTAMP,
    cancelled_by BIGINT,
    cancelled_at TIMESTAMP,
    cancellation_reason VARCHAR(1000),
    note VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    FOREIGN KEY (route_id) REFERENCES routes(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    FOREIGN KEY (driver_id) REFERENCES drivers(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (approved_by) REFERENCES users(id),
    FOREIGN KEY (cancelled_by) REFERENCES users(id)
);

CREATE INDEX idx_trip_code ON trips(trip_code);
CREATE INDEX idx_trip_status ON trips(status);
CREATE INDEX idx_departure ON trips(departure_time);