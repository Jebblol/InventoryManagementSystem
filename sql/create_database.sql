-- ============================================================
-- SQL Script: Create inventory_db database and products table
-- Tool: Run this script in MySQL Workbench
-- ============================================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS inventory_db;

-- Step 2: Select the database
USE inventory_db;

-- Step 3: Create the products table
CREATE TABLE IF NOT EXISTS products (
    product_id VARCHAR(20) PRIMARY KEY,   -- Unique product identifier
    name       VARCHAR(100) NOT NULL,      -- Product name
    category   VARCHAR(50)  NOT NULL,      -- Product category
    price      DOUBLE       NOT NULL,      -- Product price
    quantity   INT          NOT NULL,      -- Stock quantity
    is_perishable TINYINT(1) DEFAULT 0,   -- 0 = regular, 1 = perishable
    expiry_date   VARCHAR(20) DEFAULT NULL -- Expiry date for perishable products (YYYY-MM-DD)
);

-- Step 4: Insert sample data (optional)
INSERT INTO products (product_id, name, category, price, quantity, is_perishable, expiry_date) VALUES
('P001', 'Laptop',       'Electronics', 999.99,  10, 0, NULL),
('P002', 'Wireless Mouse','Electronics',  29.99,  50, 0, NULL),
('P003', 'Fresh Milk',   'Dairy',         3.49,  100, 1, '2026-04-20'),
('P004', 'Bread',        'Bakery',        2.99,  200, 1, '2026-04-15'),
('P005', 'Notebook',     'Stationery',    4.99,  300, 0, NULL);
