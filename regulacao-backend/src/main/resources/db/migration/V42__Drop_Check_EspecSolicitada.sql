-- Remover a constraint baseada no enum legado da coluna 'especialidade_solicitada'
-- Agora usamos a FK 'especialidade_id' para validar o catálogo de especialidades.
ALTER TABLE solicitacao_especialidade
DROP CONSTRAINT IF EXISTS solicitacao_especialidade_especialidade_solicitada_check;

-- Opcional: permitir qualquer código legado (ou nulo) sem travar a inserção
-- ALTER TABLE solicitacao_especialidade
--   ADD CONSTRAINT solicitacao_especialidade_especialidade_solicitada_check
--   CHECK (especialidade_solicitada IS NULL OR especialidade_solicitada ~ '^[A-Z0-9_]+$');

