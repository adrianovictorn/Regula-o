// paciente/[id]/+page.js
/** @type {import('@sveltejs/kit').Load} */
export async function load({ params, fetch }) {
  const resSolicitacao = await fetch(`http://localhost:8080/api/solicitacoes/${params.id}`);
  if (!resSolicitacao.ok) {
    throw new Error('Falha ao buscar dados do paciente');
  }
  const solicitacao = await resSolicitacao.json();

  const resAgendamentos = await fetch(`http://localhost:8080/api/agendamentos/solicitacao/${params.id}`);
  let agendamentos = [];
  if (resAgendamentos.ok) {
    agendamentos = await resAgendamentos.json();
  } else {
    console.error('Falha ao buscar agendamentos:', await resAgendamentos.text());
  }

  // Removida a busca por 'todasEspecialidades' do backend
  // let todasEspecialidades = []; // Remova ou comente esta linha e o fetch relacionado

  return {
    solicitacao,
    historico: solicitacao.especialidades,
    agendamentos
    // 'todasEspecialidades' não é mais retornada daqui, será hardcoded no componente
  };
}