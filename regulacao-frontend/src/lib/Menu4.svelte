<script>
  import { onMount } from 'svelte';

  
  export let activePage = ''; 

  
  let isTransporteOpen = false;
  let isPainelDeGestao = false

  function toggleTransporte() {
    isTransporteOpen = !isTransporteOpen;
  }

   function toggleRelatorio(){
    isPainelDeGestao = !isPainelDeGestao;
  }

  
  onMount(() => {
    if (activePage === '/cadastrar' || activePage === '/exames') {
      isTransporteOpen = true;
    }
    if(activePage ==='/cadastrar/cid' || activePage === '/listar/cid'){
      isPainelDeGestao = true;
    }
  });
</script>

<aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg min-h-screen">
  <h2 class="text-2xl font-bold text-center mb-8">SIRG</h2>
  <nav class="flex-1 flex flex-col space-y-2 px-6">

    <div>
      <button on:click={toggleTransporte} class="w-full text-left py-2 px-4 rounded hover:bg-emerald-800 transition flex justify-between items-center">
        <span>Transporte</span>
        <span class="transform transition-transform duration-200" class:rotate-180={isTransporteOpen}>▼</span>
      </button>
      {#if isTransporteOpen}
        <div class="pl-4 mt-2 space-y-2">
          <a href="/cadastrar/transporte" class="block py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/cadastrar/transporte'} class:hover:bg-emerald-800={activePage !== '/cadastrar/transporte'}>Cadastrar Transporte</a>
          <a href="/cadastrar/cidade" class="block py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/cadastrar/cidade'} class:hover:bg-emerald-800={activePage !== '/cadastrar/cidade'}>Cadastrar Cidade</a>
        </div>
        {/if}
      </div>   
      <a href="/cadastrar/cidade/local-agendamento" class="block py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/cadastrar/cidade/local-agendamento'} class:hover:bg-emerald-800={activePage !== '/cadastrar/cidade/local-agendamento'}>Ponto de Parada</a>
      <a href="/agendar/transporte" class="block py-2 px-4 rounded transition"> Agendar Transporte</a>
      <a href="/consultar/transporte" class="block py-2 px-4 rounded transition"> Consultar Transporte</a>

      <div>
        <button on:click={toggleRelatorio} class="w-full text-left py-2 px-4 rounded hover:bg-emerald-800 transition flex justify-between items-center">
          <span>Painel Gerencional</span>
          <span class="transform transition-transform duration-200" class:rotate-180={isTransporteOpen}>▼</span>
        </button>
      {#if isPainelDeGestao}
        <div class="pl-4 mt-2 space-y-2">
            <a href="/cadastrar/cid" class=" block py-2 px-4 rounded hover:bg-emerald-800 transition" class:bg-emerald-700={activePage === '/cadastrar/cid'}>Cadastrar CID</a>
            <a href="/listar/cid" class="block py-2 px-4 rounded hover:bg-emerald-800 transition" class:bg-emerald-700={activePage === '/listar/cid'}>Listar CID</a>
          </div>
      {/if}
      </div>
   
  <div class="px-6 mt-auto text-sm text-emerald-200 ">v1.2 • Adriano Victor, Filipe Ribeiro © 2025</div>
</aside>