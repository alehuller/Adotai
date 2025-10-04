CREATE TABLE IF NOT EXISTS avaliacao (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGSERIAL NOT NULL REFERENCES usuario(id),
    ong_id BIGSERIAL NOT NULL REFERENCES ong(id),
    nota INT NOT NULL CHECK (nota BETWEEN 1 AND 5),
    comentario VARCHAR(1000),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_usuario_ong UNIQUE (usuario_id, ong_id)
)