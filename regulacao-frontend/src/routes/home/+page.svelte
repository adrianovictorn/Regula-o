<script>
  import Card from '$lib/Card.svelte';
  import Card2 from '$lib/Card2.svelte';

  export let data;
  const { solicitacoes } = data;

  const pendentes = solicitacoes.filter(s => s.especialidades.some(esp => esp.status === 'AGUARDANDO')).length;
  const filtarPendentesPorUnidade = (unidade) => solicitacoes.filter(solicitacao => solicitacao.especialidades.some(esp => esp.status === 'AGUARDANDO') && solicitacao.usfOrigem === unidade).length;

  const concluida = solicitacoes.filter(solicitacao => solicitacao.especialidades.some(esp => esp.status === "REALIZADO")).length;
  const totalDeSolicitacoes = solicitacoes.length;

  function prioridadeDominante(unidade) {
    const especialidades = solicitacoes
      .filter(s => s.usfOrigem === unidade)
      .flatMap(s => s.especialidades);

    if (especialidades.some(e => e.prioridade === 'EMERGENCIA')) return 'EMERGENCIA';
    if (especialidades.some(e => e.prioridade === 'URGENCIA')) return 'URGENCIA';
    return 'NORMAL';
  }
</script>

<div class="flex h-screen bg-gray-100">
  <!-- Sidebar -->
  <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">Regula System</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded bg-emerald-700 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Nova Solicitação</a>
      <a href="/exames" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Laboratório</a>
      <a href="/agendar" class="py-2 px-4 rounded hover:bg-emerald-800">Agendamento</a>
      <a href="/paciente" class="py-2 px-4 rounded hover:bg-emerald-800">Paciente</a>
      <a href="/exportar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
  </aside>

  <!-- Main Content -->
  <div class="flex-1 flex flex-col">
    <!-- Header -->
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Painel de Controle</h1>
      <div>Bem-vindo(a), Usuário</div>
    </header>

    <!-- Dashboard Cards -->
    <main class="flex-1 p-6 overflow-auto">
      <div class="space-y-6">
        <!-- Sede Section -->
        <section class="bg-emerald-100 rounded-lg shadow p-6">
          <h2 class="text-lg font-bold text-emerald-800 mb-4">📍 Sede</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
            <Card2 header="USF 01" title="Pendentes" value={filtarPendentesPorUnidade('USF01')} href="/usf/usf1" color="emerald"/>
            <Card2 header="USF 02" title="Pendentes" value={filtarPendentesPorUnidade('USF02')} href="/usf/usf2" color="emerald"/>
          </div>
        </section>

        <!-- Zona Rural Section -->
        <section class="bg-emerald-50 rounded-lg shadow p-6">
          <h2 class="text-lg font-bold text-emerald-600 mb-4">🌳 Zona Rural</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            <Card2 header="USF 03" title="Pendentes" value={filtarPendentesPorUnidade('USF03')} href="/usf/usf3" color="emerald"/>
            <Card2 header="USF 04" title="Pendentes" value={filtarPendentesPorUnidade('USF04')} href="/usf/usf4" color="emerald"/>
            <Card2 header="USF 05" title="Pendentes" value={filtarPendentesPorUnidade('USF05')} href="/usf/usf5" color="emerald"/>
            <Card2 header="USF 06" title="Pendentes" value={filtarPendentesPorUnidade('USF06')} href="/usf/usf6" color="emerald"/>
          </div>
        </section>

        <!-- Totals Section -->
        <section class="bg-emerald-200 rounded-lg shadow p-6">
          <h2 class="text-lg font-bold text-emerald-900 mb-4">📊 Totais</h2>
          <div class="grid grid-cols-1 sm:grid-cols-3 gap-6">
            <Card2 title="Pendentes" value={pendentes} href="/usf" color="emerald-dark"/>
            <Card title="Concluídas" value={concluida} color="emerald-dark"/>
            <Card title="Total" value={totalDeSolicitacoes} color="emerald-dark"/>
          </div>
        </section>
      </div>
    </main>
  </div>
</div>
