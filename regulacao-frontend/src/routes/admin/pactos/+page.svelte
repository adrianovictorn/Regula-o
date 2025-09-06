<script>
  import { onMount } from 'svelte';
  import { user } from '$lib/stores/auth.js';
  import UserMenu from '$lib/UserMenu.svelte';
  import RoleBasedMenu from '$lib/RoleBasedMenu.svelte';
  import { listarPactos, publicarSolicitacao, listarConvites, criarConvites } from '$lib/pactosApi.js';
  import { listarDisponiveis } from '$lib/registryApi.js';
  import { listarMunicipios } from '$lib/municipiosApi.js';
  import { postApi, deleteApi } from '$lib/api.js';

  let pactos = [];
  let municipios = [];
  let loading = false;
  let error = '';
  let carregadoUmaVez = false;

  // Formulário de criação
  let nome = '';
  let descricao = '';
  let membrosSelecionados = [];

  // Convites
  let selectedPactoConviteId = null;
  let disponiveis = [];
  let busca = '';
  let convidadosSelecionados = [];
  let mensagemConvite = '';
  let convites = [];

  async function carregar() {
    loading = true; error = '';
    try {
      [pactos, municipios] = await Promise.all([listarPactos(), listarMunicipios()]);
      if (pactos.length > 0) {
        selectedPactoConviteId = pactos[0].id;
        await Promise.all([carregarDisponiveis(), carregarConvites()]);
      }
    } catch (e) {
      error = e.message || 'Erro ao carregar dados';
    } finally { loading = false; }
  }

  async function criarPacto() {
    try {
      const res = await postApi('pactos', { nome, descricao, membros: membrosSelecionados });
      if (!res.ok) throw new Error('Falha ao criar pacto');
      const novo = await res.json();
      pactos = [novo, ...pactos];
      nome = ''; descricao = ''; membrosSelecionados = [];
      alert('Pacto criado');
    } catch (e) { alert(e.message || 'Erro ao criar pacto'); }
  }

  async function adicionarMembros(pactoId, membros) {
    try {
      const res = await postApi(`pactos/${pactoId}/membros`, { membros });
      if (!res.ok) throw new Error('Falha ao adicionar membros');
      const atualizado = await res.json();
      pactos = pactos.map(p => p.id === pactoId ? atualizado : p);
    } catch (e) { alert(e.message || 'Erro ao adicionar membros'); }
  }

  async function removerMembro(pactoId, municipioId) {
    try {
      const res = await deleteApi(`pactos/${pactoId}/membros/${municipioId}`);
      if (!res.ok) throw new Error('Falha ao remover membro');
      const atualizado = await res.json();
      pactos = pactos.map(p => p.id === pactoId ? atualizado : p);
    } catch (e) { alert(e.message || 'Erro ao remover membro'); }
  }

  // Só carrega dados quando houver usuário autenticado
  $: if ($user && !carregadoUmaVez) {
    carregar();
    carregadoUmaVez = true;
  }

  async function carregarDisponiveis() {
    try {
      disponiveis = await listarDisponiveis(busca);
    } catch (e) {
      console.error(e);
    }
  }

  async function carregarConvites() {
    if (!selectedPactoConviteId) return;
    try {
      convites = await listarConvites(selectedPactoConviteId);
    } catch (e) {
      console.error(e);
    }
  }

  async function enviarConvites() {
    if (!selectedPactoConviteId) return alert('Selecione um pacto');
    if (convidadosSelecionados.length === 0) return alert('Selecione pelo menos um município');
    try {
      await criarConvites(selectedPactoConviteId, convidadosSelecionados, mensagemConvite);
      convidadosSelecionados = [];
      mensagemConvite = '';
      await carregarConvites();
      alert('Convites enviados');
    } catch (e) {
      alert(e.message || 'Erro ao enviar convites');
    }
  }
</script>

<svelte:head><title>Admin - Pactos</title></svelte:head>

