<script>
  import { onMount } from 'svelte';
  import { responderConvite } from '$lib/pactosApi.js';
  import { page } from '$app/stores';

  let token = '';
  let status = 'idle';
  let message = '';

  onMount(() => {
    token = $page.params.token;
  });

  async function aceitar() {
    try {
      status = 'loading';
      await responderConvite(token, true);
      status = 'done';
      message = 'Convite aceito com sucesso. Você já pode participar do pacto.';
    } catch (e) {
      status = 'error';
      message = e.message || 'Falha ao aceitar convite';
    }
  }

  async function recusar() {
    try {
      status = 'loading';
      await responderConvite(token, false);
      status = 'done';
      message = 'Convite recusado.';
    } catch (e) {
      status = 'error';
      message = e.message || 'Falha ao recusar convite';
    }
  }
</script>

<svelte:head><title>Convite de Pacto</title></svelte:head>

<div class="min-h-screen flex items-center justify-center bg-gray-100 p-6">
  <div class="bg-white rounded-lg shadow p-6 w-full max-w-lg">
    <h1 class="text-xl font-semibold mb-2">Convite para participar de um Pacto</h1>
    <p class="text-sm text-gray-600 mb-4">Token: <span class="font-mono">{token}</span></p>

    {#if status === 'idle'}
      <p class="mb-4">Deseja aceitar este convite?</p>
      <div class="flex gap-2">
        <button class="px-4 py-2 rounded bg-emerald-600 text-white hover:bg-emerald-700" on:click={aceitar}>Aceitar</button>
        <button class="px-4 py-2 rounded border" on:click={recusar}>Recusar</button>
      </div>
    {:else if status === 'loading'}
      <p>Processando...</p>
    {:else}
      <div class="mt-2">{message}</div>
    {/if}
  </div>
  
</div>

