CREATE TABLE refresh_token (
    id UUID PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    usuario_id BIGINT NOT NULL,
    data_expiracao TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_refresh_usuario
    FOREIGN KEY (usuario_id)
    REFERENCES usuarios(id)
    ON DELETE CASCADE
);