CREATE TABLE IF NOT EXISTS animal (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(80) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raca VARCHAR(50) NOT NULL,
    data_nascimento DATE NOT NULL,
    foto VARCHAR(255),
    descricao TEXT,
    porte VARCHAR(50) NOT NULL,
    sexo VARCHAR(10) NOT NULL,
    status VARCHAR(30) NOT NULL,
    ong_id BIGSERIAL NOT NULL REFERENCES ong(id)
);