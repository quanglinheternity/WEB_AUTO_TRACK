-- V4__Create_Expense_Tables.sql
-- Tạo các bảng liên quan đến chi phí

-- Bảng expense_categories
CREATE TABLE expense_categories (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    category_group VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE NOT NULL
);

-- Bảng expenses
CREATE TABLE expenses (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    expense_code VARCHAR(50) UNIQUE,
    trip_id BIGINT,
    category_id BIGINT NOT NULL,
    amount DECIMAL(15, 2),
    expense_date DATE,
    location VARCHAR(500),
    description VARCHAR(1000),
    receipt_number VARCHAR(100),
    attachment_url VARCHAR(500),
    status VARCHAR(50) NOT NULL,
    driver_by_id BIGINT,
    manager_approved_at TIMESTAMP,
    manager_approved_by_id BIGINT,
    manager_note VARCHAR(1000),
    accountant_approved_at TIMESTAMP,
    accountant_approved_by_id BIGINT,
    accountant_note VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY (trip_id) REFERENCES trips(id),
    FOREIGN KEY (category_id) REFERENCES expense_categories(id),
    FOREIGN KEY (driver_by_id) REFERENCES users(id),
    FOREIGN KEY (manager_approved_by_id) REFERENCES users(id),
    FOREIGN KEY (accountant_approved_by_id) REFERENCES users(id)
);

CREATE INDEX idx_trip_id ON expenses(trip_id);
CREATE INDEX idx_expense_status ON expenses(status);
CREATE INDEX idx_expense_date ON expenses(expense_date);