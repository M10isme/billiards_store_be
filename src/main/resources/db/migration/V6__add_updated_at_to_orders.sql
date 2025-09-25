ALTER TABLE orders ADD COLUMN updated_at TIMESTAMP;

-- Set initial value for existing records
UPDATE orders SET updated_at = created_at WHERE updated_at IS NULL;