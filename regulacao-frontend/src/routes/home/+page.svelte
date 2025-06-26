<script>
  import { onMount } from 'svelte';
  import { goto } from '$app/navigation';
  import { user, token } from '$lib/stores/auth.js';
  import Card from '$lib/Card.svelte';
  import Card2 from '$lib/Card2.svelte';
  import { getApi } from '$lib/api.js'; // Importa nosso helper que já envia o token!
  import Card3 from '$lib/Card3.svelte';
  import Card4 from '$lib/Card4.svelte';

  // Variáveis de estado para controlar a UI
  let solicitacoes = [];
  let isLoading = true; // Começa como 'true' para mostrar a mensagem de carregando
  let error = '';

  // Função para fazer logout
  function logout() {
    token.set(null); 
    goto('/login');  
  }

  // Esta função será executada apenas no navegador, após o componente ser montado
  onMount(async () => {
    try {
      // Usa nosso helper 'getApi' que automaticamente anexa o token JWT
      const response = await getApi('solicitacoes');

      if (!response.ok) {
        // Se o token for inválido ou o servidor der outro erro, captura a mensagem.
        const errorData = await response.text();
        throw new Error(`Falha ao carregar os dados: ${response.status} ${errorData}`);
      }
      
      solicitacoes = await response.json();
    } catch (e) {
      error = e.message;
    } finally {
      // Ao final, independentemente de sucesso ou erro, para de carregar.
      isLoading = false;
    }
  });


  $: pendentes = solicitacoes.filter(s => s.especialidades.some(esp => esp.status === 'AGUARDANDO')).length;
  $: agendado = solicitacoes.filter(s => s.especialidades.some(esp => esp.status === 'AGENDADO')).length;
  $: concluida = solicitacoes.filter(solicitacao => solicitacao.especialidades.some(esp => esp.status === "CONCLUIDO")).length;
  $: totalDeSolicitacoes = solicitacoes.length;
  $: emergencia = solicitacoes.filter(s => s.especialidades.some(esp => esp.prioridade === 'EMERGENCIA')).length;
  $: urgencia = solicitacoes.filter(s => s.especialidades.some(esp => esp.prioridade === 'URGENTE' || esp.prioridade === 'EMERGENCIA '&& esp.status === 'AGUARDANDO')).length;
  const filtarPendentesPorUnidade = (unidade) => {
    if (!solicitacoes || solicitacoes.length === 0) return 0;
    return solicitacoes.filter(solicitacao => solicitacao.especialidades.some(esp => esp.status === 'AGUARDANDO') && solicitacao.usfOrigem === unidade).length;
  };
</script>

<!-- O HTML agora é condicional com base no estado de carregamento -->
{#if isLoading}
  <div class="flex items-center justify-center h-screen">
    <p class="text-xl text-gray-600">Carregando painel de controle...</p>
  </div>
{:else if error}
  <div class="flex items-center justify-center h-screen">
    <p class="text-xl text-red-500">Erro ao carregar os dados: {error}</p>
  </div>
{:else}
  <!-- O seu layout original é renderizado aqui somente após os dados serem carregados -->
  <div class="flex h-screen bg-gray-100">
    <!-- Sidebar -->
    <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
      <h2 class="text-2xl font-bold text-center mb-8">SIRG</h2>
      <nav class="flex-1 flex flex-col space-y-2 px-6">
        <a href="/home" class="py-2 px-4 rounded bg-emerald-700 transition">Dashboard</a>
        <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Cadastro de Consulta</a>
        <a href="/exames" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exame/Procedimento</a>
        <a href="/agendar" class="py-2 px-4 rounded hover:bg-emerald-800">Agendamento</a>
        <a href="/paciente" class="py-2 px-4 rounded hover:bg-emerald-800">Paciente</a>
        <a href="/exportar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exportar Dados</a>
        <a href="/admin/cadastrar-usuario" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Cadastrar Usuário</a>

      </nav>
      <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
    </aside>

    <!-- Main Content -->
    <div class="flex-1 flex flex-col">
      <!-- Header com boas-vindas e botão de logout -->
      <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
        <h1 class="text-xl font-semibold">Painel de Controle</h1>
        {#if $user}
          <div class="flex items-center space-x-4">
            <span>Bem-vindo(a), {$user.nome}</span>
            <button 
              on:click={logout} 
              class="bg-red-600 hover:bg-red-700 text-white font-bold py-1 px-3 rounded-lg text-sm transition-colors duration-200"
            >
              Sair
            </button>
          </div>
        {:else}
          <div>
            <a href="/login" class="hover:underline">Fazer Login</a>
          </div>
        {/if}
      </header>

      <!-- Dashboard Cards -->
      <main class="flex-1 p-6 overflow-auto">
        <div class="space-y-6">
          <!-- Sede Section -->
          <section class="bg-emerald-300 rounded-lg shadow p-6">
            <h2 class="text-lg font-bold text-emerald-800 mb-4">📍 Sede</h2>
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
              <Card2 header="USF 01" title="Pendentes" value={filtarPendentesPorUnidade('USF01')} href="/usf/usf1" color="emerald"/>
              <Card2 header="USF 02" title="Pendentes" value={filtarPendentesPorUnidade('USF02')} href="/usf/usf2" color="emerald"/>
            </div>
          </section>

          <!-- Zona Rural Section -->
          <section class="bg-emerald-200 rounded-lg shadow p-6">
            <h2 class="text-lg font-bold text-emerald-600 mb-4">🌳 Zona Rural</h2>
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
              <Card2 header="USF 03" title="Pendentes" value={filtarPendentesPorUnidade('USF03')} href="/usf/usf3" color="emerald"/>
              <Card2 header="USF 04" title="Pendentes" value={filtarPendentesPorUnidade('USF04')} href="/usf/usf4" color="emerald"/>
              <Card2 header="USF 05" title="Pendentes" value={filtarPendentesPorUnidade('USF05')} href="/usf/usf5" color="emerald"/>
              <Card2 header="USF 06" title="Pendentes" value={filtarPendentesPorUnidade('USF06')} href="/usf/usf6" color="emerald"/>
            </div>
          </section>

         

          <!-- Totals Section -->
          <section class="bg-emerald-300 rounded-lg shadow p-6">
            <h2 class="text-lg font-bold text-emerald-900 mb-4">📊 Totais</h2>
            <div class="grid grid-cols-1 sm:grid-cols-4 gap-6">
              <Card2 title="Pendentes" value={pendentes} href="/usf" color="emerald-dark"/>
              <Card title="Agendados" value={agendado} href="/usf" color="emerald-dark"/>
              <Card title="Concluídas" value={concluida} color="emerald-dark"/>
              <Card title="Total" value={totalDeSolicitacoes} color="emerald-dark"/>
            </div>
          </section>

           <section class="bg-emerald-200 rounded-lg shadow p-6">
            <h2 class="text-lg font-bold text-red-900 mb-4">📊 Alertas</h2>
            <div class="grid grid-cols-1 sm:grid-cols-1 gap-6">
              <Card3 title="Urgência" value={urgencia} href="/paciente/urgentes" color="emerald-dark"/>

            </div>
          </section>

          
        </div>
      </main>
    </div>
  </div>
{/if}