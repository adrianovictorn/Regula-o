<script lang="ts">
  // A lógica permanece a mesma
  let dataSelecionada = new Date().toISOString().split('T')[0];
  let loadingType: string | null = null;

  const relatoriosDiarios = [
    { value: 'LABORATORIO', label: 'Laboratório', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M7.5 21L3 16.5m0 0L7.5 12M3 16.5h13.5m0-13.5L21 7.5m0 0L16.5 12M21 7.5H7.5" />` },
    { value: 'RAIO X', label: 'Raio X', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75l3 3m0 0l3-3m-3 3v-7.5M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />` },
    { value: 'ELETROCARDIOGRAMA', label: 'Eletrocardiograma', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M15.362 5.214A8.252 8.252 0 0112 21 8.25 8.25 0 016.038 7.048l-4.252 4.252a.75.75 0 01-1.06-1.06l4.252-4.252a.75.75 0 011.06 0l4.252 4.252a.75.75 0 01-1.06 1.06l-4.252-4.252" />` },
    { value: 'USG', label: 'Ultrassonografia', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M9 4.5v15m6-15v15m-10.875 0h15.75c.621 0 1.125-.504 1.125-1.125V5.625c0-.621-.504-1.125-1.125-1.125H4.125C3.504 4.5 3 5.004 3 5.625v12.75c0 .621.504 1.125 1.125 1.125z" />` },
  ];

  const relatoriosAgendados = [
    { value: 'DOPPLER', label: 'Doppler', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />` },
    { value: 'CARDIOLOGIA', label: 'Cardiologia', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z" />` },
    { value: 'ECOCARDIOGRAMA', label: 'Ecocardiograma', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M15.91 11.672a.375.375 0 010 .656l-5.603 3.113a.375.375 0 01-.557-.328V8.887c0-.286.357-.466.557-.327l5.603 3.112z" />` },
    { value: 'PEDIATRIA', label: 'Pediatria', icon: `<path d="M12.75 3.09v2.64M15.34 4.14l-2.03 2.03M12.75 8.3v2.64M10.16 7.14l2.03-2.03M4.75 4.14l2.03 2.03M7.25 8.3v2.64M4.75 12.86l2.03-2.03M7.25 17.01v-2.64M12.75 12.86l-2.03 2.03M10.16 17.01v-2.64" />` },
    { value: 'ORTOPEDIA', label: 'Ortopedia', icon: `<path d="M12.75 3.09v2.64M15.34 4.14l-2.03 2.03M12.75 8.3v2.64M10.16 7.14l2.03-2.03M4.75 4.14l2.03 2.03M7.25 8.3v2.64M4.75 12.86l2.03-2.03M7.25 17.01v-2.64M12.75 12.86l-2.03 2.03M10.16 17.01v-2.64" />` },
  ];

  // ... (o resto do script continua igual)

  // Função para gerar a planilha, agora com verificação
  async function gerarPlanilha(tipoPlanilha: string) {
    if (!dataSelecionada) {
      alert('Por favor, selecione uma data.');
      return;
    }

    loadingType = tipoPlanilha; // Ativa o estado de "loading"

    try {
      // 1. URL para verificação
      const checkUrl = `/api/exportar/verificar-dados?tipo=${encodeURIComponent(tipoPlanilha)}&data=${dataSelecionada}`;
      
      const response = await fetch(checkUrl);
      if (!response.ok) {
        throw new Error('Falha ao verificar dados com o servidor.');
      }

      const result = await response.json();

      // 2. Se houver dados, inicia o download
      if (result.dadosDisponiveis) {
        const downloadUrl = `/api/exportar/planilha?tipo=${encodeURIComponent(tipoPlanilha)}&data=${dataSelecionada}`;
        window.location.href = downloadUrl;
      } else {
        // 3. Se não houver dados, exibe um alerta
        alert(`Nenhum dado encontrado para o relatório de ${tipoPlanilha} na data ${new Date(dataSelecionada + 'T12:00:00').toLocaleDateString('pt-BR')}.`);
      }
    } catch (error) {
      console.error("Erro ao gerar planilha:", error);
      alert("Ocorreu um erro de comunicação com o servidor. Tente novamente.");
    } finally {
      // Reseta o estado de loading após um tempo, independentemente do resultado
      setTimeout(() => {
        loadingType = null;
      }, 1000); 
    }
  }

</script>

<div class="flex h-screen bg-gray-100">
  <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">Regula System</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Nova Solicitação</a>
      <a href="/exames" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Laboratório</a>
      <a href="/agendamentos" class="py-2 px-4 rounded hover:bg-emerald-800">Agendamento</a>
      <a href="/paciente" class="py-2 px-4 rounded hover:bg-emerald-800">Paciente</a>
      <a href="/exportar" class="py-2 px-4 rounded bg-emerald-700 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
  </aside>

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Gerar Planilhas de Encaminhamento</h1>
      <div>Bem-vindo(a), Usuário</div>
    </header>

    <main class="flex-1 p-6 overflow-auto">
      <div class="max-w-5xl mx-auto space-y-8">
        <div class="bg-white p-4 rounded-lg shadow-md">
            <label for="data" class="block text-base font-semibold text-gray-800 mb-2">
              🗓️ Selecione a data para os relatórios:
            </label>
            <input
              type="date"
              id="data"
              bind:value={dataSelecionada}
              class="w-full md:w-auto border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
            />
        </div>
        
        <section>
          <h2 class="text-xl font-bold text-gray-700 mb-3">Relatórios Diários</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-5">
            {#each relatoriosDiarios as tipo}
              <button on:click={() => gerarPlanilha(tipo.value)} disabled={loadingType === tipo.value} class="p-4 bg-emerald-600 text-white rounded-lg shadow-lg hover:bg-emerald-700 hover:scale-105 transform transition-all duration-200 flex flex-col items-center justify-center h-32 disabled:bg-gray-400 disabled:cursor-wait disabled:scale-100">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">{@html tipo.icon}</svg>
                <span class="text-lg font-bold text-center">{tipo.label}</span>
                {#if loadingType === tipo.value}<span class="text-xs mt-1 animate-pulse">Gerando...</span>{/if}
              </button>
            {/each}
          </div>
        </section>

        <section>
          <h2 class="text-xl font-bold text-gray-700 mb-3">Relatórios por Agendamento</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-5 gap-5">
            {#each relatoriosAgendados as tipo}
                <button on:click={() => gerarPlanilha(tipo.value)} disabled={loadingType === tipo.value} class="p-4 bg-teal-600 text-white rounded-lg shadow-lg hover:bg-teal-700 hover:scale-105 transform transition-all duration-200 flex flex-col items-center justify-center h-32 disabled:bg-gray-400 disabled:cursor-wait disabled:scale-100">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">{@html tipo.icon}</svg>
                    <span class="text-lg font-bold text-center">{tipo.label}</span>
                    {#if loadingType === tipo.value}<span class="text-xs mt-1 animate-pulse">Gerando...</span>{/if}
                </button>
            {/each}
          </div>
        </section>
      </div>
    </main>
  </div>
</div>