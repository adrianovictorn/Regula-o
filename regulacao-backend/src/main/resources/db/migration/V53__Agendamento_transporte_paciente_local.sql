ALTER TABLE agendamento_transporte_paciente
    ADD COLUMN local_agendamento_id BIGINT;

ALTER TABLE agendamento_transporte_paciente
    ADD CONSTRAINT fk_atp_local_agendamento
        FOREIGN KEY (local_agendamento_id) REFERENCES local_agendamento(id);

CREATE INDEX ix_atp_local_agendamento
    ON agendamento_transporte_paciente(local_agendamento_id);
