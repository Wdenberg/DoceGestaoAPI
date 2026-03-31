CREATE TABLE clients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(30),
    email VARCHAR(120),
    address TEXT,
    notes TEXT,
    total_orders INTEGER NOT NULL DEFAULT 0,
    total_spent NUMERIC(12,2) NOT NULL DEFAULT 0,
    last_order_at TIMESTAMP WITH TIME ZONE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT fk_clients_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_clients_user_id ON clients(user_id);
CREATE INDEX idx_clients_name_user_id ON clients(user_id, name);