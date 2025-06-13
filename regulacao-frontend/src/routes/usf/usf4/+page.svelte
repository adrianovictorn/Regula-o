<script>
  // Component logic: receive data and filter/paginate
  export let data;
  const { solicitacoes } = data;

  let buscar = '';
  let currentPage = 1;
  const itemsPerPage = 10;

  // Reactive filtered list based on search term
  $: filtradas = buscar.trim()
    ? solicitacoes.filter(s => {
        const termo = buscar.toLowerCase();
        const nomeMatch = s.nomePaciente.toLowerCase().includes(termo);
        const cpfMatch = s.cpfPaciente.toLowerCase().includes(termo);
        const usfMatch = s.usfOrigem.toLowerCase().includes(termo);
        const espMatch = s.especialidades.some(e => e.especialidadeSolicitada.toLowerCase().includes(termo));
        const prioMatch = s.especialidades.some(e => e.prioridade.toLowerCase().includes(termo));
        return nomeMatch || cpfMatch || usfMatch || espMatch || prioMatch;
      })
    : solicitacoes;

  // Pagination
  $: totalPages = Math.ceil(filtradas.length / itemsPerPage);
  $: paged = filtradas.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

  function prevPage() { if (currentPage > 1) currentPage--; }
  function nextPage() { if (currentPage < totalPages) currentPage++; }
</script>

<div class="flex h-screen bg-gray-100">
  <!-- Sidebar navigation -->
  <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">Regula System</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Nova Solicitação</a>
      <a href="/agendamentos" class="py-2 px-4 rounded hover:bg-emerald-800">Agendamento</a>
      <a href="/paciente" class="py-2 px-4 rounded hover:bg-emerald-800 ">Paciente</a>
      <a href="/" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
  </aside>

  <!-- Main content area -->
  <div class="flex-1 flex flex-col">
    <!-- Header -->
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Pacientes</h1>
      <div>Bem-vindo(a), Usuário</div>
    </header>

    <!-- Content -->
    <main class="flex-1 overflow-auto p-6">
      <div class="bg-white rounded-lg shadow-lg p-6 space-y-6">
        <!-- Title and search -->
        <div class="flex flex-col md:flex-row md:justify-between md:items-center">
          <h2 class="text-2xl font-bold text-emerald-800 mb-4 md:mb-0">Lista de Pacientes Pendentes</h2>
          <div class="flex w-full md:w-1/2">
            <input
              type="text"
              placeholder="Buscar por nome, CPF, especialidade..."
              bind:value={buscar}
              class="flex-1 border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
            />
          </div>
        </div>

        <!-- Total count -->
        <p class="text-gray-600">Total: {filtradas.length}</p>

        <!-- List items -->
        {#if filtradas.length === 0}
          <p class="text-center text-gray-500">Nenhuma solicitação encontrada.</p>
        {:else}
          <ul class="space-y-4">
            {#each paged as s, idx}
              <li class="bg-white border border-gray-200 rounded-lg p-4 hover:shadow transition flex">
                <!-- Item number -->
                <div class="text-emerald-700 font-bold text-xl mr-4">{(currentPage - 1) * itemsPerPage + idx + 1}.</div>
                <div class="flex-1">
                  <a href={`/paciente/${s.id}`} class="block hover:underline">
                    <h3 class="text-lg font-bold mb-2">{s.nomePaciente}</h3>
                  </a>
                  <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2 text-sm">
                    <div><span class="font-semibold">CPF:</span> {s.cpfPaciente}</div>
                    <div><span class="font-semibold">USF:</span> {s.usfOrigem}</div>
                    <div><span class="font-semibold">Data:</span> {s.dataMalote}</div>
                    <!-- Priority with color coding -->
                    <div>
                      <span class="font-semibold">Prioridade:</span>
                      <span
                        class="px-2 py-1 rounded text-white text-xs"
                        class:bg-red-600={s.especialidades[0]?.prioridade === 'EMERGENCIA'}
                        class:bg-yellow-500={s.especialidades[0]?.prioridade === 'URGENTE'}
                        class:bg-green-500={s.especialidades[0]?.prioridade === 'NORMAL'}
                      >{s.especialidades[0]?.prioridade}</span>
                    </div>
                    <div class="col-span-full"><span class="font-semibold">Especialidade:</span> {s.especialidades.map(e => e.especialidadeSolicitada).join(', ')}</div>
                    <div class="col-span-full"><span class="font-semibold">Observações:</span> {s.observacoes}</div>
                  </div>
                </div>
              </li>
            {/each}
          </ul>

          <!-- Pagination controls -->
          {#if totalPages > 1}
            <div class="flex justify-center items-center space-x-2 mt-6">
              <button on:click={prevPage} class="px-3 py-1 bg-emerald-600 text-white rounded disabled:opacity-50" disabled={currentPage === 1}>&laquo; Anterior</button>
              <span class="text-gray-700">Página {currentPage} de {totalPages}</span>
              <button on:click={nextPage} class="px-3 py-1 bg-emerald-600 text-white rounded disabled:opacity-50" disabled={currentPage === totalPages}>Próximo &raquo;</button>
            </div>
          {/if}
        {/if}
      </div>
    </main>
  </div>
</div>