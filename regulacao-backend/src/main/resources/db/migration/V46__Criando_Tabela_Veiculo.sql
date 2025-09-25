CREATE TABLE transporte(
    id BIGSERIAL PRIMARY KEY,
    nome varchar(50),
    vagas BIGINT,
    tipo varchar(50),
    modelo varchar(50)
)