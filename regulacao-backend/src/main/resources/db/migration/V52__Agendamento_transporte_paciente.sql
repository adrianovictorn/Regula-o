CREATE TABLE agendamento_transporte_paciente (
    id BIGSERIAL PRIMARY KEY,
    agendamento_transporte_id BIGINT NOT NULL,
    solicitacao_id BIGINT NOT NULL,
    turno VARCHAR(20) NOT NULL DEFAULT 'NAO_INFORMADO',
    retorna_mesmo_dia BOOLEAN,

    CONSTRAINT fk_atp_agendamento
        FOREIGN KEY (agendamento_transporte_id) REFERENCES agendamento_transporte(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_atp_solicitacao
        FOREIGN KEY (solicitacao_id) REFERENCES solicitacao(id),

    CONSTRAINT uk_atp_agendamento_solicitacao
        UNIQUE (agendamento_transporte_id, solicitacao_id)
);

CREATE INDEX ix_atp_agendamento ON agendamento_transporte_paciente(agendamento_transporte_id);
CREATE INDEX ix_atp_solicitacao ON agendamento_transporte_paciente(solicitacao_id);

INSERT INTO agendamento_transporte_paciente (agendamento_transporte_id, solicitacao_id, turno, retorna_mesmo_dia)
SELECT agendamento_transporte_id, solicitacao_id, 'NAO_INFORMADO', NULL
FROM agendamento_transporte_solicitacao;

DROP TABLE agendamento_transporte_solicitacao;
