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
    <a href="/home" class="py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/home'} class:hover:bg-emerald-800={activePage !== '/home'}>Dashboard</a>
    <a href="/agendar" class="py-2 px-4 rounded hover:bg-emerald-800" class:bg-emerald-700={activePage === '/agendar'}>Agendamento</a>
    <a href="/exportar" class="py-2 px-4 rounded hover:bg-emerald-800 transition" class:bg-emerald-700={activePage === '/exportar'}>Relatórios</a>   
  </nav>
  <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
</aside>