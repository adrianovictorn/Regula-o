<script lang="ts">
  import { onMount } from "svelte";
  import { getApi } from "$lib/api";

  // Variáveis de estado declaradas com as "runes" do Svelte 5
  let isLoading = $state(true);
  let error = $state<string | null>(null);
  let listaPacientes = $state<any[]>([]);
  let buscar = $state('');
  let currentPage = $state(1);
  const itemsPerPage = 10;

  // Busca e processa os dados quando o componente é montado no navegador
  onMount(async () => {
    try {
      const response = await getApi('solicitacoes');
      if (!response.ok) {
        throw new Error('Falha ao carregar a lista de solicitações.');
      }
      const solicitacoes = await response.json();

      // Lógica para obter pacientes únicos (executa DEPOIS que os dados chegam)
      const pacientesUnicos = new Map<string, { nomePaciente: string; cpfPaciente: string; usfOrigem: string; id: number; }>();
      solicitacoes.forEach((s: any) => {
        if (s.cpfPaciente && !pacientesUnicos.has(s.cpfPaciente)) {
          pacientesUnicos.set(s.cpfPaciente, {
            nomePaciente: s.nomePaciente,
            cpfPaciente: s.cpfPaciente,
            usfOrigem: s.usfOrigem,
            // Usamos o ID da primeira solicitação encontrada para o link do paciente
            id: s.id 
          });
        }
      });
      
      listaPacientes = Array.from(pacientesUnicos.values());

    } catch (e: any) {
      error = e.message;
    } finally {
      isLoading = false;
    }
  });

  // Valor derivado para a lista de pacientes filtrados.
  // Ele é recalculado automaticamente quando `buscar` ou `listaPacientes` mudam.
  let filtrados = $derived(
    buscar.trim()
      ? listaPacientes.filter(p => {
          const termo = buscar.toLowerCase();
          return (
            p.nomePaciente.toLowerCase().includes(termo) ||
            p.cpfPaciente.includes(termo) ||
            p.usfOrigem.toLowerCase().includes(termo)
          );
        })
      : listaPacientes
  );
  
  // CORREÇÃO: Para usar um valor derivado dentro de outro, acesse-o diretamente sem `()`.
  let totalPages = $derived(Math.ceil(filtrados.length / itemsPerPage));
  
  // Um "efeito" que roda quando suas dependências mudam. Útil para lógica imperativa.
  $effect(() => {
      if (totalPages > 0 && currentPage > totalPages) {
          currentPage = totalPages;
      }
  });
  
  // CORREÇÃO: `filtrados` é usado diretamente para chamar o método `.slice()`.
  let paged = $derived(filtrados.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage));

  function prevPage() { if (currentPage > 1) currentPage--; }
  function nextPage() { if (currentPage < totalPages) currentPage++; }
</script>

<div class="flex h-screen bg-gray-100">
 <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">SIRG</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Nova Solicitação</a>
      <a href="/exames" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Laboratório</a>
      <a href="/agendar" class="py-2 px-4 rounded hover:bg-emerald-800">Agendamento</a>
      <a href="/paciente" class="py-2 px-4 rounded bg-emerald-700">Paciente</a>
      <a href="/exportar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • © 2025</div>
 </aside>

 <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Pacientes</h1>
      <div>Bem-vindo(a), Usuário</div>
    </header>

    <main class="flex-1 overflow-auto p-6">
      <div class="bg-white rounded-lg shadow-lg p-6 space-y-6">
        <div class="flex flex-col md:flex-row md:justify-between md:items-center">
          <h2 class="text-2xl font-bold text-emerald-800 mb-4 md:mb-0">Lista de Pacientes Cadastrados</h2>
          <div class="flex w-full md:w-1/2">
            <input
              type="text"
              placeholder="Buscar por nome, CPF, USF..."
              bind:value={buscar}
              class="flex-1 border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
            />
          </div>
        </div>

        {#if isLoading}
          <div class="text-center text-gray-500 py-10">
            <p>Carregando pacientes...</p>
          </div>
        {:else if error}
           <div class="text-center text-red-600 bg-red-100 p-4 rounded-lg">
             <p><strong>Erro ao carregar dados:</strong> {error}</p>
           </div>
        {:else}
            <!-- CORREÇÃO: Usando a variável derivada diretamente no HTML -->
            <p class="text-gray-600">Total de pacientes encontrados: {filtrados.length}</p>

            {#if filtrados.length === 0 && buscar.trim()}
                <p class="text-center text-gray-500 py-10">Nenhum paciente encontrado para "{buscar}".</p>
            {:else if listaPacientes.length === 0}
                <p class="text-center text-gray-500 py-10">Nenhum paciente cadastrado no sistema.</p>
            {:else}
              <ul class="space-y-4">
                <!-- CORREÇÃO: Usando a variável derivada diretamente no HTML -->
                {#each paged as p, idx (p.cpfPaciente)}
                  <li class="bg-white border border-gray-200 rounded-lg p-4 hover:shadow-md transition flex items-center">
                    <div class="text-emerald-700 font-bold text-xl mr-4">{(currentPage - 1) * itemsPerPage + idx + 1}.</div>
                    <div class="flex-1">
                      <a href={`/paciente/${p.id}`} class="block hover:underline" title="Ver detalhes e histórico do paciente">
                        <h3 class="text-lg font-bold text-gray-800 mb-2">{p.nomePaciente}</h3>
                      </a>
                      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2 text-sm">
                        <div><span class="font-semibold text-gray-600">CPF:</span> {p.cpfPaciente}</div>
                        <div><span class="font-semibold text-gray-600">USF:</span> {p.usfOrigem}</div>
                      </div>
                    </div>
                  </li>
                {/each}
              </ul>

              {#if totalPages > 1}
                <div class="flex justify-center items-center space-x-2 mt-6">
                  <button on:click={prevPage} class="px-3 py-1 bg-emerald-600 hover:bg-emerald-800 text-white rounded disabled:opacity-50 disabled:cursor-not-allowed transition" disabled={currentPage === 1}>&laquo; Anterior</button>
                  <span class="text-gray-700">Página {currentPage} de {totalPages}</span>
                  <button on:click={nextPage} class="px-3 py-1 bg-emerald-600 hover:bg-emerald-800 text-white rounded disabled:opacity-50 disabled:cursor-not-allowed transition" disabled={currentPage === totalPages}>Próximo &raquo;</button>
                </div>
              {/if}
            {/if}
        {/if}
      </div>
    </main>
 </div>
</div>