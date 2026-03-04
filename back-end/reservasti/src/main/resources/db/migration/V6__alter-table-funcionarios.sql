
ALTER TABLE funcionarios
ADD COLUMN departamento_id BIGINT;

ALTER TABLE funcionarios
ADD CONSTRAINT fk_funcionario_departamento
FOREIGN KEY (departamento_id) REFERENCES departamentos(id);