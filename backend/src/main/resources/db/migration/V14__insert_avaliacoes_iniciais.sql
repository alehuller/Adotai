INSERT INTO
    avaliacao (usuario_id, ong_id, nota, comentario, data_criacao)
VALUES
    (
        1, -- Usuario 1
        1, -- Ong 1
        5,
        'Ótima ONG, muito atenciosos no processo de adoção!',
        CURRENT_TIMESTAMP
    ),
    (
        2, -- Usuario 2
        2, -- Ong 2
        4,
        'Bom atendimento, só demoraram um pouco para responder.',
        CURRENT_TIMESTAMP
    ),
    (
        3, -- Usuario 3
        3, -- Ong 3
        5,
        'Excelente trabalho de resgate e cuidado com os animais.',
        CURRENT_TIMESTAMP
    );
