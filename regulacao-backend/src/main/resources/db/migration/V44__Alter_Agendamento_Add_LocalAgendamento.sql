ALTER TABLE agendamento_solicitacao
    ADD COLUMN IF NOT EXISTS local_agendamento_id BIGINT;

ALTER TABLE agendamento_solicitacao
    ADD CONSTRAINT fk_agendamento_local_agendamento
        FOREIGN KEY (local_agendamento_id)
        REFERENCES local_agendamento(id);
