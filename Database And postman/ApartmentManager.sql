-- Drop the database if it already exists
DROP DATABASE IF EXISTS apartment_management_system;
CREATE DATABASE apartment_management_system;
USE apartment_management_system;

-- Create users table for authentication
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'MANAGER', 'RESIDENT') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create apartments table
CREATE TABLE apartments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_number VARCHAR(10) NOT NULL,
    area FLOAT NOT NULL,
    num_rooms INT NOT NULL,
    created_by INT,
    updated_by INT,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id)
);

-- Create residents table and link to users
CREATE TABLE residents (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    id_card VARCHAR(20) NOT NULL,
    birth_year INT,
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    apartment_id INT,
    moved_in_date DATE,
    moved_out_date DATE,
    user_id INT,
    FOREIGN KEY (apartment_id) REFERENCES apartments(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create utilities table
CREATE TABLE utilities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_id INT,
    electricity_usage INT,
    water_usage INT,
    total_amount DECIMAL(10, 2),
    payment_date DATE,
    payment_status ENUM('PAID', 'UNPAID'),
    created_by INT,
    updated_by INT,
    FOREIGN KEY (apartment_id) REFERENCES apartments(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id)
);

-- Create additional_charges table
CREATE TABLE additional_charges (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_id INT,
    charge_description VARCHAR(255),
    amount DECIMAL(10, 2),
    charge_date DATE,
    created_by INT,
    updated_by INT,
    FOREIGN KEY (apartment_id) REFERENCES apartments(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id)
);

-- Create monthly_fees table
CREATE TABLE monthly_fees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_id INT,
    resident_id INT,
    fee_month DATE,
    email_sent BOOLEAN DEFAULT FALSE,
    created_by INT,
    updated_by INT,
    FOREIGN KEY (apartment_id) REFERENCES apartments(id),
    FOREIGN KEY (resident_id) REFERENCES residents(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id)
);

-- Create apartment_history table
CREATE TABLE apartment_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    resident_id INT,
    old_apartment_id INT,
    new_apartment_id INT,
    change_date DATE,
    created_by INT,
    updated_by INT,
    FOREIGN KEY (resident_id) REFERENCES residents(id),
    FOREIGN KEY (old_apartment_id) REFERENCES apartments(id),
    FOREIGN KEY (new_apartment_id) REFERENCES apartments(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id)
);

-- create table: token: Thêm bảng token
CREATE TABLE `token`(
	id						INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id					INT NOT NULL, -- User sở hữu token 
	`key` 					VARCHAR(100) NOT NULL UNIQUE,
    `type` 					ENUM('REFRESH_TOKEN','REGISTER','FORGOT_PASSWORD') NOT NULL, -- loại token
    expired_date			DATETIME NOT NULL,
    FOREIGN KEY (user_id) 	REFERENCES users(id) 
);

-- Create search_index view
CREATE VIEW search_index AS
SELECT
    r.id AS resident_id,
    r.name AS resident_name,
    a.apartment_number
FROM
    residents r
JOIN
    apartments a ON r.apartment_id = a.id;

-- Create apartment_details view
CREATE VIEW apartment_details AS
SELECT
    a.id,
    a.apartment_number,
    a.area,
    a.num_rooms,
    r.name AS resident_name,
    r.email,
    r.phone,
    r.id_card,
    r.birth_year,
    r.gender,
    r.moved_in_date,
    r.moved_out_date
FROM
    apartments a
LEFT JOIN
    residents r ON a.id = r.apartment_id;

