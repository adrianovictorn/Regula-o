<script>
  import { goto } from '$app/navigation';
  export let data;
  // Remova 'todasEspecialidades' daqui
  const { solicitacao, historico, agendamentos } = data;

  // --- Campos editáveis do paciente (Solicitação) ---
  let nomePaciente = solicitacao.nomePaciente;
  let cpfPaciente = solicitacao.cpfPaciente;
  let cns = solicitacao.cns;
  let datanascimento = solicitacao.datanascimento;
  let usfOrigem = solicitacao.usfOrigem;
  let dataMalote = solicitacao.dataMalote;
  let observacoes = solicitacao.observacoes;

  // --- Lista hardcoded de todas as especialidades possíveis ---
  const todasEspecialidades = [
  { value: 'ANGIOLOGISTA', label: 'Angiologista' },
  { value: 'CARDIOLOGISTA', label: 'Cardiologista' },
  { value: 'DOPPLER', label: 'Doppler' },
  { value: 'ENDOCRINOLOGISTA', label: 'Endocrinologista' },
  { value: 'GASTROENTEROLOGISTA', label: 'Gastroenterologista' },
  { value: 'MASTOLOGISTA', label: 'Mastologista' },
  { value: 'MAMOGRAFIA', label: 'Mamografia' },
  { value: 'DERMATOLOGISTA', label: 'Dermatologista' },
  { value: 'NEFROLOGISTA', label: 'Nefrologista' },
  { value: 'HOLTER', label: 'Holter' },
  { value: 'ORTOPEDISTA', label: 'Ortopedista' },
  { value: 'OFTALMOLOGISTA', label: 'Oftalmologista' },
  { value: 'NEUROLOGISTA', label: 'Neurologista' },
  { value: 'NEUROPEDIATRIA', label: 'Neuropediatria' },
  { value: 'OTORRINOLARINGOLOGISTA', label: 'Otorrinolaringologista' },
  { value: 'UROLOGISTA', label: 'Urologista' },
  { value: 'REUMATOLOGISTA', label: 'Reumatologista' },
  { value: 'PNEUMOLOGISTA', label: 'Pneumologista' },
  { value: 'TESTE_ERGOMETRICO', label: 'Teste Ergométrico' },
  { value: 'MAPA', label: 'Mapa' },
  { value: 'HEMATOLOGISTA', label: 'Hematologista' },
  { value: 'TOMOGRAFIA', label: 'Tomografia' },
  { value: 'RESSONANCIA', label: 'Ressonância' },
  { value: 'CINTILOGRAFIA', label: 'Cintilografia' },
  { value: 'ELETRONEUROMIOGRAFIA', label: 'Eletroneuromiografia' },
  { value: 'CATETERISMO', label: 'Cateterismo' }
];

  // Objeto reativo para a nova especialidade a ser adicionada
  let novaEspecialidadeObj = { especialidadeSolicitada: '', status: 'AGUARDANDO', prioridade: 'NORMAL' };

  // --- Especialidades pendentes (se ainda usar esta variável) ---
  let especPendentes = solicitacao.especialidades.filter(e => e.status === 'AGUARDANDO');
  // 'novaEspec' não será mais usada para adicionar especialidades, apenas para o agendamento
  let novaEspec = ''; // Mantido para o Agendamento, se aplicável



  // Atualiza dados do paciente (solicitação)
  async function salvarPaciente() {
    const res = await fetch(`http://localhost:8080/api/solicitacoes/${solicitacao.id}`, {
      method: 'PUT',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify({
        nomePaciente,
        cpfPaciente,
        cns,
        datanascimento,
        usfOrigem,
        dataMalote,
        observacoes
      })
    });
    if (res.ok) {
      alert('Paciente atualizado com sucesso');
      goto(`/paciente/${solicitacao.id}`);
    } else {
      alert('Erro ao atualizar paciente');
    }
  }

  // Adiciona nova especialidade à solicitação
  async function adicionarEspecialidade() {
    if (!novaEspecialidadeObj.especialidadeSolicitada) {
      alert('Selecione uma especialidade para adicionar.');
      return;
    }
    const res = await fetch(`http://localhost:8080/api/solicitacoes/${solicitacao.id}/especialidades`, {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(novaEspecialidadeObj) // Enviando o objeto completo com status e prioridade
    });
    if (res.ok) {
      alert('Especialidade adicionada');
      location.reload(); // Recarrega a página para mostrar a nova especialidade
    } else {
      alert('Erro ao adicionar especialidade');
    }
  }

 
</script>

