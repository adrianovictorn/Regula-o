CREATE TABLE IF NOT EXISTS pacto_convite (
    id BIGSERIAL PRIMARY KEY,
    token UUID NOT NULL UNIQUE,
    pacto_id_remoto BIGINT NOT NULL,
    pacto_nome VARCHAR(255) NOT NULL,
    convidado_municipio_id UUID NOT NULL,
    remetente_municipio_id UUID NOT NULL,
    remetente_nome VARCHAR(255) NOT NULL,
    mensagem TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    responded_at TIMESTAMP NULL
);

CREATE INDEX IF NOT EXISTS idx_pacto_convite_pacto ON pacto_convite(pacto_id_remoto);
CREATE INDEX IF NOT EXISTS idx_pacto_convite_convidado ON pacto_convite(convidado_municipio_id);

