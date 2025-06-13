// Importa a URL base da sua API
import { API_BASE_URL } from '$lib/config.js';

/** @type {import('./$types').PageLoad} */
export async function load({ fetch }) {
  // Concatena a URL base com o endpoint da API
  const res = await fetch(`${API_BASE_URL}/api/agendamentos/pendentes`);
  if (!res.ok) {
    const errorText = await res.text();
    console.error('Falha ao buscar na função load:', res.status, errorText);
    throw new Error(`Falha ao buscar solicitações pendentes: ${res.status}`);
  }

  const agendamentosData = await res.json();

  return {
    agendamentos: Array.isArray(agendamentosData)
      ? agendamentosData
      : agendamentosData.agendamentos
  };
}
