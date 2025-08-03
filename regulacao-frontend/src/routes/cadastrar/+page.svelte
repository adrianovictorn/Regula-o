<script lang="ts">
  import { goto } from '$app/navigation';
  // Importa a nossa função postApi que já envia o token JWT
  import { postApi } from '$lib/api.js'; 
  import { opcoesEspecialidades } from '$lib/Especialidades.js'; // Usando o objeto centralizado
  import RoleBasedMenu from '$lib/RoleBasedMenu.svelte';
  import UserMenu from '$lib/UserMenu.svelte';

  // --- ESTADO DO FORMULÁRIO ---
  let errors: { [key: string]: string } = {};
  let isLoading = false;
  
  // Campos do formulário
  let nomePaciente = '';
  let cpfPaciente = '';
  let cns = '';
  let telefone ='';
  let datanascimento = '';
  let usfOrigem = '';
  let dataMalote = '';
  let observacoes = '';

  // Inicializa uma especialidade em branco com status/prioridade padrão
  let especialidades = [
    { especialidadeSolicitada: '', status: 'AGUARDANDO', prioridade: 'NORMAL' }
  ];

  // --- FUNÇÕES DO FORMULÁRIO ---

  // Adiciona mais um bloco de especialidade
  function addEspecialidade() {
    especialidades = [
      ...especialidades,
      { especialidadeSolicitada: '', status: 'AGUARDANDO', prioridade: 'NORMAL' }
    ];
  }

  // Remove a especialidade de índice i
  function removerEspecialidade(i: number) {
    especialidades = especialidades.filter((_, idx) => idx !== i);
  }

  // Submete o formulário ao backend usando o helper de API
  async function submeterForm() {
    isLoading = true;
    
    const payload = {
      nomePaciente,
      cpfPaciente: cpfPaciente.replace(/\D/g, ''), // Envia somente os números
      cns,
      telefone,
      datanascimento,
      usfOrigem,
      dataMalote,
      observacoes,
      especialidades: especialidades.filter(e => e.especialidadeSolicitada)
    };

    try {
      const res = await postApi('solicitacoes', payload);

      if (res.ok) { // Status 2xx (Sucesso)
        alert('Solicitação cadastrada com sucesso!');
        limparCampos();
        goto('/cadastrar');
      
      } else if (res.status === 409) { // 409 Conflict: Nosso sinal de CPF DUPLICADO!
        alert("CPF já cadastrado, consulte o módulo Paciente");
      
      } else { // Qualquer outro erro (400, 500, etc.)
        alert(`Ocorreu um erro (${res.status}). Verifique os dados e tente novamente.`);
      }

    } catch (err) {
      console.error("Erro de conexão:", err);
      alert("Não foi possível conectar ao servidor. Verifique sua conexão.");
    } finally {
      isLoading = false;
    }
  }

  function limparCampos() {
    nomePaciente = '';
    cpfPaciente = '';
    cns = '';
    telefone ='';
    datanascimento = '';
    usfOrigem = '';
    dataMalote = '';
    observacoes = '';
    especialidades = [{ especialidadeSolicitada: '', status: 'AGUARDANDO', prioridade: 'NORMAL' }];
  }

  function formatarCPF(e: Event) {
    const target = e.target as HTMLInputElement;
    let d = target.value.replace(/\D/g, '').slice(0, 11);
    d = d.replace(/^(\d{3})(\d)/, '$1.$2').replace(/^(\d{3}\.\d{3})(\d)/, '$1.$2').replace(/(\d{3}\.\d{3}\.\d{3})(\d)/, '$1-$2');
    cpfPaciente = d;
  }
</script>

<svelte:head>
    <title>Consulta</title>
</svelte:head>

<div class="flex h-screen bg-gray-100">
  <!-- Sidebar -->
  <RoleBasedMenu activePage="/cadastrar" />

  <!-- Main Area -->
  <div class="flex-1 flex flex-col">

    <!-- Header -->
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Cadastro de Solicitações</h1>
          <UserMenu/>
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
          <div class="grid grid-cols-1 lg:grid-cols-5 gap-6">
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
              <label class="text-sm font-medium text-gray-700 mb-1">Telefone</label>
              <input type="text" bind:value={telefone} placeholder="(00)0 0000-0000" maxlength="15" class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500">
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
                    {#each opcoesEspecialidades.especialidadesMedicas as e}
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


