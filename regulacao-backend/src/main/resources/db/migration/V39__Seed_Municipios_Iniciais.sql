-- Semeia municípios base para ambientes locais/integração
-- Garante a presença dos nomes e filas usadas nos perfis application-*.properties

-- CONCEICAO_DO_ALMEIDA
INSERT INTO municipio (id, nome, rabbit_queue_name, cnes)
VALUES ('11111111-1111-1111-1111-111111111111', 'CONCEICAO_DO_ALMEIDA', 'fila_conceicao_do_almeida', '1234567')
ON CONFLICT (nome) DO UPDATE SET
  rabbit_queue_name = EXCLUDED.rabbit_queue_name,
  cnes = EXCLUDED.cnes;

-- SAPEACU
INSERT INTO municipio (id, nome, rabbit_queue_name, cnes)
VALUES ('22222222-2222-2222-2222-222222222222', 'SAPEACU', 'fila_sapeacu', '2234567')
ON CONFLICT (nome) DO UPDATE SET
  rabbit_queue_name = EXCLUDED.rabbit_queue_name,
  cnes = EXCLUDED.cnes;

-- SANTO_ANTONIO_DE_JESUS
INSERT INTO municipio (id, nome, rabbit_queue_name, cnes)
VALUES ('33333333-3333-3333-3333-333333333333', 'SANTO_ANTONIO_DE_JESUS', 'fila_santo_antonio_de_jesus', '3234567')
ON CONFLICT (nome) DO UPDATE SET
  rabbit_queue_name = EXCLUDED.rabbit_queue_name,
  cnes = EXCLUDED.cnes;
