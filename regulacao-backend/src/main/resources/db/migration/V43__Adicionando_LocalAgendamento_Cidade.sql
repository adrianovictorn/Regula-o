CREATE TABLE cidade(
    id BIGSERIAL PRIMARY KEY,
    codigo varchar(30),
    nome varchar(50) NOT NULL,
    cep varchar(40)

);

CREATE TABLE local_agendamento(
    id BIGSERIAL primary key,
    nome_local varchar(70) NOT NULL,
    endereco varchar(120),
    numero varchar(20),
    cidade_id BIGINT NOT NULL,
    FOREIGN KEY (cidade_id) REFERENCES cidade(id)
);

