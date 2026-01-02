-- Drop tables in reverse order of dependency
DROP TABLE IF EXISTS users;

-- Create tables
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

-- Insert admin user
-- INSERT INTO users (username, password, created_at, created_by)
-- VALUES ('admin', '', CURRENT_TIMESTAMP, 'system');