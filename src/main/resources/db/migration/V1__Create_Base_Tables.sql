-- V1__Create_Base_Tables.sql
-- Tạo các bảng cơ bản: users, roles, permissions

-- Bảng users
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    full_name VARCHAR(255),
    id_number VARCHAR(50),
    date_of_birth DATE,
    address VARCHAR(500),
    avatar_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE INDEX idx_username ON users(username);

-- Bảng roles
CREATE TABLE roles (
    role_name VARCHAR(50) PRIMARY KEY,
    description VARCHAR(500)
);

-- Bảng permissions
CREATE TABLE permissions (
    permission_name VARCHAR(100) PRIMARY KEY,
    description VARCHAR(500)
);

-- Bảng role_permissions (Many-to-Many)
CREATE TABLE role_permissions (
    role_name VARCHAR(50) NOT NULL,
    permission_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (role_name, permission_name),
    FOREIGN KEY (role_name) REFERENCES roles(role_name) ON DELETE CASCADE,
    FOREIGN KEY (permission_name) REFERENCES permissions(permission_name) ON DELETE CASCADE
);

-- Bảng user_roles (Many-to-Many)
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_name) REFERENCES roles(role_name) ON DELETE CASCADE
);

-- Bảng invalidated_tokens
CREATE TABLE invalidated_tokens (
    token_id VARCHAR(36) PRIMARY KEY,
    expiry_time TIMESTAMP NOT NULL
);