CREATE TABLE IF NOT EXISTS adocao (
    idAdocao SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    status INT NOT NULL,
    usuario_id INT NOT NULL REFERENCES usuario(id),
    animal_id INT NOT NULL REFERENCES animal(id)
);