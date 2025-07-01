-- Adiciona a coluna 'turno' na tabela de agendamentos
ALTER TABLE agendamento_solicitacao ADD COLUMN turno VARCHAR(50);

-- Define 'NAO_INFORMADO' para todos os agendamentos já existentes
UPDATE agendamento_solicitacao SET turno = 'NAO_INFORMADO' WHERE turno IS NULL;

-- Define 'NAO_INFORMADO' para todos os agendamentos já existentes
UPDATE agendamento_solicitacao SET turno = 'NAO_INFORMADO' WHERE turno IS NULL;