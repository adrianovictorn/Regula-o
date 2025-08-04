<script lang="ts">
  import { onMount } from "svelte";
  import { getApi } from "$lib/api";
    import Menu from "$lib/Menu.svelte";
    import { opcoesEspecialidades } from "$lib/Especialidades";
    import UserMenu from "$lib/UserMenu.svelte";
    import RoleBasedMenu from "$lib/RoleBasedMenu.svelte";

  // --- Estado do Componente (Svelte 5 Runes) ---
  let isLoading = $state(true);
  let error = $state<string | null>(null);
  let solicitacoes = $state<any[]>([]); // Armazenará os dados filtrados da USF01
  
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
      // 1. Busca TODAS as solicitações usando a API segura
      const response = await getApi('solicitacoes'); 
      if (!response.ok) {
        throw new Error('Falha ao carregar as solicitações do servidor.');
      }
      const todasSolicitacoes = await response.json();

      // 2. Filtra os dados APÓS a busca, no lado do cliente
      solicitacoes = todasSolicitacoes.filter((s: any) => 
        s.usfOrigem === 'USF06' && // Filtro específico para USF01
        s.especialidades.some((e: any) => e.status === 'AGUARDANDO') // Filtro para pendentes
      );

    } catch (e: any) {
      error = e.message;
    } finally {
      isLoading = false;
    }
  });

  // --- Lógica Reativa com Runes ---
  // 3. Usa `$derived` para criar valores que se atualizam automaticamente
  let filtradas = $derived(
    buscar.trim()
      ? solicitacoes.filter(s => {
          const termo = buscar.toLowerCase();
          const nomeMatch = s.nomePaciente.toLowerCase().includes(termo);
          const cpfMatch = s.cpfPaciente.includes(termo);
          const usfMatch = s.usfOrigem.toLowerCase().includes(termo);
          const espMatch = s.especialidades.some((e: any) => e.especialidadeSolicitada.toLowerCase().includes(termo));
          const prioMatch = s.especialidades.some((e: any) => e.prioridade.toLowerCase().includes(termo));
          return nomeMatch || cpfMatch || usfMatch || espMatch || prioMatch;
        })
      : solicitacoes
  );
  
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
    <title>USF06</title>
</svelte:head>

<div class="flex h-screen bg-gray-100">
  <!-- Sidebar navigation -->
   <RoleBasedMenu activePage="/home" />

  <!-- Main content area -->
  <div class="flex-1 flex flex-col">
    <!-- Header -->
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Pacientes Pendentes - USF 06</h1>
          <UserMenu/>
    </header>

    <!-- Content -->
    <main class="flex-1 overflow-auto p-6">
      <div class="bg-white rounded-lg shadow-lg p-6 space-y-6">
        <!-- Title and search -->
        <div class="flex flex-col md:flex-row md:justify-between md:items-center">
          <h2 class="text-2xl font-bold text-emerald-800 mb-4 md:mb-0">Lista de Pacientes Pendentes (USF 06)</h2>
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
            <p class="text-center text-gray-500 py-10">Carregando solicitações...</p>
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
                    Nenhuma solicitação pendente para a USF 06 no momento.
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
                        <div>
                          <span class="font-semibold">Prioridade:</span>
                          <span
                            class="px-2 py-1 rounded text-white text-xs"
                            class:bg-red-600={s.especialidades[0]?.prioridade === 'EMERGENCIA'}
                            class:bg-yellow-500={s.especialidades[0]?.prioridade === 'URGENTE'}
                            class:bg-green-500={s.especialidades[0]?.prioridade === 'NORMAL'}
                          >{s.especialidades[0]?.prioridade}</span>
                        </div>
                        <div class="col-span-full"><span class="font-semibold">Especialidade:</span> {s.especialidades.map(e => getNomeEspecialidade(e.especialidadeSolicitada)).join(', ')}</div>
                        <div class="col-span-full"><span class="font-semibold">Observações:</span> {s.observacoes}</div>
                      </div>
                    </div>
                  </li>
                {/each}
              </ul>

              <!-- Pagination controls -->
              {#if totalPages > 1}
                <div class="flex justify-center items-center space-x-2 mt-6">
                  <!-- CORREÇÃO: on:click para onclick -->
                  <button onclick={prevPage} class="px-3 py-1 bg-emerald-600 text-white rounded disabled:opacity-50" disabled={currentPage === 1}>&laquo; Anterior</button>
                  <span class="text-gray-700">Página {currentPage} de {totalPages}</span>
                  <button onclick={nextPage} class="px-3 py-1 bg-emerald-600 text-white rounded disabled:opacity-50" disabled={currentPage === totalPages}>Próximo &raquo;</button>
                </div>
              {/if}
            {/if}
        {/if}
      </div>
    </main>
  </div>
</div>