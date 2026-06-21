CREATE TYPE alert_type AS ENUM (
    'PRICE_DROP',
    'TARGET_REACHED',
    'BACK_IN_STOCK',
    'PROMOTION'
);


CREATE TABLE alerts (

    id UUID PRIMARY KEY,

    monitoring_id UUID NOT NULL,

    type alert_type DEFault 'PRICE_DROP',

    message VARCHAR(500) NOT NULL,

    read BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    created_by UUID,

    updated_by UUID,

    CONSTRAINT fk_alert_monitoring
        FOREIGN KEY (monitoring_id)
        REFERENCES monitoring(id)
);