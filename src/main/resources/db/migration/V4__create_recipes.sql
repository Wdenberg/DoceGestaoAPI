CREATE TABLE recipes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    image_url TEXT,
    yield_amount NUMERIC(12,2) NOT NULL DEFAULT 1,
    yield_unit VARCHAR(30) NOT NULL DEFAULT 'unidade',
    category VARCHAR(100),
    profit_margin NUMERIC(12,2) NOT NULL DEFAULT 45,
    energy_cost NUMERIC(12,2) NOT NULL DEFAULT 0,
    labor_cost NUMERIC(12,2) NOT NULL DEFAULT 0,
    packaging_cost NUMERIC(12,2) NOT NULL DEFAULT 0,
    transport_cost NUMERIC(12,2) NOT NULL DEFAULT 0,
    ingredients_cost NUMERIC(12,2) NOT NULL DEFAULT 0,
    production_cost NUMERIC(12,2) NOT NULL DEFAULT 0,
    suggested_price NUMERIC(12,2) NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT fk_recipes_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_recipes_user_id ON recipes(user_id);
CREATE INDEX idx_recipes_name_user_id ON recipes(user_id, name);