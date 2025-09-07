import { getApi } from '$lib/api.js';

// Lista especialidades do catálogo (entidade), autenticado
export async function listarEspecialidadesCatalogo() {
  const res = await getApi('catalog/especialidades');
  if (!res.ok) throw new Error('Falha ao listar especialidades do catálogo');
  return await res.json();
}

export async function listarExamesProcedimentos() {
  const all = await listarEspecialidadesCatalogo();
  return all.filter(e => e.categoria === 'EXAME_OU_PROCEDIMENTO');
}

export async function listarEspecialidadesMedicas() {
  const all = await listarEspecialidadesCatalogo();
  return all.filter(e => e.categoria === 'ESPECIALIDADE_MEDICA');
}

// Cria uma especialidade no catálogo (admin)
export async function criarEspecialidadeCatalogo({ codigo, nome, categoria, ativo = true }) {
  const res = await (await import('$lib/api.js')).postApi('catalog/especialidades', {
    codigo,
    nome,
    categoria,
    ativo
  });
  return res;
}
