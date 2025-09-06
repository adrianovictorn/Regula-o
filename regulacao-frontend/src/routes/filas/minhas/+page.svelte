<script>
  import { user } from '$lib/stores/auth.js';
  import UserMenu from '$lib/UserMenu.svelte';
  import AdminHome from '$lib/AdminHome.svelte';
  import UserHome from '$lib/UserHome.svelte';
  import RoleBasedMenu from '$lib/RoleBasedMenu.svelte'; 

   const solicitacoesRecebidas = [
        { 
            id: 1, 
            paciente: 'Maria Silva', 
            especialidade: 'CARDIOLOGISTA', 
            municipioOrigem: 'SAJ', 
            status: 'PENDENTE', 
            dataEnvio: '03/09/2025' 
        },
        { 
            id: 2, 
            paciente: 'João Santos', 
            especialidade: 'ORTOPEDISTA', 
            municipioOrigem: 'CONCEICAO_DO_ALMEIDA', 
            status: 'PENDENTE', 
            dataEnvio: '02/09/2025' 
        }
    ];

    // Lista de solicitações que você enviou para outros municípios
    const solicitacoesEnviadas = [
        { 
            id: 3, 
            paciente: 'Carlos Pereira', 
            especialidade: 'NEUROLOGISTA', 
            municipioDestino: 'VARZEDO', 
            status: 'ACEITO', 
            dataEnvio: '01/09/2025' 
        },
        { 
            id: 4, 
            paciente: 'Ana Lima', 
            especialidade: 'ENDOCRINOLOGISTA', 
            municipioDestino: 'SAJ', 
            status: 'PENDENTE', 
            dataEnvio: '03/09/2025' 
        },
        { 
            id: 5, 
            paciente: 'Lucas Costa', 
            especialidade: 'CARDIOLOGISTA', 
            municipioDestino: 'SAJ', 
            status: 'RECUSADO', 
            dataEnvio: '31/08/2025' 
        }
    ];

    // --- Controle de Estado da UI ---
    let activeTab = 'recebidas'; // Define a aba inicial como 'recebidas'

    // Funções de ação simuladas
    function aceitar(id) {
        console.log(`Aceitando solicitação ID: ${id}`);
        alert(`Simulação: Aceitando solicitação ${id}`);
    }

    function recusar(id) {
        console.log(`Recusando solicitação ID: ${id}`);
        alert(`Simulação: Recusando solicitação ${id}`);
    }
</script>

<svelte:head>
    <title>Minhas Solicitações</title>
</svelte:head>

<div class="flex h-screen bg-gray-100">
  
  <RoleBasedMenu activePage="/filas/minhas" />

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Minhas Solicitações</h1>
      {#if $user}
        <UserMenu />
      {:else}
        <div>
          <a href="/login" class="hover:underline">Fazer Login</a>
        </div>
      {/if}
    </header>

    <main class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-200 p-6">
            <div class="bg-white p-6 rounded-lg shadow-md">
                
                <div class="border-b border-gray-200 mb-4">
                    <nav class="-mb-px flex space-x-8" aria-label="Tabs">
                        <button
                            class="whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm"
                            class:border-emerald-500="{activeTab === 'recebidas'}"
                            class:text-emerald-600="{activeTab === 'recebidas'}"
                            class:border-transparent="{activeTab !== 'recebidas'}"
                            class:text-gray-500="{activeTab !== 'recebidas'}"
                            class:hover:text-gray-700="{activeTab !== 'recebidas'}"
                            class:hover:border-gray-300="{activeTab !== 'recebidas'}"
                            on:click={() => activeTab = 'recebidas'}>
                            Solicitações Recebidas
                        </button>
                        <button
                            class="whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm"
                            class:border-emerald-500="{activeTab === 'enviadas'}"
                            class:text-emerald-600="{activeTab === 'enviadas'}"
                            class:border-transparent="{activeTab !== 'enviadas'}"
                            class:text-gray-500="{activeTab !== 'enviadas'}"
                            class:hover:text-gray-700="{activeTab !== 'enviadas'}"
                            class:hover:border-gray-300="{activeTab !== 'enviadas'}"
                            on:click={() => activeTab = 'enviadas'}>
                            Solicitações Enviadas
                        </button>
                    </nav>
                </div>

                {#if activeTab === 'recebidas'}
                    <div class="overflow-x-auto">
                        <table class="min-w-full bg-white">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th class="py-3 px-4 text-left">Paciente</th>
                                    <th class="py-3 px-4 text-left">Especialidade</th>
                                    <th class="py-3 px-4 text-left">Município de Origem</th>
                                    <th class="py-3 px-4 text-left">Data</th>
                                    <th class="py-3 px-4 text-center">Ações</th>
                                </tr>
                            </thead>
                            <tbody class="text-gray-700">
                                {#each solicitacoesRecebidas as item (item.id)}
                                    <tr class="border-b">
                                        <td class="py-3 px-4">{item.paciente}</td>
                                        <td class="py-3 px-4">{item.especialidade}</td>
                                        <td class="py-3 px-4">{item.municipioOrigem}</td>
                                        <td class="py-3 px-4">{item.dataEnvio}</td>
                                        <td class="py-3 px-4 text-center">
                                            <button on:click={() => aceitar(item.id)} class="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600 text-sm">Aceitar</button>
                                            <button on:click={() => recusar(item.id)} class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 text-sm ml-2">Recusar</button>
                                        </td>
                                    </tr>
                                {/each}
                            </tbody>
                        </table>
                    </div>
                {/if}

                {#if activeTab === 'enviadas'}
                     <div class="overflow-x-auto">
                        <table class="min-w-full bg-white">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th class="py-3 px-4 text-left">Paciente</th>
                                    <th class="py-3 px-4 text-left">Especialidade</th>
                                    <th class="py-3 px-4 text-left">Município de Destino</th>
                                    <th class="py-3 px-4 text-left">Data</th>
                                    <th class="py-3 px-4 text-center">Status</th>
                                </tr>
                            </thead>
                            <tbody class="text-gray-700">
                                {#each solicitacoesEnviadas as item (item.id)}
                                    <tr class="border-b">
                                        <td class="py-3 px-4">{item.paciente}</td>
                                        <td class="py-3 px-4">{item.especialidade}</td>
                                        <td class="py-3 px-4">{item.municipioDestino}</td>
                                        <td class="py-3 px-4">{item.dataEnvio}</td>
                                        <td class="py-3 px-4 text-center">
                                            <span class="px-2 py-1 font-semibold leading-tight text-xs rounded-full"
                                                class:text-green-700="{item.status === 'ACEITO'}" class:bg-green-100="{item.status === 'ACEITO'}"
                                                class:text-yellow-700="{item.status === 'PENDENTE'}" class:bg-yellow-100="{item.status === 'PENDENTE'}"
                                                class:text-red-700="{item.status === 'RECUSADO'}" class:bg-red-100="{item.status === 'RECUSADO'}">
                                                {item.status}
                                            </span>
                                        </td>
                                    </tr>
                                {/each}
                            </tbody>
                        </table>
                    </div>
                {/if}

            </div>
        </main>
    </div>
    
</div>
