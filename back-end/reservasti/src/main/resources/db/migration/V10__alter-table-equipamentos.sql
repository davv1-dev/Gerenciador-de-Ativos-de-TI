
ALTER TABLE equipamentos ADD COLUMN departamento_id BIGINT;

ALTER TABLE equipamentos ADD CONSTRAINT fk_equipamento_departamento
    FOREIGN KEY (departamento_id) REFERENCES departamentos(id);