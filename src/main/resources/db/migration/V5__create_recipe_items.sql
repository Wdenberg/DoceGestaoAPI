CREATE TABLE recipe_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipe_id UUID NOT NULL,
    ingredient_id UUID NOT NULL,
    quantity_used NUMERIC(12,3) NOT NULL,
    unit_measure VARCHAR(30) NOT NULL,
    calculated_cost NUMERIC(12,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT fk_recipe_items_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    CONSTRAINT fk_recipe_items_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

CREATE INDEX idx_recipe_items_recipe_id ON recipe_items(recipe_id);