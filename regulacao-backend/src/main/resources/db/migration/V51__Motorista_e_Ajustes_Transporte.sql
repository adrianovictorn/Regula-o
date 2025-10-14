CREATE TABLE motorista(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    telefone VARCHAR(30),
    observacoes VARCHAR(500)
);

ALTER TABLE agendamento_transporte
    ADD COLUMN motorista_id BIGINT;

ALTER TABLE agendamento_transporte
    ADD COLUMN hora_saida TIME;

ALTER TABLE agendamento_transporte
    ADD CONSTRAINT fk_agendamento_motorista
        FOREIGN KEY (motorista_id) REFERENCES motorista(id);

CREATE INDEX ix_agtr_motorista ON agendamento_transporte(motorista_id);

UPDATE transporte SET tipo = 'ONIBUS' WHERE tipo = 'VAN';
