CREATE TABLE price_history (
    id UUID PRIMARY KEY,

    product_id UUID NOT NULL,

    price NUMERIC(10,2) NOT NULL,

    available BOOLEAN NOT NULL,

    checked_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_price_history_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);