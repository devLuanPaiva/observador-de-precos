ALTER TABLE products
ADD COLUMN user_id UUID NOT NULL;

ALTER TABLE products
ADD CONSTRAINT fk_products_user
FOREIGN KEY (user_id)
REFERENCES users(id);

CREATE INDEX idx_products_user
ON products(user_id);