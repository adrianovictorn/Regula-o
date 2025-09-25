<script lang="ts">
  import { onMount } from "svelte";
  import { getApi, putApi } from "$lib/api";
  import { opcoesEspecialidades } from "$lib/Especialidades.js";
    import Menu from "$lib/Menu.svelte";
    import { toast } from 'svelte-sonner';
    import UserMenu from "$lib/UserMenu.svelte";

  // --- Estado do Componente (Svelte 5 Runes) ---
  let isLoading = $state(true);
  let error = $state<string | null>(null);
  let solicitacoesPendentes = $state<any[]>([]);
  
  let buscar = $state('');
  let currentPage = $state(1);
  const itemsPerPage = 10;

  function formatarData(dataString: string | null): string {
    if (!dataString) return 'N/A';
    // Cria a data em UTC para evitar problemas de fuso horário
    const data = new Date(dataString);
    // Adiciona 1 dia para compensar o fuso-horário (a data vem como YYYY-MM-DD e o JS pode interpretar como dia anterior)
    data.setDate(data.getDate() + 1);
    return data.toLocaleDateString('pt-BR');
  }

  function getNomeEspecialidade(valorEnum: string): string {
    const todasAsOpcoes = [
      ...opcoesEspecialidades.especialidadesMedicas,
      ...opcoesEspecialidades.examesEProcedimentos
    ];
    // Encontra o objeto correspondente ao valor do enum
    const opcao = todasAsOpcoes.find(opt => opt.value === valorEnum);
    // Retorna o label se encontrou, ou o próprio valor do enum como fallback
    return opcao ? opcao.label : valorEnum;
  }

  // --- Carregamento e Processamento de Dados (Client-Side) ---
  onMount(async () => {
    try {
      // 1. Busca os dados da API de forma autenticada
      const response = await getApi('solicitacoes'); 
      if (!response.ok) {
        throw new Error('Falha ao carregar as solicitações do servidor.');
      }
      const todasSolicitacoes = await response.json();

      // 2. Filtra apenas as solicitações que têm itens pendentes
      solicitacoesPendentes = todasSolicitacoes.filter((s: any) => 
        s.especialidades.some((e: any) => e.status === 'AGENDADO')
      );

    } catch (e: any) {
      error = e.message;
    } finally {
      isLoading = false;
    }
  });

  async function confirmarPresenca(idEspecialidade: number) {
    try {
      // CORREÇÃO: A URL correta, conforme seu Controller
      const res = await putApi(`especialidades/${idEspecialidade}/realizado`);

      if (!res.ok) {
        throw new Error('Falha ao confirmar a presença.');
      }
      
      // Atualiza a UI para remover o item da lista
      solicitacoesPendentes = solicitacoesPendentes.map(s => ({
          ...s,
          especialidades: s.especialidades.filter(e => e.id !== idEspecialidade)
      })).filter(s => s.especialidades.some(e => e.status === 'AGENDADO'));

      toast.success('Presença confirmada!'); // (Opcional)

    } catch (err: any) {
        error = err.message;
        toast.error(err.message); // (Opcional)
    }
  }

  async function faltouPresenca(idEspecialidade: number) {
    try {
      // CORREÇÃO: A URL correta, conforme seu Controller
      const res = await putApi(`especialidades/${idEspecialidade}/faltou`);

      if (!res.ok) {
        throw new Error('Falha ao registrar a falta.');
      }
      
      // Atualiza a UI para remover o item da lista
       solicitacoesPendentes = solicitacoesPendentes.map(s => ({
          ...s,
          especialidades: s.especialidades.filter(e => e.id !== idEspecialidade)
      })).filter(s => s.especialidades.some(e => e.status === 'AGENDADO'));
      
      toast.success('Falta registrada com sucesso!'); // (Opcional)

    } catch (err: any) {
        error = err.message;
        toast.error(err.message); // (Opcional)
    }
  }

  // --- Lógica Reativa com Runes ---
  // 3. Converte a sintaxe de reatividade de `$` para `$derived`
  let filtradas = $derived(
    buscar.trim()
      ? solicitacoesPendentes.filter(s => {
          const termo = buscar.toLowerCase();
          const nomeMatch = s.nomePaciente.toLowerCase().includes(termo);
          const cpfMatch = s.cpfPaciente.includes(termo);
          const usfMatch = s.usfOrigem.toLowerCase().includes(termo);
          const espMatch = s.especialidades.some((e: any) => e.especialidadeSolicitada.toLowerCase().includes(termo));
          const prioMatch = s.especialidades.some((e: any) => e.prioridade.toLowerCase().includes(termo));
          return nomeMatch || cpfMatch || usfMatch || espMatch || prioMatch;
        })
      : solicitacoesPendentes
  );
  
  // CORREÇÃO: Trocado 'filtrados' por 'filtradas' para corresponder ao nome da variável.
  let totalPages = $derived(Math.ceil(filtradas.length / itemsPerPage));
  let paged = $derived(filtradas.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage));

  // Efeito para ajustar a página atual se a filtragem mudar
  $effect(() => {
      if (totalPages > 0 && currentPage > totalPages) {
          currentPage = totalPages;
      }
  });
  
  function prevPage() { if (currentPage > 1) currentPage--; }
  function nextPage() { if (currentPage < totalPages) currentPage++; }
