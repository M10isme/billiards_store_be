-- Flyway migration: add customer_username column to orders
ALTER TABLE orders ADD COLUMN IF NOT EXISTS customer_username VARCHAR(50);

