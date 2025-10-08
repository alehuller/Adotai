CREATE TABLE IF NOT EXISTS agendamento (
    id BIGSERIAL PRIMARY KEY,
    animal_id BIGSERIAL NOT NULL REFERENCES animal(id),
    usuario_id BIGSERIAL NOT NULL REFERENCES usuario(id),
    ong_id BIGSERIAL NOT NULL REFERENCES ong(id),
    data_hora TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    observacao VARCHAR(255) NOT NULL,
    CONSTRAINT unique_animal_data_hora UNIQUE (animal_id, data_hora)
);