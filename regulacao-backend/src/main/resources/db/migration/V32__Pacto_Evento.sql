-- Garantir a existência das tabelas de Pacto e seus membros
CREATE TABLE IF NOT EXISTS pactos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255),
    descricao VARCHAR(255),
    municipio_criador_id UUID NULL REFERENCES municipio(id),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS pacto_membros (
    pacto_id BIGINT NOT NULL REFERENCES pactos(id) ON DELETE CASCADE,
    municipio_id UUID NOT NULL REFERENCES municipio(id) ON DELETE CASCADE,
    PRIMARY KEY (pacto_id, municipio_id)
);

-- Tabela de eventos publicados nos pactos
CREATE TABLE IF NOT EXISTS pacto_evento (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    pacto_id BIGINT NOT NULL REFERENCES pactos(id),
    solicitacao_local_id BIGINT NULL,
    municipio_origem VARCHAR(120) NOT NULL,
    label VARCHAR(120) NOT NULL,
    status VARCHAR(20) NOT NULL,
    consumido_por_municipio VARCHAR(120),
    published_at TIMESTAMP NOT NULL DEFAULT NOW(),
    consumido_at TIMESTAMP NULL
);

CREATE INDEX IF NOT EXISTS idx_pacto_evento_pacto_status ON pacto_evento(pacto_id, status);
CREATE INDEX IF NOT EXISTS idx_pacto_evento_municipio_origem ON pacto_evento(municipio_origem);
