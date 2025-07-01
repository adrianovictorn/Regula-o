<script>
  import { onMount } from 'svelte';
  import { goto } from '$app/navigation';
  import { user, token } from '$lib/stores/auth.js';
  import { fadeScale } from '$lib/transitions.js'; // 1. Importe a nova transição

  let isOpen = false;
  let node;

  function logout() {
    isOpen = false;
    token.set(null);
    goto('/login');
  }

  const handleClickOutside = (event) => {
    if (node && !node.contains(event.target)) {
      isOpen = false;
    }
  };

  onMount(() => {
    document.addEventListener('click', handleClickOutside, true);
    return () => {
      document.removeEventListener('click', handleClickOutside, true);
    };
  });
</script>

{#if $user}
  <div class="relative" bind:this={node}>
    <button
      on:click={() => (isOpen = !isOpen)}
      class="flex items-center space-x-3 p-1.5 rounded-full hover:bg-emerald-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-emerald-700 focus:ring-white transition-all duration-200"
      aria-haspopup="true"
      aria-expanded={isOpen}
    >
      <div class="w-9 h-9 rounded-full bg-emerald-50 flex items-center justify-center text-emerald-800 font-semibold ring-1 ring-white/30">
        {($user.nome || 'U').charAt(0).toUpperCase()}
      </div>
      <span class="hidden md:block font-medium text-white pr-1">{$user.nome}</span>
      <svg class="w-5 h-5 text-white/90 transform transition-transform duration-200" class:rotate-180={isOpen} xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
      </svg>
    </button>

    {#if isOpen}
      <div
        in:fadeScale={{ duration: 150, start: 0.95 }}
        class="absolute right-0 mt-2 w-56 origin-top-right bg-white rounded-md shadow-lg z-10 ring-1 ring-black ring-opacity-5 focus:outline-none"
      >
        <div class="py-1">
          <div class="px-4 py-3 border-b border-gray-200">
            <p class="text-sm text-gray-500">Logado como</p>
            <p class="text-sm font-medium text-gray-800 truncate">{$user.nome}</p>
          </div>
          <a href="/perfil" class="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors">
            <svg class="w-5 h-5 mr-3 text-gray-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
            Meu Perfil
          </a>
          <a href="/admin/cadastrar-usuario" class="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors">
            <svg class="w-5 h-5 mr-3 text-gray-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" /><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /></svg>
            Configurações
          </a>
          <div class="border-t border-gray-200"></div>
          <button on:click={logout} class="w-full text-left flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-red-50 hover:text-red-700 transition-colors">
            <svg class="w-5 h-5 mr-3 text-gray-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" /></svg>
            Sair
          </button>
        </div>
      </div>
    {/if}
  </div>
{:else}
  <div>
    <a href="/login" class="hover:underline">Fazer Login</a>
  </div>
{/if}