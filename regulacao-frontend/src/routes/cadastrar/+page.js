// Importa a URL base da sua API
import { API_BASE_URL } from '$lib/config.js';

export async function load({ fetch }) {
  // Concatena a URL base com o endpoint da API
  const res = await fetch(`${API_BASE_URL}/api/solicitacoes`);
  if (!res.ok) throw new Error('Não conseguiu se comunicar com o servidor !');
  const solicitacoes = await res.json(); // já é a lista
  return { solicitacoes };
}
