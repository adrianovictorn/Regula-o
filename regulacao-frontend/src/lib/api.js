import { token } from '$lib/stores/auth.js';
import { get } from 'svelte/store';

// Define a URL base para todas as chamadas à sua API backend.
const BASE_URL = '/api';
/**
 * Uma função central para enviar todas as requisições para a API.
 * @param {object} param0 - Objeto com método, caminho e dados.
 * @returns {Promise<Response>} A resposta do fetch.
 */
async function send({ method, path, data }) {
  // Pega o valor atual do token da nossa store de autenticação.
  const currentToken = get(token); 

  const opts = { 
    method,
    headers: {}
  };

  // Se a requisição tiver um corpo (body), define o cabeçalho correto.
  if (data) {
    opts.headers['Content-Type'] = 'application/json';
    opts.body = JSON.stringify(data);
  }

  // Se existir um token, adiciona-o ao cabeçalho de Autorização.
  if (currentToken) {
    opts.headers['Authorization'] = `Bearer ${currentToken}`;
  }

  const response = await fetch(`${BASE_URL}/${path}`, opts);
  
  // Lógica de segurança: se o token for inválido/expirado (erro 403),
  // desloga o usuário limpando o token da store.
  if (response.status === 403) {
      token.set(null); 
  }

  return response;
}

// Funções auxiliares para conveniência nas chamadas da API.
export function getApi(path) {
  return send({ method: 'GET', path });
}

export function postApi(path, data) {
  return send({ method: 'POST', path, data });
}


export function putApi(path, data) {
  return send({ method: 'PUT', path, data });
}

export function deleteApi(path, data) {
  return send({ method: 'DELETE', path, data });
}

