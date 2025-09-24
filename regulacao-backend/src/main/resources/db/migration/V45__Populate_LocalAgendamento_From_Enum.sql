ALTER TABLE local_agendamento
    ALTER COLUMN cidade_id DROP NOT NULL;

ALTER TABLE local_agendamento
    ADD COLUMN IF NOT EXISTS enum_value VARCHAR(100);

CREATE UNIQUE INDEX IF NOT EXISTS uk_local_agendamento_enum_value
    ON local_agendamento(enum_value)
    WHERE enum_value IS NOT NULL;

-- Função auxiliar inline para normalizar nome_local e associar ao enum
-- (utiliza translate para remover acentuação básica)

-- Atualiza registros existentes tentando detectar equivalência pelo nome
UPDATE local_agendamento
SET enum_value = 'POLICLINICA_RECONVALE'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'POLICLINICA_RECONVALE';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Policlínica Reconvale', '', '', NULL, 'POLICLINICA_RECONVALE'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'POLICLINICA_RECONVALE');

UPDATE local_agendamento
SET enum_value = 'POLICLINICA_MUNICIPAL_DE_SANTO_ANTONIO_DE_JESUS'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'POLICLINICA_MUNICIPAL_DE_SANTO_ANTONIO_DE_JESUS';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Policlínica Municipal de Santo Antônio de Jesus', '', '', NULL, 'POLICLINICA_MUNICIPAL_DE_SANTO_ANTONIO_DE_JESUS'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'POLICLINICA_MUNICIPAL_DE_SANTO_ANTONIO_DE_JESUS');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_DE_CONCEICAO_DO_ALMEIDA'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_DE_CONCEICAO_DO_ALMEIDA';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital de Conceição do Almeida', '', '', NULL, 'HOSPITAL_DE_CONCEICAO_DO_ALMEIDA'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_DE_CONCEICAO_DO_ALMEIDA');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_IRMA_DULCE'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_IRMA_DULCE';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital Irmã Dulce', '', '', NULL, 'HOSPITAL_IRMA_DULCE'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_IRMA_DULCE');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_DA_MULHER'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_DA_MULHER';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital da Mulher', '', '', NULL, 'HOSPITAL_DA_MULHER'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_DA_MULHER');

UPDATE local_agendamento
SET enum_value = 'LABCHECAP'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'LABCHECAP';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Labchecap', '', '', NULL, 'LABCHECAP'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'LABCHECAP');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_ROBERTO_SANTOS'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_ROBERTO_SANTOS';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital Roberto Santos', '', '', NULL, 'HOSPITAL_ROBERTO_SANTOS'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_ROBERTO_SANTOS');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_LUIS_ARGOLO'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_LUIS_ARGOLO';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital Luís Argolo', '', '', NULL, 'HOSPITAL_LUIS_ARGOLO'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_LUIS_ARGOLO');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_DOS_OLHOS_BAHIA'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_DOS_OLHOS_BAHIA';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital dos Olhos Bahia', '', '', NULL, 'HOSPITAL_DOS_OLHOS_BAHIA'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_DOS_OLHOS_BAHIA');

UPDATE local_agendamento
SET enum_value = 'CLINICA_A_MAIS_SAUDE'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'CLINICA_A_MAIS_SAUDE';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Clínica A Mais Saúde', '', '', NULL, 'CLINICA_A_MAIS_SAUDE'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'CLINICA_A_MAIS_SAUDE');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_ARISTIDES'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_ARISTIDES';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital Aristides', '', '', NULL, 'HOSPITAL_ARISTIDES'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_ARISTIDES');

UPDATE local_agendamento
SET enum_value = 'CLINICA_MURITIBA'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'CLINICA_MURITIBA';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Clínica Muritiba', '', '', NULL, 'CLINICA_MURITIBA'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'CLINICA_MURITIBA');

UPDATE local_agendamento
SET enum_value = 'APAE'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'APAE';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'APAE', '', '', NULL, 'APAE'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'APAE');

