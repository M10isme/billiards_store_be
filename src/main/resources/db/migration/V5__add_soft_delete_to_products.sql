-- Add deleted_at column to products table for soft delete functionality
ALTER TABLE products 
ADD COLUMN deleted_at TIMESTAMP NULL;

-- Add index for better performance on deleted_at queries
CREATE INDEX idx_products_deleted_at ON products(deleted_at);

-- Add index for active status queries
CREATE INDEX idx_products_active ON products(active);

-- Add comment to explain the soft delete approach
COMMENT ON COLUMN products.deleted_at IS 'Timestamp when product was soft deleted. NULL means product is active.';
COMMENT ON COLUMN products.active IS 'Boolean flag for product status. Used together with deleted_at for soft delete functionality.';