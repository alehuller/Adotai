CREATE TABLE IF NOT EXISTS adocao (
    id BIGSERIAL PRIMARY KEY,
    data_adocao DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    usuario_id BIGSERIAL NOT NULL REFERENCES usuario(id),
    animal_id BIGSERIAL NOT NULL REFERENCES animal(id)
);