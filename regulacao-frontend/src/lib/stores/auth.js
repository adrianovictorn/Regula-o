import { writable, derived } from 'svelte/store';
import { browser } from '$app/environment';
import { jwtDecode } from 'jwt-decode';

// Store para o token JWT
export const token = writable(browser ? localStorage.getItem('jwt_token') : null);

token.subscribe(value => {
  if (browser) {
    if (value) {
      localStorage.setItem('jwt_token', value);
    } else {
      localStorage.removeItem('jwt_token');
    }
  }
});

/**
 * Uma store derivada que decodifica o token e expõe os dados do usuário.
 * Ela será atualizada automaticamente sempre que o token mudar.
 */
export const user = derived(token, ($token) => {
  if (!$token) return null;

  try {
    // Decodifica o token para acessar os dados (payload)
    const decoded = jwtDecode($token);
    return {
      cpf: decoded.sub, // 'sub' (subject) é o nosso CPF
      nome: decoded.nome,
      role: decoded.role // A 'role' que adicionamos no backend
    };
  } catch (e) {
    console.error("Token inválido:", e);
    return null;
  }
});