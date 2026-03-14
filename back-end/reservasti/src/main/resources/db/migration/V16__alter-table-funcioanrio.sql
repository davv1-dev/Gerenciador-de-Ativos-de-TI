
ALTER TABLE funcionarios ADD COLUMN status_acesso VARCHAR(50);

UPDATE funcionarios SET status_acesso = 'APROVADO' WHERE status_acesso IS NULL;

ALTER TABLE funcionarios ALTER COLUMN status_acesso SET NOT NULL;
