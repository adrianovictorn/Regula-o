-- V29__Corrigindo_Data.sql (idempotente)
ALTER TABLE solicitacao
  ADD COLUMN IF NOT EXISTS datanascimento DATE;

-- Se houver mais alters/criações, siga o mesmo padrão:
-- CREATE INDEX IF NOT EXISTS idx_solicitacao_datanascimento ON solicitacao(datanascimento);

-- Para comandos que não têm IF NOT EXISTS, use um bloco PL/pgSQL:
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = 'public'
      AND table_name = 'solicitacao'
      AND column_name = 'datanascimento'
  ) THEN
    -- aqui dentro você pode fazer UPDATEs/ajustes que dependem da coluna, se precisar
    -- ex.: UPDATE solicitacao SET datanascimento = ... WHERE datanascimento IS NULL;
  END IF;
END$$;
