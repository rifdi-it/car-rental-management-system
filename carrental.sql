-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 26, 2026 at 07:57 AM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `carrental`
--

-- --------------------------------------------------------

--
-- Table structure for table `cars`
--

CREATE TABLE `cars` (
  `car_id` int(11) NOT NULL,
  `plate_no` varchar(30) NOT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `fuel_type` varchar(20) DEFAULT NULL,
  `daily_rate` decimal(10,2) DEFAULT NULL,
  `status` enum('Available','Rented','Maintenance') DEFAULT 'Available',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cars`
--

INSERT INTO `cars` (`car_id`, `plate_no`, `brand`, `model`, `year`, `fuel_type`, `daily_rate`, `status`, `created_at`) VALUES
(1, 'WP-TEST-001', 'TOYOTA', 'AXIO', 2018, 'PETROL', '7500.00', 'Available', '2026-01-21 16:32:13'),
(2, 'BFS-3430', 'BAJAJ', 'PULSER', 2017, 'PATROL', '2000.00', 'Available', '2026-01-21 16:41:17'),
(3, '59-4408', 'NISSAN', 'CARAVAN', 1991, 'DISEL', '8000.00', 'Available', '2026-01-21 16:42:21'),
(4, '48-0519', 'TOYOTA', 'DYNA', 1982, 'DISEL', '10000.00', 'Available', '2026-01-21 16:43:07'),
(5, 'WP-CAR-1234', 'TOYOTA', 'AXIO', 2019, 'PETROL', '7500.00', 'Available', '2026-01-21 16:50:35'),
(6, 'WP-CAR-2345', 'TOYOTA', 'PREMIO', 2018, 'PETROL', '9000.00', 'Available', '2026-01-21 16:50:35'),
(7, 'CP-CAR-3456', 'NISSAN', 'SUNNY', 2017, 'PETROL', '6500.00', 'Available', '2026-01-21 16:50:35'),
(8, 'SP-CAR-4567', 'HONDA', 'CIVIC', 2016, 'PETROL', '8000.00', 'Available', '2026-01-21 16:50:35'),
(9, 'WP-CAR-5678', 'SUZUKI', 'WAGON R', 2020, 'PETROL', '5000.00', 'Available', '2026-01-21 16:50:35'),
(10, 'NW-CAR-6789', 'MAZDA', 'DEMIO', 2019, 'PETROL', '6200.00', 'Available', '2026-01-21 16:50:35'),
(11, 'CP-CAR-7890', 'HYUNDAI', 'ELANTRA', 2018, 'PETROL', '7400.00', 'Available', '2026-01-21 16:50:35'),
(12, 'WP-CAR-8901', 'KIA', 'RIO', 2019, 'PETROL', '6000.00', 'Available', '2026-01-21 16:50:35'),
(13, 'WP-VAN-1122', 'TOYOTA', 'HIACE', 2017, 'DIESEL', '12000.00', 'Available', '2026-01-21 16:50:35'),
(14, 'CP-VAN-2233', 'NISSAN', 'CARAVAN', 2016, 'DIESEL', '9500.00', 'Available', '2026-01-21 16:50:35'),
(15, 'SP-VAN-3344', 'SUZUKI', 'EVERY', 2018, 'PETROL', '5500.00', 'Available', '2026-01-21 16:50:35'),
(16, 'WP-VAN-4455', 'MITSUBISHI', 'DELICA', 2015, 'DIESEL', '8800.00', 'Available', '2026-01-21 16:50:35'),
(17, 'NW-VAN-5566', 'NISSAN', 'VANETTE', 2014, 'DIESEL', '9000.00', 'Available', '2026-01-21 16:50:35'),
(18, 'CP-VAN-6677', 'TOYOTA', 'LITEACE', 2016, 'DIESEL', '9200.00', 'Available', '2026-01-21 16:50:35'),
(19, 'WP-LOR-7788', 'ISUZU', 'ELF', 2016, 'DIESEL', '11000.00', 'Available', '2026-01-21 16:50:35'),
(20, 'CP-LOR-8899', 'TATA', 'ACE', 2019, 'DIESEL', '7000.00', 'Available', '2026-01-21 16:50:35'),
(21, 'SP-LOR-9900', 'MITSUBISHI', 'CANTER', 2015, 'DIESEL', '11500.00', 'Available', '2026-01-21 16:50:35'),
(22, 'WP-BIK-1111', 'HONDA', 'CD-125', 2020, 'PETROL', '2000.00', 'Available', '2026-01-21 16:50:35'),
(23, 'CP-BIK-2222', 'BAJAJ', 'PULSAR 150', 2021, 'PETROL', '2500.00', 'Available', '2026-01-21 16:50:35'),
(24, 'SP-BIK-3333', 'YAMAHA', 'FZ', 2019, 'PETROL', '2800.00', 'Available', '2026-01-21 16:50:35'),
(25, 'NW-BIK-4444', 'TVS', 'APACHE', 2020, 'PETROL', '2600.00', 'Available', '2026-01-21 16:50:35'),
(26, 'WP-BIK-5555', 'SUZUKI', 'GIXXER', 2021, 'PETROL', '2900.00', 'Available', '2026-01-21 16:50:35'),
(27, 'WP-3W-6666', 'BAJAJ', 'THREEWHEEL', 2020, 'PETROL', '3000.00', 'Available', '2026-01-21 16:50:35'),
(28, 'CP-3W-7777', 'TVS', 'KING', 2019, 'PETROL', '2800.00', 'Available', '2026-01-21 16:50:35');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `cust_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `nic` varchar(50) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `license_no` varchar(50) DEFAULT NULL,
  `license_image` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`cust_id`, `name`, `nic`, `phone`, `address`, `license_no`, `license_image`, `description`, `created_at`) VALUES