</script>

<svelte:head>
    <title>Agendados</title>
</svelte:head>

<div class="flex min-h-screen bg-gray-100">
  <!-- Sidebar navigation -->
    <Menu activePage="/home" />

  <!-- Main content area -->
  <div class="flex-1 flex flex-col">
    <!-- Header -->
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Pacientes Agendados</h1>
          <UserMenu/>
    </header>

    <!-- Content -->
    <main class="flex-1 overflow-auto p-6">
      <div class="bg-white rounded-lg shadow-lg p-6 space-y-6">
        <!-- Title and search -->
        <div class="flex flex-col md:flex-row md:justify-between md:items-center">
          <h2 class="text-2xl font-bold text-emerald-800 mb-4 md:mb-0">Lista de Pacientes Agendados</h2>
          <div class="flex w-full md:w-1/2">
            <input
              type="text"
              placeholder="Buscar por nome, CPF, especialidade..."
              bind:value={buscar}
              class="flex-1 border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
            />
          </div>
        </div>

        <!-- Feedback de Carregamento e Erro -->
        {#if isLoading}
            <p class="text-center text-gray-500 py-10">Carregando solicitações agendadas...</p>
        {:else if error}
            <p class="text-center text-red-600 bg-red-100 p-4 rounded-lg">Erro ao carregar dados: {error}</p>
        {:else}
            <p class="text-gray-600">Total: {filtradas.length}</p>

            <!-- List items -->
            {#if filtradas.length === 0}
              <p class="text-center text-gray-500 py-10">
                {#if buscar.trim()}
                    Nenhuma solicitação encontrada para "{buscar}".
                {:else}
                    Nenhuma solicitação pendente no momento.
                {/if}
              </p>
            {:else}
              <ul class="space-y-4">
                {#each paged as s, idx (s.id)}
                  <li class="bg-white border border-gray-200 rounded-lg p-4 hover:shadow transition flex">
                    <div class="text-emerald-700 font-bold text-xl mr-4">{(currentPage - 1) * itemsPerPage + idx + 1}.</div>
                    <div class="flex-1">
                      <a href={`/paciente/${s.id}`} class="block hover:underline">
                        <h3 class="text-lg font-bold mb-2">{s.nomePaciente}</h3>
                      </a>
                      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2 text-sm">
                        <div><span class="font-semibold">CPF:</span> {s.cpfPaciente}</div>
                        <div><span class="font-semibold">USF:</span> {s.usfOrigem}</div>
                        <div><span class="font-semibold">Data:</span> {formatarData(s.dataMalote)}</div>
                       
                      <div class="col-span-full mt-2">
                     <span class="font-semibold text-gray-700">Procedimentos Agendados:</span>
  
                      <ul class="list-disc list-inside pl-4 mt-1 space-y-1">
                        {#each s.especialidades.filter(e => e.status === 'AGENDADO') as esp}
                          <li class="text-gray-600 flex justify-between items-center">
                            
                            <div>
                              {getNomeEspecialidade(esp.especialidadeSolicitada)} -

                              {#if esp.agendamentoId}
                                {@const agendamento = s.agendamentos.find(ag => ag.id === esp.agendamentoId)}
                                {#if agendamento}
                                  <span class="text-xs py-0.5 bg-emerald-200 w-max rounded">
                                    Agendado para: {formatarData(agendamento.dataAgendada)}
                                  </span>
                                {/if}
                              {/if}
                            </div>

                            <div class="flex items-center space-x-2">
                              <button 
                                onclick={() => confirmarPresenca(esp.id)}
                                class="px-2 py-1 text-xs font-medium text-white bg-green-600 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition-colors"
                                title="Confirmar Presença"
                              >
                                ✓ Realizado
                              </button>
                              <button 
                                onclick={() => faltouPresenca(esp.id)}
                                class="px-2 py-1 text-xs font-medium text-white bg-red-600 rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition-colors"
                                title="Registrar Falta"
                              >
                                ✗ Faltou
                              </button>
                            </div>

                          </li>
                        {/each}
                      </ul>
                        </div>
                        <div class="col-span-full"><span class="font-semibold">Observações:</span> {s.observacoes}</div>
                      </div>
                    </div>
                  </li>
                {/each}
              </ul>

              <!-- Pagination controls -->
              {#if totalPages > 1}
                <div class="flex justify-center items-center space-x-2 mt-6">
                  <button onclick={prevPage} class="px-3 py-1 bg-emerald-600 hover:bg-emerald-800 cursor-pointer text-white rounded disabled:opacity-50" disabled={currentPage === 1}>&laquo; Anterior</button>
                  <span class="text-gray-700">Página {currentPage} de {totalPages}</span>
                  <button onclick={nextPage} class="px-3 py-1 bg-emerald-600 hover:bg-emerald-800 cursor-pointer text-white rounded disabled:opacity-50" disabled={currentPage === totalPages}>Próximo &raquo;</button>
                </div>
              {/if}
            {/if}
        {/if}
      </div>
    </main>
  </div>
</div>