// Importa a URL base da sua API para manter o código consistente
import { API_BASE_URL } from '$lib/config.js';

export async function load({ fetch }) {
  // Substitui o endereço hardcoded pela variável
  const res = await fetch(`${API_BASE_URL}/api/solicitacoes`);
  if (!res.ok) {
    throw new Error('Não conseguiu se comunicar com o servidor!');
  }
  const solicitacoes = await res.json();

  return { solicitacoes };
}
