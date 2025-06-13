<script lang="ts">
  // Component logic: receive data and filter/paginate
  export let data;

  // A estrutura de 'data' deve ser a mesma da página original, contendo 'solicitacoes'.
  // Para esta nova página, vamos processar essas solicitações para obter pacientes únicos.
  const { solicitacoes } = data;

  // Mapa para armazenar pacientes únicos (usando CPF como chave para unicidade)
  let pacientesUnicos = new Map<string, { nomePaciente: string; cpfPaciente: string; usfOrigem: string; id: number; }>();

  // Processa as solicitações para extrair pacientes únicos
  solicitacoes.forEach(s => {
    if (!pacientesUnicos.has(s.cpfPaciente)) {
      pacientesUnicos.set(s.cpfPaciente, {
        nomePaciente: s.nomePaciente,
        cpfPaciente: s.cpfPaciente,
        usfOrigem: s.usfOrigem,
        id: s.id // Mantemos um ID, pode ser o da primeira solicitação ou um ID do paciente se o backend tiver
      });
    }
  });

  // Converte o mapa de volta para um array para filtragem e paginação
  let listaPacientes = Array.from(pacientesUnicos.values());

  let buscar = '';
  let currentPage = 1;
  const itemsPerPage = 10;

  // Reactive filtered list based on search term
  $: filtrados = buscar.trim()
    ? listaPacientes.filter(p => {
        const termo = buscar.toLowerCase();
        const nomeMatch = p.nomePaciente.toLowerCase().includes(termo);
        const cpfMatch = p.cpfPaciente.toLowerCase().includes(termo);
        const usfMatch = p.usfOrigem.toLowerCase().includes(termo);
        return nomeMatch || cpfMatch || usfMatch;
      })
    : listaPacientes;

  // Pagination
  $: totalPages = Math.ceil(filtrados.length / itemsPerPage);
  $: paged = filtrados.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

  function prevPage() { if (currentPage > 1) currentPage--; }
  function nextPage() { if (currentPage < totalPages) currentPage++; }
</script>

<div class="flex h-screen bg-gray-100">
 <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">Regula System</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Nova Solicitação</a>
      <a href="/exames" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Laboratório</a>
      <a href="/agendar" class="py-2 px-4 rounded  hover:bg-emerald-800">Agendamento</a>
      <a href="/paciente" class="py-2 px-4 rounded bg-emerald-700">Paciente</a>
      <a href="/exportar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
  </aside>

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Pacientes</h1>
      <div>Bem-vindo(a), Usuário</div>
    </header>

    <main class="flex-1 overflow-auto p-6">
      <div class="bg-white rounded-lg shadow-lg p-6 space-y-6">
        <div class="flex flex-col md:flex-row md:justify-between md:items-center">
          <h2 class="text-2xl font-bold text-emerald-800 mb-4 md:mb-0">Lista de Pacientes</h2>
          <div class="flex w-full md:w-1/2">
            <input
              type="text"
              placeholder="Buscar por nome, CPF, USF..."
              bind:value={buscar}
              class="flex-1 border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
            />
          </div>
        </div>

        <p class="text-gray-600">Total: {filtrados.length}</p>

        {#if filtrados.length === 0}
          <p class="text-center text-gray-500">Nenhum paciente encontrado.</p>
        {:else}
          <ul class="space-y-4">
            {#each paged as p, idx}
              <li class="bg-white border border-gray-200 rounded-lg p-4 hover:shadow transition flex">
                <div class="text-emerald-700 font-bold text-xl mr-4">{(currentPage - 1) * itemsPerPage + idx + 1}.</div>
                <div class="flex-1">
                  <a href={`/paciente/${p.id}`} class="block hover:underline">
                    <h3 class="text-lg font-bold mb-2">{p.nomePaciente}</h3>
                  </a>
                  <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2 text-sm">
                    <div><span class="font-semibold">CPF:</span> {p.cpfPaciente}</div>
                    <div><span class="font-semibold">USF:</span> {p.usfOrigem}</div>
                  </div>
                  </div>
              </li>
            {/each}
          </ul>

          {#if totalPages > 1}
            <div class="flex justify-center items-center space-x-2 mt-6">
              <button on:click={prevPage} class="px-3 py-1 bg-emerald-600 hover:bg-emerald-800 text-white rounded disabled:opacity-50 cursor-pointer" disabled={currentPage === 1}>&laquo; Anterior</button>
              <span class="text-gray-700">Página {currentPage} de {totalPages}</span>
              <button on:click={nextPage} class="px-3 py-1 bg-emerald-600 text-white rounded disabled:opacity-50 cursor-pointer hover:bg-emerald-800" disabled={currentPage === totalPages}>Próximo &raquo;</button>
            </div>
          {/if}
        {/if}
      </div>
    </main>
  </div>
</div>