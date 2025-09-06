ALTER TABLE solicitacao ADD COLUMN IF NOT EXISTS origem_municipio_id UUID NULL;
ALTER TABLE solicitacao ADD COLUMN IF NOT EXISTS origem_municipio_nome VARCHAR(255) NULL;

