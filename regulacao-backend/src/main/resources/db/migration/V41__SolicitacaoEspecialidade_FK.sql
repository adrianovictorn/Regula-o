-- Adiciona coluna de FK para Especialidade
ALTER TABLE solicitacao_especialidade
    ADD COLUMN IF NOT EXISTS especialidade_id BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_solic_espec_especialidade'
    ) THEN
        ALTER TABLE solicitacao_especialidade
            ADD CONSTRAINT fk_solic_espec_especialidade
            FOREIGN KEY (especialidade_id) REFERENCES especialidade(id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_solic_espec_especialidade_id ON solicitacao_especialidade (especialidade_id);
