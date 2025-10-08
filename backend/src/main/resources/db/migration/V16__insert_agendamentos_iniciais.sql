INSERT INTO
    agendamento (animal_id, usuario_id, ong_id, data_hora, status, observacao)
VALUES
    (
        1, -- Animal 1 (ONG 1)
        1, -- Usuario 1
        1, -- Ong 1
        TIMESTAMP '2025-10-12 14:00:00',
        'AGENDADO',
        'Visita para conhecer o cachorro Thor.'
    ),
    (
        2, -- Animal 2 (ONG 2)
        2, -- Usuario 2
        2, -- Ong 2
        TIMESTAMP '2025-10-13 10:30:00',
        'AGENDADO',
        'Agendamento para conhecer a gata Luna.'
    ),
    (
        3, -- Animal 3 (ONG 3)
        3, -- Usuario 3
        3, -- Ong 3
        TIMESTAMP '2025-10-14 16:00:00',
        'AGENDADO',
        'Visita para avaliar adoção do cachorro Max.'
    ),
    (
        1, -- Animal 1 (mesma ONG 1)
        4, -- Usuario 4
        1, -- Ong 1
        TIMESTAMP '2025-10-15 09:00:00',
        'AGENDADO',
        'Segunda visita agendada para o mesmo animal, sem sobreposição.'
    );
