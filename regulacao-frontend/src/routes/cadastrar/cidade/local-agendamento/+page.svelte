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

  interface LocalAgendamento {
    id: number;
    nomeLocal: string;
    endereco: string;
    numero: string;
    cidadeId: number | null;
    cidadeNome: string | null;
    enumValue?: string | null;
  }

  let novoNomeLocal = $state('');
  let novoEndereco = $state('');
  let novoNumero = $state('');
  let cidadesExistentes = $state<Cidade[]>([]);
  let cidadeId = $state<string>('');
  let locais = $state<LocalAgendamento[]>([]);

  let termoBuscaCidade = $state('');
  let termoBuscaLocal = $state('');
  let isLoading = $state(false);
  let erro = $state('');
  let mensagem = $state('');

  const cidadesFiltradas = $derived(
    termoBuscaCidade.trim()
      ? cidadesExistentes.filter((c) => c.nomeCidade.toLocaleLowerCase().includes(termoBuscaCidade.toLocaleLowerCase()))
      : cidadesExistentes
  );

  const locaisFiltrados = $derived(
    termoBuscaLocal.trim()
      ? locais.filter((l) => l.nomeLocal.toLocaleLowerCase().includes(termoBuscaLocal.toLocaleLowerCase()))
      : locais
  );

  async function carregarCidades() {
    try {
      const res = await getApi('cidades');
      if (!res.ok) {
        throw new Error('Erro ao receber dados das cidades.');
      }
      cidadesExistentes = await res.json();
    } catch (e: any) {
      erro = e.message ?? 'Erro ao buscar cidades.';
    }
  }

  async function carregarLocaisAgendamento() {
    try {
      const res = await getApi('local/agendamento');
      if (!res.ok) {
        throw new Error('Erro ao buscar locais de agendamento.');
      }
      locais = await res.json();
    } catch (e: any) {
      erro = e.message ?? 'Erro ao carregar locais.';
    }
  }

  async function cadastrarLocalAgendamento(event: Event) {
    event.preventDefault();
    mensagem = '';
    erro = '';

    const cidadeSelecionadaId = cidadeId ? Number(cidadeId) : null;

    if (!cidadeSelecionadaId) {
      erro = 'Selecione uma cidade antes de cadastrar o local.';
      return;
    }

    const payload = {
      nomeLocal: novoNomeLocal,
      endereco: novoEndereco,
      numero: novoNumero,
      cidadeId: cidadeSelecionadaId
    };

    try {
      const res = await postApi('local/agendamento/cadastrar', payload);
      if (!res.ok) {
        throw new Error('Erro ao cadastrar o local de agendamento.');
      }

      mensagem = 'Local cadastrado com sucesso!';
      novoNomeLocal = '';
      novoEndereco = '';
      novoNumero = '';
      cidadeId = '';

      await carregarLocaisAgendamento();
    } catch (e: any) {
      erro = e.message ?? 'Erro ao se conectar ao servidor!';
    }
  }

  onMount(async () => {
    isLoading = true;
    await Promise.all([carregarCidades(), carregarLocaisAgendamento()]);
    isLoading = false;
  });
</script>

<svelte:head>
  <title>Cadastro de Local de Agendamento</title>
</svelte:head>

<div class="flex min-h-screen bg-gray-100">
  <RoleBasedMenu activePage="/cadastrar/cidade/local-agendamento" />

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Cadastrar Local de Agendamento</h1>
      <UserMenu />
    </header>

    <main class="flex-1 overflow-auto p-6">
      <div class="grid gap-6 lg:grid-cols-3">
        <section class="lg:col-span-1 bg-white rounded-lg shadow p-6 space-y-4">
          <h2 class="text-lg font-semibold text-emerald-700">Novo Local</h2>

          {#if mensagem}
            <div class="rounded bg-emerald-100 text-emerald-800 px-3 py-2 text-sm">{mensagem}</div>
          {/if}

          {#if erro}
            <div class="rounded bg-red-100 text-red-700 px-3 py-2 text-sm">{erro}</div>
          {/if}

          <form class="space-y-4" on:submit={cadastrarLocalAgendamento}>
            <div class="flex flex-col space-y-1">
              <label class="text-sm font-medium text-gray-700" for="nomeLocal">Nome do Local</label>
              <input
                id="nomeLocal"
                type="text"
                bind:value={novoNomeLocal}
                required
                placeholder="Hospital, clínica, etc."
                class="border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>

            <div class="flex flex-col space-y-1">
              <label class="text-sm font-medium text-gray-700" for="endereco">Endereço</label>
              <input
                id="endereco"
                type="text"
                bind:value={novoEndereco}
                placeholder="Rua, bairro..."
                class="border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>

            <div class="flex flex-col space-y-1">
              <label class="text-sm font-medium text-gray-700" for="numero">Número</label>
              <input
                id="numero"
                type="text"
                bind:value={novoNumero}
                class="border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>

            <div class="flex flex-col space-y-1">
              <label class="text-sm font-medium text-gray-700" for="buscaCidade">Buscar cidade</label>
              <input
                id="buscaCidade"
                type="text"
                bind:value={termoBuscaCidade}
                placeholder="Digite para filtrar..."
                class="border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>

            <div class="flex flex-col space-y-1">
              <label class="text-sm font-medium text-gray-700" for="cidade">Cidade</label>
              <select
                id="cidade"
                bind:value={cidadeId}
                class="border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              >
                <option value="" disabled>Selecione...</option>
                {#each cidadesFiltradas as cidade}
                  <option value={cidade.id}>{cidade.nomeCidade}</option>
                {/each}
              </select>
            </div>

            <button
              type="submit"
              class="w-full bg-emerald-700 text-white py-2 rounded-lg hover:bg-emerald-800 transition"
            >
              Cadastrar Local
            </button>
          </form>
        </section>

        <section class="lg:col-span-2 bg-white rounded-lg shadow p-6 space-y-4">
          <div class="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
            <div>
              <h2 class="text-lg font-semibold text-emerald-700">Locais cadastrados</h2>
              <p class="text-sm text-gray-500">Consulte e filtre os locais registrados.</p>
            </div>

            <div class="w-full md:w-64">
              <input
                type="text"
                bind:value={termoBuscaLocal}
                placeholder="Buscar pelo nome do local..."
                class="w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              />
            </div>
          </div>

          {#if isLoading}
            <p class="text-sm text-gray-500">Carregando informações...</p>
          {:else if locaisFiltrados.length === 0}
            <p class="text-sm text-gray-500">Nenhum local cadastrado até o momento.</p>
          {:else}
            <div class="overflow-hidden rounded-lg border border-gray-200">
              <table class="min-w-full divide-y divide-gray-200 text-sm">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-4 py-2 text-left font-medium text-gray-600">Nome</th>
                    <th class="px-4 py-2 text-left font-medium text-gray-600">Endereço</th>
                    <th class="px-4 py-2 text-left font-medium text-gray-600">Número</th>
                    <th class="px-4 py-2 text-left font-medium text-gray-600">Cidade</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-gray-100">
                  {#each locaisFiltrados as loc}
                    <tr class="hover:bg-gray-50">
                      <td class="px-4 py-2 text-gray-700">{loc.nomeLocal}</td>
                      <td class="px-4 py-2 text-gray-600">{loc.endereco || '—'}</td>
                      <td class="px-4 py-2 text-gray-600">{loc.numero || '—'}</td>
                      <td class="px-4 py-2 text-gray-600">{loc.cidadeNome || '—'}</td>
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
