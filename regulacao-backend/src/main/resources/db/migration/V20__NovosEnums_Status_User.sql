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
    'CANCELADO',
    'REALIZADO',
    'RETORNO',
    'RETORNO_POLICLINICA'
));


-- 2. Atualiza a constraint de CARGO (ROLE) na tabela usuarios
-- Remove a constraint antiga, se ela existir.
ALTER TABLE usuarios
DROP CONSTRAINT IF EXISTS usuarios_cargo_check;

-- Adiciona a nova constraint com a lista ATUALIZADA de todos os cargos.
-- ADICIONE SEUS NOVOS CARGOS AQUI DENTRO DOS PARÊNTESES.
ALTER TABLE usuarios
ADD CONSTRAINT usuarios_cargo_check
CHECK (cargo IN (
    'ADMIN',
    'USER',
    'PACIENTE',
    'ENFERMEIRO',
    'MEDICO',
    'RECEPCAO'
));