CREATE TABLE IF NOT EXISTS animal (
    id INT PRIMARY KEY,
    nome VARCHAR(45) NOT NULL,
    especie VARCHAR(45) NOT NULL,
    raca VARCHAR(45),
    data_nascimento DATE,
    foto_perfil VARCHAR(255),
    fotos_galeria LONGTEXT,
    descricao LONGTEXT,
    porte VARCHAR(45),
    sexo VARCHAR(45),
    status VARCHAR(50) NOT NULL,
    ong_id INT NOT NULL REFERENCES ong(id)
);