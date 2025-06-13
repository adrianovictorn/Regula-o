
<script lang="ts">
  import { goto, invalidateAll } from '$app/navigation';

  export let data;
  // pega direto do `data`
  const { solicitacoes } = data;

  let errors: { [key: string]: string } = {}
  // campos do formulário
  let nomePaciente = '';
  let cpfPaciente = '';
  let cns = '';
  let datanascimento = '';
  let usfOrigem = '';
  let dataMalote = '';
  let observacoes = '';

  

  // lista de todas as especialidades possíveis
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


  // inicializa uma especialidade em branco com status/prioridade padrão
  let especialidades = [
    { especialidadeSolicitada: '', status: 'AGUARDANDO', prioridade: 'NORMAL' }
  ];

  // adiciona mais um bloco de especialidade
  function addEspecialidade() {
    especialidades = [
      ...especialidades,
      { especialidadeSolicitada: '', status: 'AGUARDANDO', prioridade: 'NORMAL' }
    ];
  }

  // remove a especialidade de índice i
  function removerEspecialidade(i) {
    especialidades = especialidades.filter((_, idx) => idx !== i);
  }

  // submete o formulário ao backend
   async function submeterForm() {
    // 1. Limpa os erros da tentativa anterior
    errors = {};

    const payload = {
      nomePaciente,
      cpfPaciente: cpfPaciente.replace(/\D/g, ''), // Envia somente os números
      cns,
      datanascimento,
      usfOrigem,
      dataMalote,
      observacoes,
      especialidades: especialidades.filter(e => e.especialidadeSolicitada) // Envia apenas as que foram preenchidas
    };

    try {
      const res = await fetch(`/api/solicitacoes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (res.ok) {
        // 2. Sucesso!
        alert('Solicitação cadastrada com sucesso!');
        limparCampos();
        await invalidateAll(); // Atualiza os dados em outras partes do app
        goto('/home'); // Redireciona para o dashboard

      } else if (res.status === 400) {
        // 3. Erro de Validação!
        const errorData = await res.json();
        // Mapeia os erros recebidos para o nosso objeto 'errors'
        for (const error of errorData.errors) {
            errors[error.field] = error.defaultMessage;
        }
      } else {
        // 4. Outros erros do servidor
        alert(`Erro no servidor: ${res.status}. Tente novamente.`);
      }
    } catch (err) {
      console.error("Erro de conexão:", err);
      alert("Não foi possível conectar ao servidor. Verifique sua conexão.");
    }
  }

  function limparCampos(){
  nomePaciente = '';
  cpfPaciente = '';
  cns = '';
  datanascimento = '';
  usfOrigem = '';
  dataMalote = '';
  observacoes = '';
  especialidades = [{ especialidadeSolicitada: '', status: 'AGUARDANDO', prioridade: 'NORMAL' }];
}

 function formatarCPF(e) {
    let d = e.target.value.replace(/\D/g, '').slice(0,11);
    d = d.replace(/^(\d{3})(\d)/, '$1.$2').replace(/^(\d{3}\.\d{3})(\d)/, '$1.$2').replace(/(\d{3}\.\d{3}\.\d{3})(\d)/, '$1-$2');
    cpfPaciente = d;
  }
</script>

<div class="flex h-screen bg-gray-100">
  <!-- Sidebar -->
  <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">Regula System</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded bg-emerald-700 transition">Nova Solicitação</a>
      <a href="/exames" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Laboratório</a>
      <a href="/agendar" class="py-2 px-4 rounded hover:bg-emerald-800">Agendamento</a>
      <a href="/paciente" class="py-2 px-4 rounded hover:bg-emerald-800">Paciente</a>
      <a href="/exportar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
  </aside>

  <!-- Main Area -->
  <div class="flex-1 flex flex-col">

    <!-- Header -->
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Cadastro de Solicitações</h1>
      <div>Bem-vindo(a), Usuário</div>
    </header>

    <!-- Form Container -->
    <main class="flex-1 overflow-auto p-6">
      <div class="bg-white rounded-lg shadow-lg p-6">
        <h2 class="text-2xl font-bold text-emerald-800 mb-6">Cadastro de Solicitação</h2>
        <form on:submit|preventDefault={submeterForm} class="space-y-6">

          <!-- Dados Principais -->
          <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div class="flex flex-col">
              <label class="text-sm font-medium text-gray-700 mb-1">Data do Recebimento</label>
              <input type="date" bind:value={dataMalote} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
            </div>
            <div class="flex flex-col">
              <label class="text-sm font-medium text-gray-700 mb-1">USF Origem</label>
              <select bind:value={usfOrigem} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required>
                <option value="" disabled>Selecione...</option>
                {#each ['USF01','USF02','USF03','USF04','USF05','USF06'] as u}
                  <option value={u}>{u}</option>
                {/each}
              </select>
            </div>
          </div>

          <!-- Dados Paciente -->
          <div class="grid grid-cols-1 lg:grid-cols-4 gap-6">
            <div class="flex flex-col">
              <label class="text-sm font-medium text-gray-700 mb-1">Nome do Paciente</label>
              <input type="text" bind:value={nomePaciente} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
            </div>
            <div class="flex flex-col">
              <label class="text-sm font-medium text-gray-700 mb-1">CPF</label>
              <input type="text" bind:value={cpfPaciente} on:input={formatarCPF} placeholder="000.000.000-00" class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
            </div>
             <div class="flex flex-col">
              <label class="text-sm font-medium text-gray-700 mb-1">CNS</label>
              <input type="text" bind:value={cns} placeholder="Digite o cartão do SUS" maxlength="15" class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
            </div>
            <div class="flex flex-col">
              <label class="text-sm font-medium text-gray-700 mb-1">Data de Nascimento</label>
              <input type="date" bind:value={datanascimento} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
            </div>
          </div>

          <!-- Observações -->
          <div class="flex flex-col">
            <label class="text-sm font-medium text-gray-700 mb-1">Observações</label>
            <textarea bind:value={observacoes} rows="4" maxlength="500" class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"></textarea>
          </div>

          <!-- Especialidades -->
          <div>
            <h3 class="text-lg font-semibold text-gray-800 mb-4">Especialidades</h3>
            <div class="space-y-4">
              {#each especialidades as esp, i}
                <div class="grid grid-cols-1 md:grid-cols-4 gap-4 items-center p-4 border border-gray-200 rounded-lg">
                  <!-- Procedimento -->
                  <select bind:value={esp.especialidadeSolicitada} class="border-gray-300 rounded-lg p-2" required>
                    <option value="" disabled>Procedimento</option>
                    {#each todasEspecialidades as e}
                      <option value={e.value}>{e.label}</option>
                    {/each}
                  </select>

                  <!-- Status -->
                  <select bind:value={esp.status} class="border-gray-300 rounded-lg p-2">
                    <option value="AGUARDANDO">Aguardando</option>
                    <option value="AGENDADO">Agendado</option>
                    <option value="REALIZADO">Realizado</option>
                    <option value="CANCELADO">Cancelado</option>
                  </select>

                  <!-- Prioridade -->
                  <select bind:value={esp.prioridade} class="border-gray-300 rounded-lg p-2">
                    <option value="NORMAL">Normal</option>
                    <option value="URGENTE">Urgente</option>
                    <option value="EMERGENCIA">Emergência</option>
                  </select>

                  <!-- Remover -->
                  <button type="button" on:click={() => removerEspecialidade(i)} class="text-red-500 hover:text-red-700 font-medium">✕ Remover</button>
                </div>
              {/each}
            </div>
            <button type="button" on:click={addEspecialidade} class="mt-2 text-emerald-600 hover:text-emerald-800 font-medium">+ Adicionar Especialidade</button>
          </div>

          <!-- Submit -->
          <button type="submit" class="w-full bg-emerald-800 text-white py-3 rounded-lg hover:bg-emerald-900 transition">Enviar</button>
        </form>
      </div>
    </main>
  </div>
</div>


