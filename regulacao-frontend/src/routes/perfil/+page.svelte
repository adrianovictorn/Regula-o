<script>
  import { onMount } from 'svelte';
  import { fly } from 'svelte/transition';
  import { user } from '$lib/stores/auth.js';
  import Menu from '$lib/Menu.svelte';
  import UserMenu from '$lib/UserMenu.svelte';

  // Variáveis para funcionalidade de edição
  let isEditing = false;
  let nomeUsuario = $user?.nome || '';
  
  // Variável para controlar a animação de entrada
  let isMounted = false;
  onMount(() => {
    isMounted = true;
  });

  function handleSaveChanges() {
    console.log('Salvando alterações para:', nomeUsuario);
    isEditing = false;
    // Futuramente, aqui você chamaria a API para salvar
  }
</script>

<svelte:head>
    <title>Perfil</title>
</svelte:head>

<div class="flex h-screen bg-gray-50 font-sans">
  <Menu activePage="/perfil" />

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between z-10">
      <h1 class="text-xl font-semibold">Meu Perfil</h1>
      <UserMenu />
    </header>

    <main class="flex-1 p-4 md:p-8 overflow-auto">
      {#if isMounted}
        <div 
          class="max-w-3xl mx-auto"
          in:fly={{ y: 20, duration: 500, delay: 100 }}
        >
          <div class="bg-white rounded-lg shadow-xl overflow-hidden">
            <div class="bg-emerald-500 h-32" />

            <div class="p-6 md:p-8 relative">
              <div class="absolute -mt-24">
                <div class="w-28 h-28 rounded-full bg-emerald-100 flex items-center justify-center text-emerald-600 text-5xl font-bold ring-4 ring-white">
                  {($user.nome || 'U').charAt(0).toUpperCase()}
                </div>
              </div>

              <div class="flex justify-between items-start pt-8">
                <div class="ml-32">
                  <h2 class="text-3xl font-bold text-gray-800">{$user.nome}</h2>
                  <p class="text-sm text-gray-500 mt-1">
                    Nível de Acesso:
                    <span class="font-semibold bg-emerald-100 text-emerald-800 py-0.5 px-2 rounded-full">{$user.role}</span>
                  </p>
                </div>
                <div>
                  {#if !isEditing}
                    <button
                      on:click={() => (isEditing = true)}
                      class="flex items-center px-4 py-2 text-sm font-medium text-white bg-emerald-600 rounded-lg hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500 transition-all duration-200 shadow-sm hover:shadow-md"
                    >
                      <svg class="w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.536L16.732 3.732z" /></svg>
                      Editar
                    </button>
                  {/if}
                </div>
              </div>

              <hr class="my-8 border-gray-200" />

              <form on:submit|preventDefault={handleSaveChanges} class="space-y-6">
                <div class="relative">
                  <label for="nome" class="absolute -top-2 left-2 inline-block bg-white px-1 text-xs font-medium text-gray-600">Nome Completo</label>
                  <div class="relative">
                    <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
                      <svg class="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" /></svg>
                    </div>
                    <input type="text" id="nome" bind:value={nomeUsuario} disabled={!isEditing} class="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-md bg-gray-50 focus:ring-emerald-500 focus:border-emerald-500 transition disabled:opacity-70 disabled:bg-gray-100" />
                  </div>
                </div>
                
                <div class="relative">
                  <label for="cpf" class="absolute -top-2 left-2 inline-block bg-white px-1 text-xs font-medium text-gray-600">CPF</label>
                  <div class="relative">
                     <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
                      <svg class="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor"><path d="M4 4a2 2 0 00-2 2v1h16V6a2 2 0 00-2-2H4z" /><path fill-rule="evenodd" d="M18 9H2v5a2 2 0 002 2h12a2 2 0 002-2V9zM4 13a1 1 0 011-1h1a1 1 0 110 2H5a1 1 0 01-1-1zm3 1a1 1 0 100-2 1 1 0 000 2zm3-1a1 1 0 11-2 0 1 1 0 012 0zm3 1a1 1 0 100-2 1 1 0 000 2z" clip-rule="evenodd" /></svg>
                    </div>
                    <input type="text" id="cpf" value={$user.cpf} disabled class="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-md bg-gray-200 text-gray-500 cursor-not-allowed" />
                  </div>
                </div>

                {#if isEditing}
                  <div class="flex justify-end space-x-4 pt-4">
                    <button type="button" on:click={() => isEditing = false} class="px-6 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-lg hover:bg-gray-300 transition-colors">
                      Cancelar
                    </button>
                    <button type="submit" class="px-6 py-2 text-sm font-medium text-white bg-emerald-600 rounded-lg hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500 transition-colors">
                      Salvar Alterações
                    </button>
                  </div>
                {/if}
              </form>
            </div>
          </div>
        </div>
      {/if}
    </main>
  </div>
</div>