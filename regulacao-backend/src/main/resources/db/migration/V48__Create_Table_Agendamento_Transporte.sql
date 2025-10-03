CREATE TABLE agendamento_transporte(
    id BIGSERIAL PRIMARY KEY,
    transporte_id BIGINT NOT NULL,
    cidade_id BIGINT NOT NULL,
    data_agendamento_transporte DATE NOT NULL,
    status_agendamento varchar(20) NOT NULL,

    CONSTRAINT fk_agr_transporte
     FOREIGN KEY (transporte_id) REFERENCES transporte(id),

    CONSTRAINT fk_agr_cidade
     FOREIGN KEY (cidade_id) REFERENCES cidade(id)
    
);

CREATE INDEX ix_agtr_data ON agendamento_transporte(data_agendamento_transporte);
CREATE INDEX ix_agtr_transporte ON agendamento_transporte(transporte_id);
CREATE INDEX ix_agtr_cidade ON agendamento_transporte(cidade_id);

CREATE TABLE agendamento_transporte_solicitacao(
    agendamento_transporte_id BIGINT NOT NULL,
    solicitacao_id BIGINT NOT NULL,
    primary key(agendamento_transporte_id, solicitacao_id),

    CONSTRAINT fk_agts_transporte 
        FOREIGN KEY (agendamento_transporte_id) REFERENCES agendamento_transporte(id)
        ON DELETE CASCADE,
    
    CONSTRAINT fk_agts_solicitacao
        FOREIGN KEY (solicitacao_id) REFERENCES solicitacao(id)
    
);

CREATE INDEX ix_agendamento_transporte_solicitacao ON agendamento_transporte_solicitacao(solicitacao_id);


CREATE TABLE agendamento_transporte_local_agendamento(
    agendamento_transporte_id BIGINT NOT NULL,
    local_agendamento_id BIGINT NOT NULL,
    PRIMARY KEY(agendamento_transporte_id, local_agendamento_id),

    CONSTRAINT fk_agtl_transporte
        FOREIGN KEY (agendamento_transporte_id) REFERENCES agendamento_transporte(id),

    CONSTRAINT fk_agtl_local 
        FOREIGN KEY (local_agendamento_id) REFERENCES local_agendamento(id)
);

CREATE INDEX ix_transporte_local_agendamento ON agendamento_transporte_local_agendamento(local_agendamento_id);