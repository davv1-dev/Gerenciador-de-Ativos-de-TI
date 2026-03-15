ALTER TABLE equipamentos ALTER COLUMN numero_patrimonio DROP NOT NULL;

ALTER TABLE equipamentos ADD COLUMN quantidade INTEGER NOT NULL DEFAULT 1;