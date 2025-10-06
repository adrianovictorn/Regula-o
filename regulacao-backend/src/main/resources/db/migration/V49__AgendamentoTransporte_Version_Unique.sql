ALTER TABLE agendamento_transporte
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

ALTER TABLE agendamento_transporte
    ALTER COLUMN version DROP DEFAULT;

ALTER TABLE agendamento_transporte
    ADD CONSTRAINT uk_agendamento_transporte_transporte_data
        UNIQUE (transporte_id, data_agendamento_transporte);
