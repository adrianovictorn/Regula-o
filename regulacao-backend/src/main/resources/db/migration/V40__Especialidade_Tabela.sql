-- Tabela de Especialidade (migração do Enum para tabela)
CREATE TABLE IF NOT EXISTS especialidade (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(150) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL UNIQUE,
    categoria VARCHAR(50) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

