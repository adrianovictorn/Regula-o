-- V20__Atualiza_enums_roles_e_status.sql

-- 1. Atualiza a constraint de STATUS na tabela solicitacao_especialidade
-- Remove a constraint antiga para evitar erros.
ALTER TABLE solicitacao_especialidade
DROP CONSTRAINT IF EXISTS solicitacao_especialidade_status_check;

-- Adiciona a nova constraint com a lista ATUALIZADA de todos os status.
-- ADICIONE SEUS NOVOS STATUS AQUI DENTRO DOS PARÊNTESES.
ALTER TABLE solicitacao_especialidade
ADD CONSTRAINT solicitacao_especialidade_status_check
CHECK (status IN (
    'AGUARDANDO',
    'AGENDADO',
    'FALTOU',
    'CANCELADO',
    'REALIZADO',
    'RETORNO',
    'RETORNO_POLICLINICA'
));


