CREATE TABLE products (
    id UUID PRIMARY KEY,

    title VARCHAR(120) NOT NULL,

    url VARCHAR(500),

    image_url VARCHAR(500),

    store VARCHAR(120),

    sku VARCHAR(120),

    current_price NUMERIC(10,2),

    available BOOLEAN DEFAULT TRUE,

    active BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    created_by UUID,
    updated_by UUID
);