CREATE TABLE reservas (
    id BIGSERIAL PRIMARY KEY,
    funcionario_id BIGINT NOT NULL,
    equipamento_id BIGINT NOT NULL,

    data_agendamento TIMESTAMP NOT NULL, -- Data e hora exata em que ele clicou em "Reservar"
    data_prevista_retirada DATE NOT NULL, -- Apenas o dia
    data_prevista_devolucao DATE NOT NULL, -- Apenas o dia
    data_devolucao_real TIMESTAMP, -- Pode ser nulo até ele devolver

    status VARCHAR(50) NOT NULL,

    CONSTRAINT fk_reserva_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id),
    CONSTRAINT fk_reserva_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamentos(id)
);