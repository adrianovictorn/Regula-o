-- Adiciona a coluna 'turno' na tabela de agendamentos


-- Define 'NAO_INFORMADO' para todos os agendamentos já existentes
UPDATE agendamento_solicitacao SET turno = 'NAO_INFORMADO' WHERE turno IS NULL;