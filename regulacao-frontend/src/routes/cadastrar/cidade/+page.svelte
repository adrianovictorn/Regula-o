<script lang="ts">
  import { getApi, postApi } from '$lib/api';
  import Menu from '$lib/Menu.svelte';
    import RoleBasedMenu from '$lib/RoleBasedMenu.svelte';
  import UserMenu from '$lib/UserMenu.svelte';
  import { onMount } from 'svelte';

  interface Cidade {
    id: number;
    nomeCidade: string;
    codigoIBGE: string;
    cep: string;
  }

  let novoNome = $state('');
  let novoCodigo = $state('');
  let novoCep = $state('');

  let cidades = $state<Cidade[]>([]);
  let termoBusca = $state('');
  let isLoading = $state(false);
  let mensagem = $state('');
  let erro = $state('');

  const cidadesFiltradas = $derived(
    termoBusca.trim()
      ? cidades.filter((c) => c.nomeCidade.toLocaleLowerCase().includes(termoBusca.toLocaleLowerCase()))
      : cidades
  );

  async function carregarCidades() {
    isLoading = true;
    erro = '';
    try {
      const res = await getApi('cidades');
      if (!res.ok) {
        throw new Error('Não foi possível carregar as cidades.');
      }
      cidades = await res.json();
    } catch (e: any) {
      erro = e.message ?? 'Erro ao carregar cidades.';
    } finally {
      isLoading = false;
    }
  }

  async function cadastrarCidade(event: Event) {
    event.preventDefault();
    mensagem = '';
    erro = '';

    const payload = {
      nomeCidade: novoNome,
      codigoIBGE: novoCodigo,
      cep: novoCep
    };

    try {
      const res = await postApi('cidades/cadastrar', payload);

      if (!res.ok) {
        throw new Error('Erro ao enviar dados ao servidor.');
      }

      mensagem = 'Cidade cadastrada com sucesso!';
      novoNome = '';
      novoCodigo = '';
      novoCep = '';
      await carregarCidades();
    } catch (e: any) {
      erro = e.message ?? 'Erro ao se conectar com o servidor.';
    }
  }

  onMount(carregarCidades);
</script>

<svelte:head>
  <title>Cadastro de Cidade</title>
</svelte:head>

<div class="flex min-h-screen bg-gray-100">
  <RoleBasedMenu activePage="/cadastrar/cidade" />

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Cadastrar Cidade</h1>
      <UserMenu />
    </header>

    <main class="flex-1 overflow-auto p-6">
      <div class="grid gap-6 lg:grid-cols-3">
        <section class="lg:col-span-1 bg-white rounded-lg shadow p-6 space-y-4">
          <h2 class="text-lg font-semibold text-emerald-700">Nova Cidade</h2>

          {#if mensagem}
            <div class="rounded bg-emerald-100 text-emerald-800 px-3 py-2 text-sm">{mensagem}</div>
          {/if}

          {#if erro}
            <div class="rounded bg-red-100 text-red-700 px-3 py-2 text-sm">{erro}</div>
          {/if}

          <form class="space-y-4" on:submit={cadastrarCidade}>
            <div class="flex flex-col space-y-1">
              <label class="text-sm font-medium text-gray-700" for="nomeCidade">Nome</label>
              <input
                id="nomeCidade"
                type="text"
                bind:value={novoNome}
                required
                class="border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>

            <div class="flex flex-col space-y-1">
              <label class="text-sm font-medium text-gray-700" for="codigoIBGE">Código IBGE</label>
              <input
                id="codigoIBGE"
                type="text"
                bind:value={novoCodigo}
                class="border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>

            <div class="flex flex-col space-y-1">
              <label class="text-sm font-medium text-gray-700" for="cep">CEP</label>
              <input
                id="cep"
                type="text"
                bind:value={novoCep}
                class="border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>

            <button
              type="submit"
              class="w-full bg-emerald-700 text-white py-2 rounded-lg hover:bg-emerald-800 transition"
            >
              Cadastrar
            </button>
          </form>
        </section>

        <section class="lg:col-span-2 bg-white rounded-lg shadow p-6 space-y-4">
          <div class="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
            <div>
              <h2 class="text-lg font-semibold text-emerald-700">Cidades cadastradas</h2>
              <p class="text-sm text-gray-500">Visualize e busque cidades já registradas.</p>
            </div>

            <div class="w-full md:w-64">
              <input
                type="text"
                bind:value={termoBusca}
                placeholder="Buscar pelo nome..."
                class="w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>
          </div>

          {#if isLoading}
            <p class="text-gray-500 text-sm">Carregando cidades...</p>
          {:else if erro && cidades.length === 0}
            <p class="text-red-600 text-sm">{erro}</p>
          {:else if cidadesFiltradas.length === 0}
            <p class="text-gray-500 text-sm">Nenhuma cidade encontrada.</p>
          {:else}
            <div class="overflow-hidden rounded-lg border border-gray-200">
              <table class="min-w-full divide-y divide-gray-200 text-sm">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-4 py-2 text-left font-medium text-gray-600">Nome</th>
                    <th class="px-4 py-2 text-left font-medium text-gray-600">Código IBGE</th>
                    <th class="px-4 py-2 text-left font-medium text-gray-600">CEP</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                  {#each cidadesFiltradas as cidade}
                    <tr class="hover:bg-gray-50">
                      <td class="px-4 py-2 text-gray-700">{cidade.nomeCidade}</td>
                      <td class="px-4 py-2 text-gray-600">{cidade.codigoIBGE || '—'}</td>
                      <td class="px-4 py-2 text-gray-600">{cidade.cep || '—'}</td>
                    </tr>
                  {/each}
                </tbody>
              </table>
            </div>
          {/if}
        </section>
      </div>
    </main>
  </div>
</div>
