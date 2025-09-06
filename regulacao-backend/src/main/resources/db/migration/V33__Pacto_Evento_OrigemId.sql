ALTER TABLE pacto_evento
ADD COLUMN IF NOT EXISTS municipio_origem_id UUID;

CREATE INDEX IF NOT EXISTS idx_pacto_evento_municipio_origem_id
  ON pacto_evento(municipio_origem_id);

