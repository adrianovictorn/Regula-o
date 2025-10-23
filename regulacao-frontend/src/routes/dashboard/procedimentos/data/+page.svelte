<script lang="ts">
  import { onMount } from 'svelte';
  import { getApi } from '$lib/api.js';
  import RoleBasedMenu from '$lib/RoleBasedMenu.svelte';
  import UserMenu from '$lib/UserMenu.svelte';

  // --- Estado do Componente ---
  let isLoading = $state(true);
  let error = $state('');
  
  // A variável final que será renderizada na tela
  let paineisDeProcedimentos = $state([]);

  // --- Definições Visuais (Responsabilidade do Frontend) ---
  const metadadosPaineis = {
    'Laboratório': { href: '/agendas/laboratorio', color: 'bg-sky-500', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M7.5 21L3 16.5m0 0L7.5 12M3 16.5h13.5m0-13.5L21 7.5m0 0L16.5 12M21 7.5H7.5" />` },
    'Raio X': { href: '/agendas/raio-x', color: 'bg-indigo-500', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75l3 3m0 0l3-3m-3 3v-7.5M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />` },
    'Ultrassonografia': { href: '/agendas/ultrasom', color: 'bg-purple-500', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M9 4.5v15m6-15v15m-10.875 0h15.75c.621 0 1.125-.504 1.125-1.125V5.625c0-.621-.504-1.125-1.125-1.125H4.125C3.504 4.5 3 5.004 3 5.625v12.75c0 .621.504 1.125 1.125 1.125z" />` },
    'Doppler': { href: '/agendas/doppler', color: 'bg-teal-500', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />` },
    'Eletrocardiograma': { href: '/agendas/eletrocardiograma', color: 'bg-rose-500', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />` },
    'Pediatra': { href: '/agendas/pediatra', color: 'bg-amber-500', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M15.182 15.182a4.5 4.5 0 01-6.364 0M21 12a9 9 0 11-18 0 9 9 0 0118 0zM9 9.75h.008v.008H9v-.008zm6 0h.008v.008H15v-.008z" />` },
    'Ortopedista': { href: '/agendas/ortopedista', color: 'bg-cyan-600', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M19.5 12c0-1.232-.046-2.453-.138-3.662a4.006 4.006 0 00-3.7-3.7 48.678 48.678 0 00-7.324 0 4.006 4.006 0 00-3.7 3.7c-.092 1.21-.138 2.43-.138 3.662a4.006 4.006 0 003.7 3.7 48.656 48.656 0 007.324 0 4.006 4.006 0 003.7-3.7zM12 12h.008v.008H12V12z" />` },
    'Cardiologista': { href: '/agendas/cardiologista', color: 'bg-rose-600', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z" />` },
    'Ecocardiograma': { href: '/agendas/ecocardiograma', color: 'bg-rose-600', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z" />` },
    'Cirurgião Geral': { href: '/agendas/cirurgiao-geral', color: 'bg-emerald-600', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M19.5 12c0-1.232-.046-2.453-.138-3.662a4.006 4.006 0 00-3.7-3.7 48.678 48.678 0 00-7.324 0 4.006 4.006 0 00-3.7 3.7c-.092 1.21-.138 2.43-.138 3.662a4.006 4.006 0 003.7 3.7 48.656 48.656 0 007.324 0 4.006 4.006 0 003.7-3.7zM12 12h.008v.008H12V12z" />`},
    'Dermatologista': { href: '/agendas/dermatologista', color: 'bg-rose-600', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M19.5 12c0-1.232-.046-2.453-.138-3.662a4.006 4.006 0 00-3.7-3.7 48.678 48.678 0 00-7.324 0 4.006 4.006 0 00-3.7 3.7c-.092 1.21-.138 2.43-.138 3.662a4.006 4.006 0 003.7 3.7 48.656 48.656 0 007.324 0 4.006 4.006 0 003.7-3.7zM12 12h.008v.008H12V12z" />`},
    'Procedimento Dermatologista': { href: '/agendas/procedimento_dermatologista', color: 'bg-rose-700', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M19.5 12c0-1.232-.046-2.453-.138-3.662a4.006 4.006 0 00-3.7-3.7 48.678 48.678 0 00-7.324 0 4.006 4.006 0 00-3.7 3.7c-.092 1.21-.138 2.43-.138 3.662a4.006 4.006 0 003.7 3.7 48.656 48.656 0 007.324 0 4.006 4.006 0 003.7-3.7zM12 12h.008v.008H12V12z" />`},
    'Neuropediatra': { href: '/agendas/neuropediatria', color: 'bg-yellow-400', icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M19.5 12c0-1.232-.046-2.453-.138-3.662a4.006 4.006 0 00-3.7-3.7 48.678 48.678 0 00-7.324 0 4.006 4.006 0 00-3.7 3.7c-.092 1.21-.138 2.43-.138 3.662a4.006 4.006 0 003.7 3.7 48.656 48.656 0 007.324 0 4.006 4.006 0 003.7-3.7zM12 12h.008v.008H12V12z" />` },
  };

  // --- Lógica de Data ---
  function getHojeFormatado() {
    const hoje = new Date();
    return hoje.toISOString().split('T')[0]; // Formato YYYY-MM-DD
  }

  let dataSelecionada = $state(getHojeFormatado());
  let dataFormatada = $state('');

  // --- Lógica de Dados ---
  async function buscarEProcessarContagem(data: string) {
    isLoading = true;
    error = '';
    try {
      // 1. Busca os dados crus do backend
      const response = await getApi(`agendamentos/contagem-por-data?data=${data}`);
      if (!response.ok) {
        throw new Error(`Falha na API: ${response.statusText}`);
      }
      const contagens = await response.json();

      // 2. Formata a data selecionada para exibição
      const [ano, mes, dia] = data.split('-');
      const dataObj = new Date(Number(ano), Number(mes) - 1, Number(dia));
      dataFormatada = dataObj.toLocaleDateString('pt-BR', { timeZone: 'UTC', weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });

      // 3. Combina os dados da API com os dados visuais do frontend
      paineisDeProcedimentos = contagens.map(item => ({
        ...item, // vem da API: { label, count }
        ...(metadadosPaineis[item.label] || {}) // adiciona do frontend: { href, color, icon }
      }));

    } catch (e) {
      error = e.message;
    } finally {
      isLoading = false;
    }
  }

  // Efeito reativo do Svelte 5: roda sempre que `dataSelecionada` mudar.
  $effect(() => {
    buscarEProcessarContagem(dataSelecionada);
  });
</script>

<svelte:head>
    <title>Painel de Procedimentos</title>
</svelte:head>

<div class="flex min-h-screen bg-gray-50">
  <RoleBasedMenu activePage="/dashboard/procedimentos" />

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Painel de Procedimentos</h1>
      <UserMenu />
    </header>

    <main class="flex-1 p-6 overflow-auto">
      {#if isLoading}
        <div class="text-center py-10"><p>Carregando...</p></div>
      {:else if error}
        <div class="text-center py-10 text-red-600"><p>Erro ao carregar: {error}</p></div>
      {:else}
        <div class="max-w-7xl mx-auto">
          <div class="mb-8 p-4 border-l-4 border-emerald-500 bg-emerald-50 rounded-r-lg">
            <h2 class="text-2xl font-bold text-gray-800">Agendamentos do Dia</h2>
            <p class="text-gray-600">{dataFormatada}</p>

             <div class="mt-4">
                <label for="data-busca" class="text-gray-700 font-semibold">Buscar por outra data:</label>
                <input 
                    type="date" 
                    id="data-busca" 
                    bind:value={dataSelecionada}
                    class="mt-1 block w-full md:w-auto border-gray-300 rounded-md shadow-sm focus:ring-emerald-500 focus:border-emerald-500"
                />
            </div>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {#each paineisDeProcedimentos as procedimento}
      <a href={procedimento.href} class="block group">                
           <div class="bg-white rounded-xl shadow-md p-6 transform transition-all duration-300 hover:shadow-xl hover:-translate-y-1">
                  <div class="flex items-start justify-between">
                    <div class="flex-shrink-0 w-12 h-12 {procedimento.color} text-white rounded-lg flex items-center justify-center">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                        {@html procedimento.icon}
                      </svg>
                    </div>
                    <div class="text-right">
                      <p class="text-5xl font-bold text-gray-800">{procedimento.count}</p>
                      <p class="text-sm text-gray-500">agendados</p>
                    </div>
                  </div>
                  <h3 class="mt-4 text-lg font-semibold text-gray-900 group-hover:text-emerald-600 transition-colors">
                    {procedimento.label}
                  </h3>
                </div>
              </a>
            {/each}
          </div>
        </div>
      {/if}
    </main>
  </div>
</div>
