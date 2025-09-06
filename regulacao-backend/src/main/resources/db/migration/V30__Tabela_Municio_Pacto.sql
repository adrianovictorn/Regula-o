-- Cria a tabela para armazenar os municípios
CREATE TABLE municipio (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    rabbit_queue_name VARCHAR(255) NOT NULL UNIQUE
);

-- Cria a tabela para gerenciar os pactos entre municípios
CREATE TABLE pacto_regional (
    id BIGSERIAL PRIMARY KEY,
    solicitante_id BIGINT NOT NULL,
    receptor_id BIGINT NOT NULL,
    status VARCHAR(255) NOT NULL,
    CONSTRAINT fk_solicitante FOREIGN KEY (solicitante_id) REFERENCES municipio(id),
    CONSTRAINT fk_receptor FOREIGN KEY (receptor_id) REFERENCES municipio(id)
);