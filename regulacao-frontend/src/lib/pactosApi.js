import { getApi, postApi } from '$lib/api.js';

export async function listarPactos() {
  const res = await getApi('pactos');
  if (!res.ok) throw new Error('Falha ao listar pactos');
  return await res.json();
}

export async function listarFeed(pactoId) {
  const res = await getApi(`pactos/${pactoId}/feed`);
  if (!res.ok) throw new Error('Falha ao carregar feed do pacto');
  return await res.json();
}

export async function claimEvento(pactoId, eventoUuid) {
  const res = await postApi(`pactos/${pactoId}/solicitacoes/${eventoUuid}/claim`);
  if (res.status === 409) {
    return { claimed: false, status: 'CONFLITO', message: 'Já consumido por outro município' };
  }
  if (!res.ok) throw new Error('Falha ao efetuar claim');
  return await res.json();
}

export async function publicarSolicitacao(pactoId, payload) {
  const res = await postApi(`pactos/${pactoId}/solicitacoes/publicar`, payload);
  if (!res.ok) throw new Error('Falha ao publicar solicitação no pacto');
  return await res.json();
}

// Convites de Pacto
export async function listarConvites(pactoId) {
  const res = await getApi(`pactos/${pactoId}/convites`);
  if (!res.ok) throw new Error('Falha ao listar convites');
  return await res.json();
}

export async function criarConvites(pactoId, convidados, mensagem) {
  const res = await postApi(`pactos/${pactoId}/convites`, { convidados, mensagem });
  if (!res.ok) throw new Error('Falha ao enviar convites');
  return await res.json();
}

export async function responderConvite(token, aceitar) {
  const res = await postApi(`pactos/convites/${token}/responder`, { aceitar });
  if (!res.ok) throw new Error('Falha ao responder convite');
  return await res.json();
}
