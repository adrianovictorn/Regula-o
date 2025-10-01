-- Permite que local_agendado seja nulo, já que passamos a utilizar a relação com local_agendamento.
ALTER TABLE agendamento_solicitacao
    ALTER COLUMN local_agendado DROP NOT NULL;