UPDATE local_agendamento
SET enum_value = 'CDI'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'CDI';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'CDI', '', '', NULL, 'CDI'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'CDI');

UPDATE local_agendamento
SET enum_value = 'CEDAF'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'CEDAF';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'CEDAF', '', '', NULL, 'CEDAF'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'CEDAF');

UPDATE local_agendamento
SET enum_value = 'CLINICA_ELIZ'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'CLINICA_ELIZ';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Clínica Eliz', '', '', NULL, 'CLINICA_ELIZ'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'CLINICA_ELIZ');

UPDATE local_agendamento
SET enum_value = 'MULTICENTRO_DA_CARLOS_GOMES'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'MULTICENTRO_DA_CARLOS_GOMES';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Multicentro da Carlos Gomes', '', '', NULL, 'MULTICENTRO_DA_CARLOS_GOMES'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'MULTICENTRO_DA_CARLOS_GOMES');

UPDATE local_agendamento
SET enum_value = 'MULTIIMAGEM'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'MULTIIMAGEM';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Multi Imagem', '', '', NULL, 'MULTIIMAGEM'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'MULTIIMAGEM');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_MUNICIPAL'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_MUNICIPAL';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital Municipal', '', '', NULL, 'HOSPITAL_MUNICIPAL'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_MUNICIPAL');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_ORTOPEDICO'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_ORTOPEDICO';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital Ortopédico', '', '', NULL, 'HOSPITAL_ORTOPEDICO'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_ORTOPEDICO');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_DO_SUBURBIO'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_DO_SUBURBIO';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital do Subúrbio', '', '', NULL, 'HOSPITAL_DO_SUBURBIO'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_DO_SUBURBIO');

UPDATE local_agendamento
SET enum_value = 'PRIMAGEM'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'PRIMAGEM';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Primagem', '', '', NULL, 'PRIMAGEM'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'PRIMAGEM');

UPDATE local_agendamento
SET enum_value = 'PONTO_ALTO_IMAGEM'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'PONTO_ALTO_IMAGEM';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Ponto Alto Imagem', '', '', NULL, 'PONTO_ALTO_IMAGEM'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'PONTO_ALTO_IMAGEM');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_REGIONAL'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_REGIONAL';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital Regional', '', '', NULL, 'HOSPITAL_REGIONAL'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_REGIONAL');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_SANTA_IZABEL'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_SANTA_IZABEL';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital Santa Izabel', '', '', NULL, 'HOSPITAL_SANTA_IZABEL'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_SANTA_IZABEL');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_MARTAGAO_GESTEIRA'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_MARTAGAO_GESTEIRA';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital Martagão Gesteira', '', '', NULL, 'HOSPITAL_MARTAGAO_GESTEIRA'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_MARTAGAO_GESTEIRA');

UPDATE local_agendamento
SET enum_value = 'ADAB'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'ADAB';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'ADAB', '', '', NULL, 'ADAB'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'ADAB');

UPDATE local_agendamento
SET enum_value = 'HOSPITAL_DA_CRIANCA'
WHERE enum_value IS NULL
  AND UPPER(REPLACE(translate(nome_local,'ÁÀÂÃÉÈÊÍÌÎÓÒÔÕÚÙÛÇ','AAAAEEEIIIOOOOUUUC'), ' ', '_')) = 'HOSPITAL_DA_CRIANCA';

INSERT INTO local_agendamento (nome_local, endereco, numero, cidade_id, enum_value)
SELECT 'Hospital da Criança', '', '', NULL, 'HOSPITAL_DA_CRIANCA'
WHERE NOT EXISTS (SELECT 1 FROM local_agendamento WHERE enum_value = 'HOSPITAL_DA_CRIANCA');

-- Preenche local_agendamento_id para registros antigos que possuíam apenas o enum
UPDATE agendamento_solicitacao ag
SET local_agendamento_id = la.id
FROM local_agendamento la
WHERE ag.local_agendamento_id IS NULL
  AND ag.local_agendado = la.enum_value;
