CREATE TABLE IF NOT EXISTS pacto_join_request (
    id BIGSERIAL PRIMARY KEY,
    token UUID NOT NULL UNIQUE,
    pacto_id_remoto BIGINT NOT NULL,
    solicitante_municipio_id UUID NOT NULL,
    solicitante_nome VARCHAR(255) NOT NULL,
    mensagem TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    responded_at TIMESTAMP NULL
);

CREATE INDEX IF NOT EXISTS idx_pacto_join_pacto ON pacto_join_request(pacto_id_remoto);
CREATE INDEX IF NOT EXISTS idx_pacto_join_solicitante ON pacto_join_request(solicitante_municipio_id);

