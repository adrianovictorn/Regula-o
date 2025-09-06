CREATE TABLE IF NOT EXISTS notificacao (
    id BIGSERIAL PRIMARY KEY,
    municipio_destino_id UUID NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    resumo TEXT NOT NULL,
    link_path VARCHAR(255),
    payload JSONB,
    lida BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_notificacao_destino_lida ON notificacao(municipio_destino_id, lida);