<div class="flex h-screen bg-gray-100">
  <RoleBasedMenu activePage="/admin/pactos" />
  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Pactos (Admin)</h1>
      {#if $user}<UserMenu />{:else}<div><a href="/login" class="hover:underline">Fazer Login</a></div>{/if}
    </header>

    <main class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-200 p-6">
      <div class="bg-white p-6 rounded-lg shadow-md space-y-6">
        {#if !$user}
          <div class="text-gray-700">Faça login para gerenciar pactos.</div>
          <a href="/login" class="inline-block mt-2 text-emerald-700 underline">Ir para login</a>
        {:else}
        {#if error}<div class="text-red-600">{error}</div>{/if}

        <section>
          <h2 class="text-lg font-semibold mb-2">Criar novo pacto</h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-3 items-end">
            <div>
              <label class="block text-sm text-gray-600 mb-1">Nome</label>
              <input class="border rounded p-2 w-full" bind:value={nome} placeholder="Nome do pacto" />
            </div>
            <div>
              <label class="block text-sm text-gray-600 mb-1">Descrição</label>
              <input class="border rounded p-2 w-full" bind:value={descricao} placeholder="Descrição" />
            </div>
            <div>
              <label class="block text-sm text-gray-600 mb-1">Membros</label>
              <select class="border rounded p-2 w-full" bind:value={membrosSelecionados} multiple size="4">
                {#each municipios as m}
                  <option value={m.id}>{m.nome}</option>
                {/each}
              </select>
            </div>
          </div>
          <div class="mt-3">
            <button class="bg-emerald-600 text-white px-4 py-2 rounded" on:click={criarPacto}>Criar</button>
          </div>
        </section>

        <section>
          <h2 class="text-lg font-semibold mb-2">Convidar municípios para um pacto</h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-3 items-end">
            <div>
              <label class="block text-sm text-gray-600 mb-1">Pacto</label>
              <select class="border rounded p-2 w-full" bind:value={selectedPactoConviteId} on:change={carregarConvites}>
                {#each pactos as p}
                  <option value={p.id}>{p.nome}</option>
                {/each}
              </select>
            </div>
            <div>
              <label class="block text-sm text-gray-600 mb-1">Buscar municípios</label>
              <div class="flex gap-2">
                <input class="border rounded p-2 w-full" bind:value={busca} placeholder="Nome do município" />
                <button class="px-3 py-2 bg-gray-100 rounded border" on:click={carregarDisponiveis}>Buscar</button>
              </div>
            </div>
            <div>
              <label class="block text-sm text-gray-600 mb-1">Mensagem (opcional)</label>
              <input class="border rounded p-2 w-full" bind:value={mensagemConvite} placeholder="Digite uma mensagem" />
            </div>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-3">
            <div>
              <label class="block text-sm text-gray-600 mb-1">Disponíveis</label>
              <select class="border rounded p-2 w-full" bind:value={convidadosSelecionados} multiple size="6">
                {#each disponiveis as d}
                  <option value={d.id}>{d.nome}</option>
                {/each}
              </select>
            </div>
            <div class="flex items-end">
              <button class="bg-emerald-600 text-white px-4 py-2 rounded" on:click={enviarConvites}>Enviar convites</button>
            </div>
          </div>

          <div class="mt-6">
            <h3 class="text-md font-semibold mb-2">Convites enviados/recebidos</h3>
            <div class="overflow-x-auto">
              <table class="min-w-full bg-white">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="py-2 px-3 text-left">Token</th>
                    <th class="py-2 px-3 text-left">Pacto</th>
                    <th class="py-2 px-3 text-left">Convidado</th>
                    <th class="py-2 px-3 text-left">Remetente</th>
                    <th class="py-2 px-3 text-left">Status</th>
                    <th class="py-2 px-3 text-left">Criado em</th>
                  </tr>
                </thead>
                <tbody class="text-gray-700">
                  {#if convites.length === 0}
                    <tr><td class="py-4 px-3 text-gray-500" colspan="6">Nenhum convite para este pacto.</td></tr>
                  {/if}
                  {#each convites as c (c.id)}
                    <tr class="border-b">
                      <td class="py-2 px-3 text-xs">{c.token}</td>
                      <td class="py-2 px-3">{c.pactoNome}</td>
                      <td class="py-2 px-3 text-xs">{c.convidadoMunicipioId}</td>
                      <td class="py-2 px-3">{c.remetenteNome}</td>
                      <td class="py-2 px-3">{c.status}</td>
                      <td class="py-2 px-3">{c.createdAt ?? '-'}</td>
                    </tr>
                  {/each}
                </tbody>
              </table>
            </div>
          </div>
        </section>

        <section>
          <h2 class="text-lg font-semibold mb-2">Pactos existentes</h2>
          <div class="overflow-x-auto">
            <table class="min-w-full bg-white">
              <thead class="bg-gray-50">
                <tr>
                  <th class="py-3 px-4 text-left">Nome</th>
                  <th class="py-3 px-4 text-left">Criado em</th>
                  <th class="py-3 px-4 text-left">Membros</th>
                  <th class="py-3 px-4 text-left">Adicionar membros</th>
                </tr>
              </thead>
              <tbody class="text-gray-700">
                {#each pactos as p (p.id)}
                  <tr class="border-b align-top">
                    <td class="py-3 px-4 font-medium">{p.nome}</td>
                    <td class="py-3 px-4">{p.createdAt ?? '-'}</td>
                    <td class="py-3 px-4">
                      {#if p.membros && p.membros.length > 0}
                        <ul class="list-disc ml-5 space-y-1">
                          {#each p.membros as m}
                            <li class="flex items-center gap-2">
                              <span>{m.nome}</span>
                              <button class="text-red-600 text-xs" on:click={() => removerMembro(p.id, m.id)}>remover</button>
                            </li>
                          {/each}
                        </ul>
                      {:else}
                        <span class="text-gray-500">Sem membros</span>
                      {/if}
                    </td>
                    <td class="py-3 px-4">
                      <select class="border rounded p-2 w-full" bind:this={p._select} multiple size="4">
                        {#each municipios as m}
                          <option value={m.id}>{m.nome}</option>
                        {/each}
                      </select>
                      <button class="mt-2 bg-emerald-600 text-white px-3 py-1 rounded text-sm" on:click={() => adicionarMembros(p.id, Array.from(p._select.selectedOptions).map(o => o.value))}>Adicionar</button>
                    </td>
                  </tr>
                {/each}
              </tbody>
            </table>
          </div>
        </section>
        {/if}
      </div>
    </main>
  </div>
</div>
