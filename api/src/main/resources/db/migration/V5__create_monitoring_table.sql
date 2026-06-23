CREATE TABLE monitoring (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    product_id UUID NOT NULL,

    target_price NUMERIC(10,2),

    notify_stock BOOLEAN DEFAULT TRUE,

    notify_promotion BOOLEAN DEFAULT TRUE,

    active BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    created_by UUID,
    updated_by UUID,

    CONSTRAINT fk_monitoring_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_monitoring_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);