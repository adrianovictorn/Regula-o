-- 1. Remove a constraint antiga para evitar erros.
ALTER TABLE agendamento_solicitacao
DROP CONSTRAINT IF EXISTS agendamento_solicitacao_local_agendado_check;

-- 2. Adiciona a nova constraint com a lista COMPLETA E ATUALIZADA de todos os locais.
ALTER TABLE agendamento_solicitacao
ADD CONSTRAINT agendamento_solicitacao_local_agendado_check
CHECK (local_agendado IN (
    'POLICLINICA_RECONVALE',
    'POLICLINICA_MUNICIPAL_DE_SANTO_ANTONIO_DE_JESUS',
    'HOSPITAL_DE_CONCEICAO_DO_ALMEIDA',
    'HOSPITAL_IRMA_DULCE',
    'HOSPITAL_DA_MULHER',
    'LABCHECAP',
    'HOSPITAL_ROBERTO_SANTOS',
    'HOSPITAL_LUIS_ARGOLO',
    'HOSPITAL_DOS_OLHOS_BAHIA',
    'CLINICA_A_MAIS_SAUDE',
    'HOSPITAL_ARISTIDES',
    'CLINICA_MURITIBA',
    'APAE',
    'CDI',
    'CEDAF',
    'CLINICA_ELIZ',
    'MULTICENTRO_DA_CARLOS_GOMES',
    'MULTIIMAGEM',
    'HOSPITAL_MUNICIPAL',
    'HOSPITAL_ORTOPEDICO',
    'HOSPITAL_DO_SUBURBIO',
    'PRIMAGEM',
    'PONTO_ALTO_IMAGEM',
    'HOSPITAL_REGIONAL',
    'HOSPITAL_SANTA_IZABEL',
    'HOSPITAL_MARTAGAO_GESTEIRA',
    'ADAB',
    'HOSPITAL_DA_CRIANCA'
));