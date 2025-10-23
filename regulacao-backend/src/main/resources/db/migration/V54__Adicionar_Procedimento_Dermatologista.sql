UPDATE especialidade
SET codigo = 'PROCEDIMENTO_DERMATOLOGISTA'
WHERE codigo = 'PROCEDIMENTO_DERMATOLOGIA';

-- Garante o cadastro da especialidade de Procedimento Dermatologia
INSERT INTO especialidade (codigo, nome, categoria, ativo)
SELECT 'PROCEDIMENTO_DERMATOLOGISTA', 'Procedimento Dermatologia', 'ESPECIALIDADE_MEDICA', TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM especialidade WHERE codigo = 'PROCEDIMENTO_DERMATOLOGISTA'
);

-- Atualiza vínculos das solicitações para apontarem para o novo registro
UPDATE solicitacao_especialidade se
SET especialidade_id = e.id,
    especialidade_solicitada = 'PROCEDIMENTO_DERMATOLOGISTA'
FROM especialidade e
WHERE e.codigo = 'PROCEDIMENTO_DERMATOLOGISTA'
  AND se.especialidade_solicitada IN ('PROCEDIMENTO_DERMATOLOGISTA', 'PROCEDIMENTO_DERMATOLOGIA')
  AND (se.especialidade_id IS NULL OR se.especialidade_id <> e.id);
