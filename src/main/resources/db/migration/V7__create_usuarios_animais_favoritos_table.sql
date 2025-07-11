CREATE TABLE IF NOT EXISTS usuario_animais_favoritos (
    usuario_id BIGINT NOT NULL,
    animal_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, animal_id),
    CONSTRAINT fk_usuario_animais_favoritos_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_usuario_animais_favoritos_animal FOREIGN KEY (animal_id) REFERENCES animal(id) ON DELETE CASCADE
);
-- Índice para melhorar consultas por animal
CREATE INDEX IF NOT EXISTS idx_usuario_animais_favoritos_animal_id ON usuario_animais_favoritos(animal_id);