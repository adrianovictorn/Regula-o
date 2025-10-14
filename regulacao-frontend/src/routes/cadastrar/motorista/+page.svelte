<script lang="ts">
  import { getApi, postApi, putApi, deleteByIdApi } from '$lib/api.js';
  import RoleBasedMenu from '$lib/RoleBasedMenu.svelte';
  import UserMenu from '$lib/UserMenu.svelte';
  import { onMount } from 'svelte';
  import { Pencil, Trash } from 'lucide-svelte';

  interface Motorista {
    id: number;
    nome: string;
    telefone?: string;
    observacoes?: string;
  }

  let motoristas: Motorista[] = [];
  let motoristasFiltrados: Motorista[] = [];
  let termoBusca = '';
  let isLoading = false;
  let erro = '';
  let mensagem = '';

  let nome = '';
  let telefone = '';
  let observacoes = '';

  let editandoId: number | null = null;
  let draft: Partial<Motorista> = {};

  $: motoristasFiltrados = termoBusca.trim()
    ? motoristas.filter((m) => m.nome.toLowerCase().includes(termoBusca.toLowerCase()))
    : motoristas;

  async function carregarMotoristas() {
    isLoading = true;
    erro = '';
    try {
      const res = await getApi('motoristas');
      if (!res.ok) throw new Error('Não foi possível carregar os motoristas.');
      motoristas = await res.json();
    } catch (error: any) {
      erro = error?.message ?? 'Erro ao carregar motoristas.';
    } finally {
      isLoading = false;
    }
  }

  async function criarMotorista() {
    mensagem = '';
    erro = '';
    if (!nome.trim()) {
      erro = 'Informe o nome do motorista.';
      return;
    }

    const payload = {
      nome: nome.trim(),
      telefone: telefone.trim() || null,
      observacoes: observacoes.trim() || null
    };

    try {
      const res = await postApi('motoristas', payload);
      if (!res.ok) throw new Error('Erro ao cadastrar motorista.');
      nome = '';
      telefone = '';
      observacoes = '';
      mensagem = 'Motorista cadastrado com sucesso.';
      await carregarMotoristas();
    } catch (error: any) {
      erro = error?.message ?? 'Erro ao cadastrar motorista.';
    }
  }

  function iniciarEdicao(m: Motorista) {
    editandoId = m.id;
    draft = { ...m };
    mensagem = '';
    erro = '';
  }

  function cancelarEdicao() {
    editandoId = null;
    draft = {};
  }

  async function atualizarMotorista() {
    if (editandoId == null) return;
    if (!draft.nome?.trim()) {
      erro = 'Informe o nome do motorista.';
      return;
    }

    const payload = {
      nome: draft.nome.trim(),
      telefone: draft.telefone?.trim() || null,
      observacoes: draft.observacoes?.trim() || null
    };

    try {
      const res = await putApi(`motoristas/${editandoId}`, payload);
      if (!res.ok) throw new Error('Erro ao atualizar motorista.');
      mensagem = 'Motorista atualizado com sucesso.';
      cancelarEdicao();
      await carregarMotoristas();
    } catch (error: any) {
      erro = error?.message ?? 'Erro ao atualizar motorista.';
    }
  }

  async function excluirMotorista(id: number) {
    if (!confirm('Deseja realmente remover este motorista?')) return;
    mensagem = '';
    erro = '';
    try {
      const res = await deleteByIdApi(`motoristas/${id}`);
      if (!res.ok && res.status !== 204) throw new Error('Erro ao remover motorista.');
      mensagem = 'Motorista removido.';
      await carregarMotoristas();
    } catch (error: any) {
      erro = error?.message ?? 'Erro ao remover motorista.';
    }
  }

  onMount(() => {
    carregarMotoristas();
  });
</script>

