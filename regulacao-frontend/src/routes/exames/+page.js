// Importa a URL base da sua API
import { API_BASE_URL } from '$lib/config.js';

// Em src/routes/exames/+page.js (ou +page.server.js)
export async function load({ fetch }) {
  // Concatena a URL base com o endpoint da API
  const res = await fetch(`${API_BASE_URL}/api/solicitacoes`); 
  if (!res.ok) {
    console.error('Não conseguiu se comunicar com o servidor para listar solicitações!');
    return {
      listaDeSolicitacoes: [], // Retorna lista vazia em caso de erro
      error: 'Falha ao carregar lista de solicitações.'
    };
  }
  const todasAsSolicitacoes = await res.json();
  return {
    listaDeSolicitacoes: todasAsSolicitacoes // Lista para o dropdown
  };
}
