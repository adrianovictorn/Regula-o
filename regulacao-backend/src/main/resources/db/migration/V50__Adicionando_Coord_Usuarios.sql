ALTER TABLE usuarios
DROP CONSTRAINT IF EXISTS usuarios_cargo_check;

-- Adiciona a nova constraint com a lista ATUALIZADA de todos os cargos.
-- ADICIONE SEUS NOVOS CARGOS AQUI DENTRO DOS PARÊNTESES.
ALTER TABLE usuarios
ADD CONSTRAINT usuarios_cargo_check
CHECK (cargo IN (
    'ADMIN',
    'USER',
    'PACIENTE',
    'ENFERMEIRO',
    'MEDICO',
    'RECEPCAO',
    'COORD_TRANSPORTE'
));