<div class="flex min-h-screen bg-gray-100">
  <RoleBasedMenu activePage="/cadastrar/motorista"/>

  <div class="flex flex-1 flex-col">
    <header class="bg-emerald-700 text-white shadow px-6 py-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Gestão de Motoristas</h1>
      <UserMenu />
    </header>

    <main class="flex-1 overflow-auto p-6 space-y-6">
      <section class="bg-white border border-emerald-100 rounded-xl shadow p-6 space-y-4">
        <h2 class="text-lg font-semibold text-emerald-800">Cadastrar motorista</h2>
        <div class="grid gap-4 md:grid-cols-3">
          <div class="flex flex-col">
            <label class="text-sm text-gray-700 font-medium mb-1">Nome*</label>
            <input
              class="border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              bind:value={nome}
              placeholder="Nome do motorista">
          </div>
          <div class="flex flex-col">
            <label class="text-sm text-gray-700 font-medium mb-1">Telefone</label>
            <input
              class="border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              bind:value={telefone}
              placeholder="(00) 00000-0000"
            />
          </div>
          <div class="flex flex-col md:col-span-3">
            <label class="text-sm text-gray-700 font-medium mb-1">Observações</label>
            <textarea
              class="border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              rows="2"
              bind:value={observacoes}
              placeholder="Informações adicionais"
            ></textarea>
          </div>
        </div>
        <div>
          <button
            type="button"
            class="bg-emerald-700 text-white px-4 py-2 rounded-lg hover:bg-emerald-800 transition"
            on:click={criarMotorista}
          >
            Salvar
          </button>
        </div>
        {#if mensagem}
          <p class="text-sm text-emerald-700">{mensagem}</p>
        {/if}
        {#if erro}
          <p class="text-sm text-red-600">{erro}</p>
        {/if}
      </section>

      <section class="bg-white border border-gray-200 rounded-xl shadow p-6 space-y-4">
        <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <h2 class="text-lg font-semibold text-gray-800">Motoristas cadastrados</h2>
          <input
            type="search"
            placeholder="Buscar por nome"
            class="border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-emerald-500 md:w-64"
            bind:value={termoBusca}
          />
        </div>

        {#if isLoading}
          <p class="text-sm text-gray-500">Carregando motoristas...</p>
        {:else if motoristasFiltrados.length === 0}
          <p class="text-sm text-gray-500">Nenhum motorista encontrado.</p>
        {:else}
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200 text-sm">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-4 py-2 text-left font-medium text-gray-600">Nome</th>
                  <th class="px-4 py-2 text-left font-medium text-gray-600">Telefone</th>
                  <th class="px-4 py-2 text-left font-medium text-gray-600">Observações</th>
                  <th class="px-4 py-2 text-right font-medium text-gray-600">Ações</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-gray-100">
                {#each motoristasFiltrados as motorista}
                  <tr>
                    <td class="px-4 py-2 text-gray-800">
                      {#if editandoId === motorista.id}
                        <input
                          class="w-full border border-gray-300 rounded px-2 py-1"
                          bind:value={draft.nome}
                        />
                      {:else}
                        {motorista.nome}
                      {/if}
                    </td>
                    <td class="px-4 py-2 text-gray-700">
                      {#if editandoId === motorista.id}
                        <input
                          class="w-full border border-gray-300 rounded px-2 py-1"
                          bind:value={draft.telefone}
                        />
                      {:else}
                        {motorista.telefone || '-'}
                      {/if}
                    </td>
                    <td class="px-4 py-2 text-gray-700">
                      {#if editandoId === motorista.id}
                        <textarea
                          class="w-full border border-gray-300 rounded px-2 py-1"
                          rows="2"
                          bind:value={draft.observacoes}
                        ></textarea>
                      {:else}
                        {motorista.observacoes || '-'}
                      {/if}
                    </td>
                    <td class="px-4 py-2">
                      <div class="flex justify-end gap-2">
                        {#if editandoId === motorista.id}
                          <button
                            class="bg-emerald-600 text-white px-3 py-1 rounded hover:bg-emerald-700"
                            type="button"
                            on:click={atualizarMotorista}
                          >
                            Salvar
                          </button>
                          <button
                            class="border border-gray-300 px-3 py-1 rounded hover:bg-gray-100"
                            type="button"
                            on:click={cancelarEdicao}
                          >
                            Cancelar
                          </button>
                        {:else}
                          <button
                            class="text-emerald-600 hover:text-emerald-800"
                            type="button"
                            on:click={() => iniciarEdicao(motorista)}
                          >
                            <Pencil size={18} />
                          </button>
                          <button
                            class="text-red-500 hover:text-red-700"
                            type="button"
                            on:click={() => excluirMotorista(motorista.id)}
                          >
                            <Trash size={18} />
                          </button>
                        {/if}
                      </div>
                    </td>
                  </tr>
                {/each}
              </tbody>
            </table>
          </div>
        {/if}
      </section>
    </main>
  </div>
</div>
