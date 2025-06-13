/** @type {import('./$types').PageLoad} */
export async function load({ fetch }) {
  const res = await fetch(`/api/agendamentos/pendentes`);
  if (!res.ok) {
    throw new Error('Falha ao buscar solicitações pendentes');
  }
  const agendamentos = await res.json();
  return { agendamentos };
}
