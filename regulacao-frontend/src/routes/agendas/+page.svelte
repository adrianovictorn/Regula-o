<script>
    import { getApi } from "$lib/api";
    import Menu2 from "$lib/Menu2.svelte";
    import UserMenu from "$lib/UserMenu.svelte";
    import { error } from "@sveltejs/kit";


async function carregarSolicitacoes() {
        const res = await getApi('/solicitacoes');
        if(!res.ok){
            throw new Error(`Falha ao receber dados`)
        }
        return res.json();

}
const promessaSolicitacoes = carregarSolicitacoes();

</script>



<svelte:head>
    <title>Dashboard</title>
</svelte:head>


  <!-- O seu layout original é renderizado aqui somente após os dados serem carregados -->
  <div class="flex h-screen bg-gray-100">
    <!-- Sidebar -->
     
    <Menu2 activePage='/home'></Menu2>
  
   <!-- Main Content -->
    <div class="flex-1 flex flex-col">

      <!-- Header com boas-vindas e botão de logout -->
  
  
      <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
        <h1 class="text-xl font-semibold">Painel</h1>
        <UserMenu></UserMenu>
      </header>

      <!-- Dashboard Cards -->
      <main class="flex-1 p-6 overflow-auto">       

        {#await promessaSolicitacoes}
          <p>Carregando painel</p>
        {:then solicitacoes}
        {#if solicitacoes && solicitacoes.length > 0}
        <div class="space-y-4">
            {#each solicitacoes as s}
            <div class="rounded shadow bg-white p-4">
              <p>Nome  do Paciente: {s.nomePaciente}</p>
              <div class="flex">
                <p class="font-bold">CPF:{s.cpfPaciente}</p>
                <p>USF: {s.usfOrigem}</p>
              </div>

              <button>Confirmar V</button>
              <button>Não compareceu F</button>
            </div>
            {/each}
        </div>
         
        {:else}
          <p>Nenhuma solicitação encontrada</p>
        {/if}
        
      {:catch error}
          <p>Erro ao carregar dados {error.message}</p>
        {/await}
         
     </main>
    
    </div>
  </div>

