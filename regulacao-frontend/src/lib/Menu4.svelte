<script>
  import { onMount } from 'svelte';

  export let activePage = '';

  let isTransporteOpen = false;
  let isPainelDeGestao = false;
  let isCarro = false;
  let lastActivePage = '';

  const transporteAgendamentoRoutes = [
    '/agendar/transporte',
    '/consultar/transporte'
  ]

  const transporteRoutes = [
    '/cadastrar/transporte',
    '/cadastrar/cidade',
    '/cadastrar/motorista',
    '/cadastrar/cidade/local-agendamento',
    '/cadastrar/paciente'
  ];

  const painelGestaoRoutes = [
    '/cadastrar/cid',
    '/listar/cid',
    '/admin/cadastrar-usuario',
    '/admin/'
  ];

  function toggleTransporte() {
    isTransporteOpen = !isTransporteOpen;
  }

  function toggleAgendamentoTransporte(){
    isCarro = !isCarro;
  }

  function togglePainelDeGestao() {
    isPainelDeGestao = !isPainelDeGestao;
  }

  function isRouteActive(page, routes) {
    return routes.some((route) =>
      page === route || (route.endsWith('/') ? page.startsWith(route) : page.startsWith(`${route}/`))
    );
  }

  function ensureOpenFor(page) {
    if (isRouteActive(page, transporteRoutes)) {
      isTransporteOpen = true;
    }
    if (isRouteActive(page, painelGestaoRoutes)) {
      isPainelDeGestao = true;
    }

    if(isRouteActive(page, transporteAgendamentoRoutes)){
      isCarro = true;
    }
  }

  onMount(() => {
    ensureOpenFor(activePage);
    lastActivePage = activePage;
  });

  $: if (activePage !== lastActivePage) {
    ensureOpenFor(activePage);
    lastActivePage = activePage;
  }
</script>

<aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg min-h-screen">
  <h2 class="text-2xl font-bold text-center mb-8">SIRG</h2>
  <nav class="flex-1 flex flex-col space-y-2 px-6">
    <div>
      <button on:click={toggleAgendamentoTransporte} class="w-full text-left py-2 px-4 rounded hover:bg-emerald-800 transition flex justify-between items-center">
        <span>Gestão de Transporte</span>
        <span class="transform transition-transform duration-200" class:rotate-180={isCarro}>▾</span>
      </button>

      {#if  isCarro}      
        <div class="pl-4 mt-2 space-y-2">
          <a href="/agendar/transporte" class="block py-2 px-4 rounded transition hover:bg-emerald-800" class:bg-emerald-700={activePage === '/agendar/transporte'}> Agendar Transporte</a>
          <a href="/consultar/transporte" class="block py-2 px-4 rounded transition hover:bg-emerald-800 " class:bg-emerald-700={activePage === '/consultar/transporte'}> Consultar Transporte</a>
        </div>
      {/if}
    </div>
    
    <div>
      <button on:click={toggleTransporte} class="w-full text-left py-2 px-4 rounded hover:bg-emerald-800 transition flex justify-between items-center">
        <span>Painel Gerencial</span>
        <span class="transform transition-transform duration-200" class:rotate-180={isTransporteOpen}>▾</span>
      </button>
      {#if isTransporteOpen}
        <div class="pl-4 mt-2 space-y-2">
          <a href="/cadastrar/transporte" class="block py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/cadastrar/transporte'} class:hover:bg-emerald-800={activePage !== '/cadastrar/transporte'}>Cadastrar Transporte</a>
          <a href="/cadastrar/cidade" class="block py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/cadastrar/cidade'} class:hover:bg-emerald-800={activePage !== '/cadastrar/cidade'}>Cadastrar Cidade</a>
          <a href="/cadastrar/motorista" class="block py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/cadastrar/motorista'} class:hover:bg-emerald-800={activePage !== '/cadastrar/motorista'}> Cadastrar Motorista</a>
          <a href="/cadastrar/paciente" class="block py-2 px-4 rounded transition" class:bg-emerald-700={activePage ==='/cadastrar/paciente'} class:hover:bg-emerald-800={activePage ==='/cadastrar/paciente'}>Cadastrar Paciente</a>
          <a href="/cadastrar/cidade/local-agendamento" class="block py-2 px-4 rounded transition" class:bg-emerald-700={activePage === '/cadastrar/cidade/local-agendamento'} class:hover:bg-emerald-800={activePage !== '/cadastrar/cidade/local-agendamento'}> Ponto de Parada</a>
        </div>
      {/if}
    </div>


    <div>
      <button on:click={togglePainelDeGestao} class="w-full text-left py-2 px-4 rounded hover:bg-emerald-800 transition flex justify-between items-center">
        <span>Painel Administrador</span>
        <span class="transform transition-transform duration-200" class:rotate-180={isPainelDeGestao}>▾</span>
      </button>
      {#if isPainelDeGestao}
        <div class="pl-4 mt-2 space-y-2">
            <a href="/admin/cadastrar-usuario" class="block py-2 px-4 rounded hover:bg-emerald-800 transition" class:bg-emerald-700={activePage === '/admin/cadastrar-usuario'}>Cadastrar Usuário</a>
        </div>
      {/if}
    </div>
  </nav>

  <div class="px-6 mt-auto text-sm text-emerald-200 ">v1.2 - Adriano Victor, Filipe Ribeiro - 2025</div>
</aside>
