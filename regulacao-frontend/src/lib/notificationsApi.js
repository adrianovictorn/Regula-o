import { getApi, postApi } from '$lib/api.js';

export async function listarNaoLidas() {
  const res = await getApi('notifications/unread');
  if (!res.ok) throw new Error('Falha ao listar notificações');
  return await res.json();
}

export async function marcarComoLida(id) {
  const res = await postApi(`notifications/${id}/read`);
  if (!res.ok) throw new Error('Falha ao marcar como lida');
}

export async function marcarTodasComoLidas() {
  const res = await postApi('notifications/read-all');
  if (!res.ok) throw new Error('Falha ao marcar todas como lidas');
}

