<script lang="ts">
  import RoleBasedMenu from '$lib/RoleBasedMenu.svelte';
  import UserMenu from '$lib/UserMenu.svelte';
  import { onMount } from 'svelte';
  import { listarEspecialidadesCatalogo, criarEspecialidadeCatalogo } from '$lib/especialidadesApi.js';

  let isLoading = false;
  let nome = '';
  let codigo = '';
  let categoria: 'ESPECIALIDADE_MEDICA' | 'EXAME_OU_PROCEDIMENTO' = 'ESPECIALIDADE_MEDICA';
  let ativo = true;
  let lista: any[] = [];
  let erro: string | null = null;
  let termoBusca = '';

  function normalize(s: string) {
    return (s || '')
      .toString()
      .normalize('NFD')
      .replace(/\p{Diacritic}/gu, '')
      .toLowerCase();
  }

  $: listaFiltrada = !termoBusca
    ? lista
    : lista.filter((e) => {
        const q = normalize(termoBusca);
        return (
          normalize(e.nome).includes(q) ||
          normalize(e.codigo).includes(q) ||
          normalize(e.categoria).includes(q)
        );
      });

  async function carregarLista() {
    try {
      const data = await listarEspecialidadesCatalogo();
      lista = data;
    } catch (e: any) {
      erro = e?.message || 'Falha ao carregar lista';
    }
  }

  async function salvar(e: Event) {
    e.preventDefault();
    isLoading = true;
    erro = null;
    try {
      const res = await criarEspecialidadeCatalogo({ codigo: codigo?.trim() || undefined, nome: nome?.trim(), categoria, ativo });
      if (res.ok) {
        alert('Especialidade salva com sucesso');
        nome = '';
        codigo = '';
        categoria = 'ESPECIALIDADE_MEDICA';
        ativo = true;
        await carregarLista();
      } else {
        const body = await res.json().catch(() => ({}));
        alert(`Erro ao salvar: ${body.message || res.status}`);
      }
    } finally {
      isLoading = false;
    }
  }

  onMount(carregarLista);
</script>

<svelte:head>
  <title>Cadastrar Especialidade</title>
  </svelte:head>

<div class="flex h-screen bg-gray-100">
  <RoleBasedMenu activePage="/cadastrar/especialidade" />
  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Cadastro de Especialidades</h1>
      <UserMenu />
    </header>

    <main class="flex-1 overflow-auto p-6">
      <div class="bg-white rounded-lg shadow-lg p-6">
        <h2 class="text-2xl font-bold text-emerald-800 mb-6">Nova Especialidade</h2>
        <form on:submit|preventDefault={salvar} class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="flex flex-col">
              <label class="text-sm font-medium text-gray-700 mb-1">Nome</label>
              <input class="border border-gray-300 rounded-lg p-2" bind:value={nome} required />
            </div>
            <div class="flex flex-col">
              <label class="text-sm font-medium text-gray-700 mb-1">Código (opcional)</label>
              <input class="border border-gray-300 rounded-lg p-2" bind:value={codigo} placeholder="Se vazio, será gerado do nome" />
            </div>
            <div class="flex flex-col">
              <label class="text-sm font-medium text-gray-700 mb-1">Categoria</label>
              <select class="border border-gray-300 rounded-lg p-2" bind:value={categoria}>
                <option value="ESPECIALIDADE_MEDICA">Especialidade Médica</option>
                <option value="EXAME_OU_PROCEDIMENTO">Exame ou Procedimento</option>
              </select>
            </div>
            <div class="flex items-center">
              <label class="inline-flex items-center space-x-2">
                <input type="checkbox" bind:checked={ativo} />
                <span>Ativo</span>
              </label>
            </div>
          </div>
          <button type="submit" class="bg-emerald-700 text-white px-4 py-2 rounded hover:bg-emerald-800" disabled={isLoading}>
            {#if isLoading}Salvando...{:else}Salvar{/if}
          </button>
        </form>
      </div>

      <div class="bg-white rounded-lg shadow-lg p-6 mt-6">
        <h2 class="text-xl font-semibold text-emerald-800 mb-4">Especialidades Cadastradas</h2>
        <div class="flex items-center gap-3 mb-4">
          <input
            type="text"
            class="border border-gray-300 rounded-lg p-2 w-full md:w-96"
            placeholder="Buscar por nome, código ou categoria..."
            bind:value={termoBusca}
          />
          <button
            class="px-3 py-2 text-sm bg-gray-100 rounded border border-gray-300 hover:bg-gray-200"
            type="button"
            on:click={() => { termoBusca = ''; }}
          >Limpar</button>
        </div>
        {#if erro}
          <p class="text-red-500">{erro}</p>
        {/if}
        <div class="overflow-auto">
          <table class="w-full text-left text-sm">
            <thead>
              <tr class="border-b">
                <th class="py-2 px-2">ID</th>
                <th class="py-2 px-2">Código</th>
                <th class="py-2 px-2">Nome</th>
                <th class="py-2 px-2">Categoria</th>
                <th class="py-2 px-2">Ativo</th>
              </tr>
            </thead>
            <tbody>
              {#each listaFiltrada as e (e.id)}
                <tr class="border-b hover:bg-gray-50">
                  <td class="py-2 px-2">{e.id}</td>
                  <td class="py-2 px-2">{e.codigo}</td>
                  <td class="py-2 px-2">{e.nome}</td>
                  <td class="py-2 px-2">{e.categoria}</td>
                  <td class="py-2 px-2">{e.ativo ? 'Sim' : 'Não'}</td>
                </tr>
              {/each}
            </tbody>
          </table>
        </div>
      </div>
    </main>
  </div>
</div>