<div class="flex h-screen bg-gray-100">
  <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">Regula System</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Nova Solicitação</a>
      <a href="/agendar" class="py-2 px-4 rounded hover:bg-emerald-800  ">Agendamento</a>
      <a href="/paciente" class="py-2 px-4 rounded bg-emerald-700">Paciente</a>
      <a href="/exportar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
  </aside>

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white p-4 flex justify-between items-center">
      <h1 class="text-xl font-semibold">Dados do Paciente</h1>
      <div><span class="font-bold">CPF:</span> {cpfPaciente}</div>
    </header>

    <main class="p-6 overflow-auto space-y-6">
      <section class="bg-white rounded-lg shadow p-6">
        <h2 class="text-lg font-bold text-emerald-800 mb-4">Editar Paciente</h2>
        <div class="grid grid-cols-1 md:grid-cols-5 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Nome</label>
            <input type="text" bind:value={nomePaciente} class="w-full border border-gray-300 rounded p-2" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Cartão do SUS</label>
            <input type="text" bind:value={cns} class="w-full border border-gray-300 rounded p-2" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">USF Origem</label>
            <input type="text" bind:value={usfOrigem} class="w-full border border-gray-300 rounded p-2" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Data de Nascimento</label>
            <input type="date" bind:value={datanascimento} class="w-full border border-gray-300 rounded p-2" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Data Recebimento</label>
            <input type="date" bind:value={dataMalote} class="w-full border border-gray-300 rounded p-2" />
          </div>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-1 gap-4">
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-1">Observações</label>
            <textarea bind:value={observacoes} rows="3" class="w-full border border-gray-300 rounded p-2"></textarea>
          </div>
        </div>
        <button on:click={salvarPaciente} class="mt-4 bg-emerald-800 text-white px-6 py-2 rounded hover:bg-emerald-900">Salvar Alterações</button>
      </section>

      <section class="bg-white rounded-lg shadow p-6">
        <h2 class="text-lg font-bold text-emerald-800 mb-4">Histórico de Agendamentos</h2>
        {#if agendamentos.length === 0}
          <p class="text-gray-500">Nenhum agendamento encontrado.</p>
        {:else}
          <ul class="space-y-2">
            {#each agendamentos as ag}
              {#each historico as h }
                <li class="flex justify-between p-2 border-b">
                <span>Local: {ag.localAgendado.replace(/_/g,' ')}</span>
                <span>Data do Agendamento: {ag.dataAgendada}</span>
                <span>Observação: {ag.observacoes}</span>
                <span>Especialidade: {h.especialidadeSolicitada}</span>
                <span>Status: {h.status}</span>
              </li>
              {/each}
            
            {/each}
          </ul>
        {/if}
      </section>

      <section class="bg-white rounded-lg shadow p-6">
        <h2 class="text-lg font-bold text-emerald-800 mb-4">Especialidades</h2>
        <ul class="space-y-2 mb-4">
          {#each solicitacao.especialidades as e}
            <li class="flex justify-between p-2 border-b">
              <span>{e.especialidadeSolicitada}</span>
              <span class="text-sm font-semibold">{e.status}</span>
            </li>
          {/each}
        </ul>
        <div class="space-y-4 p-4 border border-gray-200 rounded-lg">
            <h3 class="text-md font-semibold text-gray-800 mb-2">Adicionar Nova Especialidade</h3>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Especialidade</label>
                    <select bind:value={novaEspecialidadeObj.especialidadeSolicitada} class="w-full border border-gray-300 rounded p-2" required>
                        <option value="" disabled selected>Selecione a Especialidade</option>
                        {#each todasEspecialidades as se}
                            <option value={se.value}>{se.label}</option>
                        {/each}
                    </select>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
                    <select bind:value={novaEspecialidadeObj.status} class="w-full border border-gray-300 rounded p-2">
                        <option value="AGUARDANDO">Aguardando</option>
                        <option value="AGENDADO">Agendado</option>
                        <option value="REALIZADO">Realizado</option>
                        <option value="CANCELADO">Cancelado</option>
                    </select>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Prioridade</label>
                    <select bind:value={novaEspecialidadeObj.prioridade} class="w-full border border-gray-300 rounded p-2">
                        <option value="NORMAL">Normal</option>
                        <option value="URGENTE">Urgente</option>
                        <option value="EMERGENCIA">Emergência</option>
                    </select>
                </div>
            </div>
            <button on:click={adicionarEspecialidade} class="mt-4 bg-emerald-800 text-white px-6 py-2 rounded hover:bg-emerald-900">Adicionar Especialidade</button>
        </div>
      </section>

      
    </main>
  </div>
</div>