-- -----------------------------------------------------
-- Hotel Booking System Database Script
-- -----------------------------------------------------
-- This script creates the database schema for the Hotel Booking System.sq

-- Drop the database if it already exists (optional, use with caution)
DROP DATABASE IF EXISTS hotel_booking;

-- Create new database
CREATE DATABASE hotel_booking;
USE hotel_booking;

-- -----------------------------------------------------
-- Table: customers
-- -----------------------------------------------------
CREATE TABLE `customers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `phone` BIGINT NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`),
  UNIQUE KEY `unique_phone` (`phone`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table: managers
-- -----------------------------------------------------
CREATE TABLE `managers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table: rooms
-- -----------------------------------------------------
CREATE TABLE `rooms` (
  `room_number` VARCHAR(50) NOT NULL,
  `type` VARCHAR(50) DEFAULT NULL,
  `price` DOUBLE DEFAULT NULL,
  `is_available` TINYINT(1) DEFAULT '1',
  PRIMARY KEY (`room_number`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table: bookings
-- -----------------------------------------------------
CREATE TABLE `bookings` (
  `booking_id` INT NOT NULL AUTO_INCREMENT,
  `customer_id` INT NOT NULL,
  `room_number` VARCHAR(50) NOT NULL,
  `days` INT DEFAULT NULL,
  `total_amount` DOUBLE DEFAULT NULL,
  `booked_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `status` VARCHAR(20) DEFAULT 'BOOKED',
  PRIMARY KEY (`booking_id`),
  FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`room_number`) REFERENCES `rooms` (`room_number`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Insert sample data (optional, for testing)
-- -----------------------------------------------------
INSERT INTO managers (username, password) VALUES ('admin', 'admin123');

INSERT INTO rooms (room_number, type, price, is_available) VALUES
('101', 'Single', 1500, 1),
('102', 'Double', 2500, 1),
('103', 'Suite', 5000, 1);

-- End of Script
