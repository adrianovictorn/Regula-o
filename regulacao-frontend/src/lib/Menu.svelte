<script>
  import { onMount } from 'svelte';

  // Props para o componente
  export let activePage = ''; // Recebe a URL da página ativa

  // Controle para o submenu de Solicitação
  let isSolicitacaoOpen = false;

  function toggleSolicitacao() {
    isSolicitacaoOpen = !isSolicitacaoOpen;
  }

  // Lógica para manter o submenu aberto se a página ativa for uma de suas filhas
  onMount(() => {
    if (activePage === '/cadastrar' || activePage === '/exames') {
      isSolicitacaoOpen = true;
    }
  });
</script>

<aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
  <h2 class="text-2xl font-bold text-center mb-8">SIRG</h2>
  <nav class="flex-1 flex flex-col space-y-2 px-6">
    <a href="/dashboard" class="py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/dashboard'} class:hover:bg-emerald-800={activePage !== '/dashboard'}>Dashboard</a>

    <div>
      <button on:click={toggleSolicitacao} class="w-full text-left py-2 px-4 rounded hover:bg-emerald-800 transition flex justify-between items-center">
        <span>Solicitação</span>
        <span class="transform transition-transform duration-200" class:rotate-180={isSolicitacaoOpen}>▼</span>
      </button>
      {#if isSolicitacaoOpen}
        <div class="pl-4 mt-2 space-y-2">
          <a href="/cadastrar" class="block py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/cadastrar'} class:hover:bg-emerald-800={activePage !== '/cadastrar'}>Cadastro de Consulta</a>
          <a href="/exames" class="block py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/exames'} class:hover:bg-emerald-800={activePage !== '/exames'}>Exame/Procedimento</a>
        </div>
      {/if}
    </div>

    <a href="/agendar" class="py-2 px-4 rounded hover:bg-emerald-800" class:bg-emerald-700={activePage === '/agendar'}>Agendamento</a>
    <a href="/dashboard/procedimentos" class="py-2 px-4 rounded hover:bg-emerald-800" class:bg-emerald-700={activePage === '/dashboard/procedimentos'}>Agenda do Dia</a>
    <a href="/paciente" class="py-2 px-4 rounded hover:bg-emerald-800" class:bg-emerald-700={activePage === '/paciente'}>Paciente</a>
    <a href="/exportar" class="py-2 px-4 rounded hover:bg-emerald-800 transition" class:bg-emerald-700={activePage === '/exportar'}>Relatórios</a>
    <a href="/admin/cadastrar-usuario" class="py-2 px-4 rounded hover:bg-emerald-800 transition" class:bg-emerald-700={activePage === '/admin/cadastrar-usuario'}>Cadastrar Usuário</a>
  </nav>
  <div class="px-6 mt-4 text-sm text-emerald-200">v1.1 • Adriano Victor, Filipe Ribeiro © 2025</div>
</aside>