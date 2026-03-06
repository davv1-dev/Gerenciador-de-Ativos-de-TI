
ALTER TABLE funcionarios DROP CONSTRAINT fk_funcionario_usuario;

ALTER TABLE funcionarios DROP COLUMN usuario_id;

ALTER TABLE funcionarios ALTER COLUMN cpf TYPE VARCHAR(14);

ALTER TABLE funcionarios ALTER COLUMN cep TYPE VARCHAR(9);