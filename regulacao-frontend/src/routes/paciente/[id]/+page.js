// Importa a URL base da sua API
import { API_BASE_URL } from '$lib/config.js';

/** @type {import('./$types').PageLoad} */
export async function load({ params, fetch }) {
  // Atualiza o fetch para usar a URL base
  const resSolicitacao = await fetch(`${API_BASE_URL}/api/solicitacoes/${params.id}`);
  if (!resSolicitacao.ok) {
    throw new Error('Falha ao buscar dados do paciente');
  }
  const solicitacao = await resSolicitacao.json();

  // Atualiza o segundo fetch também
  const resAgendamentos = await fetch(`${API_BASE_URL}/api/agendamentos/solicitacao/${params.id}`);
  let agendamentos = [];
  if (resAgendamentos.ok) {
    agendamentos = await resAgendamentos.json();
  } else {
    console.error('Falha ao buscar agendamentos:', await resAgendamentos.text());
  }

  return {
    solicitacao,
    historico: solicitacao.especialidades,
    agendamentos
  };
}
