-- Flyway migration: add address column to users
ALTER TABLE users ADD COLUMN IF NOT EXISTS address VARCHAR(255);

