CREATE TABLE funcionarios (
    id BIGSERIAL PRIMARY KEY,
    nome_completo VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    numero_de_telefone VARCHAR(20),

    logradouro VARCHAR(150),
    bairro VARCHAR(100),
    cep VARCHAR(8),
    cidade VARCHAR(100),
    uf VARCHAR(2),
    numero INTEGER,
    complemento VARCHAR(100),

    usuario_id BIGINT UNIQUE,
    CONSTRAINT fk_funcionario_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);