/** @type {import('./$types').PageLoad} */
export async function load({ fetch }) {
  const res = await fetch(`/api/agendamentos/pendentes`);
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