(1, 'Sample Customer', NULL, '0770000000', 'Sri Lanka', NULL, NULL, NULL, '2026-01-21 16:32:13'),
(2, 'Mohommadhu Riyal Muhammad Rifdhy', '200026904943', '0752047031', 'Gallegama,Kekunagolla', '344454', 'C:\\Users\\ASUS\\CarRental\\uploads\\1769013598798_WIN_20260121_22_07_22_Pro.jpg', 'New Customer', '2026-01-21 16:40:00');

-- --------------------------------------------------------

--
-- Table structure for table `rentals`
--

CREATE TABLE `rentals` (
  `rent_id` int(11) NOT NULL,
  `cust_id` int(11) NOT NULL,
  `car_id` int(11) NOT NULL,
  `rent_date` date NOT NULL,
  `expected_return_date` date NOT NULL,
  `actual_return_date` date DEFAULT NULL,
  `total_amount` decimal(12,2) DEFAULT 0.00,
  `advance` decimal(12,2) DEFAULT 0.00,
  `status` enum('Rented','Returned','Cancelled') DEFAULT 'Rented'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `rentals`
--

INSERT INTO `rentals` (`rent_id`, `cust_id`, `car_id`, `rent_date`, `expected_return_date`, `actual_return_date`, `total_amount`, `advance`, `status`) VALUES
(1, 2, 2, '2026-01-21', '2026-01-23', '2026-01-21', '5000.00', '1000.00', 'Returned');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','staff') DEFAULT 'staff',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`, `created_at`) VALUES
(1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin', '2026-01-21 16:32:13'),
(2, 'nahla', '$2a$12$t49lpqPiqb.mC/poQ1yWFORIXbJlpPV7hJ5wHwyMMgJNaL8R/Dly.', 'admin', '2026-01-21 16:33:05'),
(4, 'user', '$2a$12$ePE0uLPmn99Cdy1tcyzNTeZ4D2VtCwm7OrYavWKsyu3Rwj/fjJ5m.', 'admin', '2026-02-09 17:04:06'),
(5, 'rifdhy', '$2a$12$dcuP/.OjPLQIevFjuJwAwuX1V6Nev71hOZwytsyUbLIwk7w.EgiRu', 'admin', '2026-04-21 14:48:09');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cars`
--
ALTER TABLE `cars`
  ADD PRIMARY KEY (`car_id`),
  ADD UNIQUE KEY `plate_no` (`plate_no`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`cust_id`);

--
-- Indexes for table `rentals`
--
ALTER TABLE `rentals`
  ADD PRIMARY KEY (`rent_id`),
  ADD KEY `fk_rent_cust` (`cust_id`),
  ADD KEY `fk_rent_car` (`car_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cars`
--
ALTER TABLE `cars`
  MODIFY `car_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `cust_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `rentals`
--
ALTER TABLE `rentals`
  MODIFY `rent_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `rentals`
--
ALTER TABLE `rentals`
  ADD CONSTRAINT `fk_rent_car` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`),
  ADD CONSTRAINT `fk_rent_cust` FOREIGN KEY (`cust_id`) REFERENCES `customers` (`cust_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