-- password: 123456 (base64)
-- Insert initial data into users table
INSERT INTO users (username, email, `password`, role) VALUES
('admin', 'admin@example.com', '$2a$10$6J3zMOlyiDYvLpmLfHsyCuAJvGM2EYPYh6uNYKl.9PgWSiy9GhMy6', 'ADMIN'),
('manager', 'manager@example.com', '$2a$10$6J3zMOlyiDYvLpmLfHsyCuAJvGM2EYPYh6uNYKl.9PgWSiy9GhMy6', 'MANAGER'),
('resident_john', 'john_resident@example.com', '$2a$10$6J3zMOlyiDYvLpmLfHsyCuAJvGM2EYPYh6uNYKl.9PgWSiy9GhMy6', 'RESIDENT'),
('assistant', 'assistant@example.com', '$2a$10$6J3zMOlyiDYvLpmLfHsyCuAJvGM2EYPYh6uNYKl.9PgWSiy9GhMy6', 'MANAGER'),
('superadmin', 'superadmin@example.com', '$2a$10$6J3zMOlyiDYvLpmLfHsyCuAJvGM2EYPYh6uNYKl.9PgWSiy9GhMy6', 'ADMIN'),
('janedoe', 'jane.doe@example.com', '$2a$10$6J3zMOlyiDYvLpmLfHsyCuAJvGM2EYPYh6uNYKl.9PgWSiy9GhMy6', 'RESIDENT'),
('michael', 'michael@example.com', '$2a$10$6J3zMOlyiDYvLpmLfHsyCuAJvGM2EYPYh6uNYKl.9PgWSiy9GhMy6', 'RESIDENT'),
('lisa', 'lisa@example.com', '$2a$10$6J3zMOlyiDYvLpmLfHsyCuAJvGM2EYPYh6uNYKl.9PgWSiy9GhMy6', 'MANAGER');

-- Insert initial data into apartments table
INSERT INTO apartments (apartment_number, area, num_rooms, created_by, updated_by) VALUES
('A101', 70.5, 3, 1, 1),
('A102', 65.0, 2, 1, 1),
('A103', 80.0, 3, 1, 1),
('A104', 55.5, 2, 1, 1),
('B201', 90.0, 4, 1, 1),
('B202', 75.5, 3, 1, 1),
('B203', 60.0, 2, 1, 1),
('B204', 85.0, 4, 1, 1),
('C301', 95.0, 4, 1, 1),
('C302', 70.0, 3, 1, 1);

-- Insert initial data into residents table
INSERT INTO residents (name, email, phone, id_card, birth_year, gender, apartment_id, moved_in_date, moved_out_date, user_id) VALUES
('John Doe', 'john@example.com', '0123456789', 'ID123456', 1985, 'MALE', 1, '2020-01-15', NULL, 3),
('Jane Smith', 'jane@example.com', '0987654321', 'ID654321', 1990, 'FEMALE', 2, '2021-03-22', NULL, NULL),
('Alice Johnson', 'alice@example.com', '0123987654', 'ID987654', 1987, 'FEMALE', 3, '2019-07-30', NULL, NULL),
('Bob Brown', 'bob@example.com', '0234567890', 'ID456789', 1992, 'MALE', 4, '2022-05-14', NULL, NULL),
('Charlie Davis', 'charlie@example.com', '0345678901', 'ID345678', 1988, 'MALE', 5, '2021-12-01', NULL, NULL),
('Diana Evans', 'diana@example.com', '0456789012', 'ID234567', 1989, 'FEMALE', 6, '2023-06-21', NULL, NULL),
('Ethan Green', 'ethan@example.com', '0567890123', 'ID123890', 1991, 'MALE', 7, '2020-09-10', NULL, NULL),
('Fiona Harris', 'fiona@example.com', '0678901234', 'ID987123', 1986, 'FEMALE', 8, '2021-11-30', NULL, NULL),
('George White', 'george@example.com', '0789012345', 'ID654987', 1993, 'MALE', 9, '2022-08-20', NULL, NULL),
('Helen King', 'helen@example.com', '0890123456', 'ID321456', 1994, 'FEMALE', 10, '2019-04-25', NULL, NULL);

