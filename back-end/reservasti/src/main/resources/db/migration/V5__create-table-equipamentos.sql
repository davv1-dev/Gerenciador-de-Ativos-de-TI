CREATE TABLE equipamentos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    numero_patrimonio VARCHAR(50) NOT NULL UNIQUE,

    status VARCHAR(50) NOT NULL,

    categoria_id BIGINT NOT NULL,

    CONSTRAINT fk_equipamento_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);