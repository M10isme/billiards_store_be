-- Backfill customer_username on existing orders by matching customerName to users.fullName
-- NOTE: This is best-effort and assumes full_name matches; review before running in production.
UPDATE orders
SET customer_username = u.username
FROM users u
WHERE orders.customer_name = u.full_name;

