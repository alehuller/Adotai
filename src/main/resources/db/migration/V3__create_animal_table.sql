CREATE TABLE IF NOT EXISTS animal (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(45) NOT NULL,
    especie VARCHAR(45) NOT NULL,
    raca VARCHAR(45),
    data_nascimento DATE,
    foto VARCHAR(255),
    descricao TEXT,
    porte VARCHAR(45),
    sexo VARCHAR(45),
    status VARCHAR(50) NOT NULL,
    ong_id BIGSERIAL NOT NULL REFERENCES ong(id)
);