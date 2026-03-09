CREATE TABLE chamados (
    id BIGINT PRIMARY KEY,
    equipamento_id BIGINT,
    solicitante_id BIGINT NOT NULL,
    tecnico_id BIGINT,
    tipo_problema VARCHAR(100) NOT NULL,
    descricao_detalhada TEXT NOT NULL,
    localizacao_exata VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    data_abertura TIMESTAMP NOT NULL,
    data_resolucao TIMESTAMP,

    CONSTRAINT fk_chamado_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamentos(id),
    CONSTRAINT fk_chamado_solicitante FOREIGN KEY (solicitante_id) REFERENCES funcionarios(id),
    CONSTRAINT fk_chamado_tecnico FOREIGN KEY (tecnico_id) REFERENCES funcionarios(id)
);