-- Insert initial data into utilities table
INSERT INTO utilities (apartment_id, electricity_usage, water_usage, total_amount, payment_date, payment_status, created_by, updated_by) VALUES
(1, 120, 15, 50.00, '2023-07-01', 'PAID', 1, 1),
(2, 100, 12, 45.00, '2023-07-02', 'PAID', 1, 1),
(3, 150, 20, 60.00, '2023-07-03', 'UNPAID', 1, 1),
(4, 90, 10, 40.00, '2023-07-04', 'PAID', 1, 1),
(5, 110, 18, 55.00, '2023-07-05', 'PAID', 1, 1),
(6, 130, 15, 50.00, '2023-07-06', 'UNPAID', 1, 1),
(7, 140, 17, 57.00, '2023-07-07', 'PAID', 1, 1),
(8, 115, 13, 48.00, '2023-07-08', 'PAID', 1, 1),
(9, 160, 19, 62.00, '2023-07-09', 'UNPAID', 1, 1),
(10, 125, 16, 52.00, '2023-07-10', 'PAID', 1, 1);

-- Insert initial data into additional_charges table
INSERT INTO additional_charges (apartment_id, charge_description, amount, charge_date, created_by, updated_by) VALUES
(1, 'Cleaning Fee', 20.00, '2023-07-01', 1, 1),
(2, 'Elevator Maintenance', 15.00, '2023-07-02', 1, 1),
(3, 'Parking Fee', 10.00, '2023-07-03', 1, 1),
(4, 'Security Fee', 25.00, '2023-07-04', 1, 1),
(5, 'Swimming Pool Maintenance', 30.00, '2023-07-05', 1, 1),
(6, 'Gym Fee', 40.00, '2023-07-06', 1, 1),
(7, 'Laundry Fee', 35.00, '2023-07-07', 1, 1),
(8, 'Internet Fee', 45.00, '2023-07-08', 1, 1),
(9, 'Trash Removal', 18.00, '2023-07-09', 1, 1),
(10, 'Common Area Lighting', 22.00, '2023-07-10', 1, 1);

-- Insert initial data into monthly_fees table
INSERT INTO monthly_fees (apartment_id, resident_id, fee_month, email_sent, created_by, updated_by) VALUES
(1, 1, '2023-07-01', TRUE, 1, 1),
(2, 2, '2023-07-01', TRUE, 1, 1),
(3, 3, '2023-07-01', FALSE, 1, 1),
(4, 4, '2023-07-01', TRUE, 1, 1),
(5, 5, '2023-07-01', TRUE, 1, 1),
(6, 6, '2023-07-01', FALSE, 1, 1),
(7, 7, '2023-07-01', TRUE, 1, 1),
(8, 8, '2023-07-01', TRUE, 1, 1),
(9, 9, '2023-07-01', FALSE, 1, 1),
(10, 10, '2023-07-01', TRUE, 1, 1);

DELIMITER //

CREATE TRIGGER update_resident_and_history
AFTER UPDATE ON residents
FOR EACH ROW
BEGIN
    -- Kiểm tra nếu cư dân chuyển sang căn hộ mới
    IF NEW.apartment_id != OLD.apartment_id THEN
        -- Thêm một bản ghi vào bảng apartment_history
        INSERT INTO apartment_history (resident_id, old_apartment_id, new_apartment_id, change_date, created_by, updated_by)
        VALUES (NEW.id, OLD.apartment_id, NEW.apartment_id, CURDATE(), COALESCE(NEW.user_id, 1), COALESCE(NEW.user_id, 1));
        
        -- Cập nhật moved_out_date cho căn hộ cũ và moved_in_date cho căn hộ mới
        IF OLD.apartment_id IS NOT NULL THEN
            UPDATE residents
            SET moved_out_date = CURDATE()
            WHERE id = NEW.id;
        END IF;
        
        IF NEW.apartment_id IS NOT NULL THEN
            UPDATE residents
            SET moved_in_date = CURDATE()
            WHERE id = NEW.id;
        END IF;
    END IF;
END; //

DELIMITER ;

DELIMITER //

CREATE TRIGGER after_resident_delete
AFTER DELETE ON residents
FOR EACH ROW
BEGIN
    -- Thêm một bản ghi vào bảng apartment_history trước khi xóa
    INSERT INTO apartment_history (resident_id, old_apartment_id, new_apartment_id, change_date, created_by, updated_by)
    VALUES (OLD.id, OLD.apartment_id, NULL, CURDATE(), COALESCE(OLD.user_id, 1), COALESCE(OLD.user_id, 1));
END; //

DELIMITER ;
