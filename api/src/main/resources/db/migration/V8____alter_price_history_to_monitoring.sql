ALTER TABLE price_history
DROP CONSTRAINT fk_price_history_product;

ALTER TABLE price_history
DROP COLUMN product_id;

ALTER TABLE price_history
ADD COLUMN monitoring_id UUID NOT NULL;

ALTER TABLE price_history
ADD CONSTRAINT fk_price_history_monitoring
    FOREIGN KEY (monitoring_id)
    REFERENCES monitoring(id);