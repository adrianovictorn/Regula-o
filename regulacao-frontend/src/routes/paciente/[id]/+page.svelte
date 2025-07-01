<script lang="ts">
    import { onMount } from "svelte";
    import { page } from '$app/stores';
    import { goto } from '$app/navigation';
    import { getApi, postApi, putApi, deleteApi } from '$lib/api'; // Importando deleteApi
    import { opcoesEspecialidades } from '$lib/Especialidades.js';
    import Menu from "$lib/Menu.svelte";

    // --- Estado do Componente com Svelte 5 Runes ---
    let solicitacao = $state<any>(null);
    let agendamentos = $state<any[]>([]);
    let isLoading = $state(true);
    let error = $state<string | null>(null);

    // Variáveis para o formulário de edição
    let nomePaciente = $state('');
    let cpfPaciente = $state('');
    let cns = $state('');
    let datanascimento = $state('');
    let usfOrigem = $state('');
    let dataMalote = $state('');
    let observacoes = $state('');
    let telefone = $state('');

    // Objeto reativo para a nova especialidade a ser adicionada
    let novaEspecialidadeObj = $state({ especialidadeSolicitada: '', status: 'AGUARDANDO', prioridade: 'NORMAL' });
    
    // --- Lógica de Carregamento de Dados ---
    onMount(async () => {
        const id = $page.params.id; // Pega o ID da URL
        try {
            // Busca dados da solicitação e agendamentos em paralelo
            const [resSolicitacao, resAgendamentos] = await Promise.all([
                getApi(`solicitacoes/${id}`),
                getApi(`agendamentos/solicitacao/${id}`)
            ]);

            if (!resSolicitacao.ok) {
                throw new Error('Falha ao buscar os dados do paciente.');
            }
            
            // Popula o estado com os dados recebidos
            solicitacao = await resSolicitacao.json();
            if (resAgendamentos.ok) {
                agendamentos = await resAgendamentos.json();
            } else {
                console.warn("Não foi possível carregar os agendamentos.");
            }

            // Preenche os campos do formulário para edição
            nomePaciente = solicitacao.nomePaciente;
            cpfPaciente = solicitacao.cpfPaciente;
            cns = solicitacao.cns;
            datanascimento = solicitacao.datanascimento;
            usfOrigem = solicitacao.usfOrigem;
            dataMalote = solicitacao.dataMalote;
            observacoes = solicitacao.observacoes;
            telefone = solicitacao.telefone || ''; // Usa um valor padrão caso não exista

        } catch (e: any) {
            error = e.message;
        } finally {
            isLoading = false;
        }
    });

    // --- Funções de Ação (com API autenticada) ---

    // Atualiza dados do paciente (solicitação)
    async function salvarPaciente() {
        if (!solicitacao) return;

        const payload = {
            nomePaciente, cpfPaciente, cns, telefone,
            datanascimento, usfOrigem, dataMalote, observacoes
        };
        
        const res = await putApi(`solicitacoes/${solicitacao.id}`, payload);
        
        if (res.ok) {
            alert('Paciente atualizado com sucesso');
        } else {
            alert('Erro ao atualizar paciente');
        }
    }

    // Adiciona nova especialidade à solicitação
    async function adicionarEspecialidade() {
        if (!novaEspecialidadeObj.especialidadeSolicitada) {
            alert('Selecione uma especialidade para adicionar.');
            return;
        }
        if (!solicitacao) return;

        const res = await postApi(`solicitacoes/${solicitacao.id}/especialidades`, novaEspecialidadeObj);
        
        if (res.ok) {
            alert('Especialidade adicionada com sucesso!');
            location.reload(); // Recarrega a página para refletir as mudanças
        } else {
            alert('Erro ao adicionar especialidade');
        }
    }

    // NOVA FUNÇÃO: Remover especialidade
    async function removerEspecialidade(especialidadeId: number) {
        if (!confirm('Tem certeza que deseja remover esta especialidade?')) {
            return;
        }
        try {
            const res = await deleteApi(`solicitacoes/especialidades/${especialidadeId}`); // Chame o endpoint DELETE
            if (res.ok) {
                alert('Especialidade removida com sucesso!');
                // Atualiza a lista de especialidades localmente para refletir a mudança sem recarregar a página.
                // Isso é importante para Svelte 5 com `$state` para manter a reatividade.
                solicitacao.especialidades = solicitacao.especialidades.filter((e: any) => e.id !== especialidadeId);
            } else if (res.status === 404) {
                 alert('Especialidade não encontrada.');
            }
            else {
                alert('Erro ao remover especialidade.');
            }
        } catch (e: any) {
            alert(`Erro na requisição: ${e.message}`);
        }
    }

    // Valores derivados para exibir na tela de forma reativa
    let historico = $derived(solicitacao?.especialidades || []);
    let especPendentes = $derived(historico.filter((e: any) => e.status === 'AGUARDANDO'));

</script>

<div class="flex h-screen bg-gray-100">
 <Menu activePage = "/paciente"/>

 <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white p-4 flex justify-between items-center">
        {#if isLoading}
            <h1 class="text-xl font-semibold">Carregando Dados do Paciente...</h1>
        {:else if solicitacao}
            <h1 class="text-xl font-semibold">Dados do Paciente</h1>
            <div><span class="font-bold">CPF:</span> {solicitacao.cpfPaciente}</div>
        {/if}
    </header>

    <main class="p-6 overflow-auto space-y-6">
        {#if isLoading}
            <div class="text-center p-10">Carregando...</div>
        {:else if error}
            <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4" role="alert">
                <p class="font-bold">Erro ao carregar</p>
                <p>{error}</p>
            </div>
        {:else if solicitacao}
            <section class="bg-white rounded-lg shadow p-6">
                <h2 class="text-lg font-bold text-emerald-800 mb-4">Editar Paciente</h2>
                <div class="grid grid-cols-1 md:grid-cols-6 gap-6">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Nome</label>
                        <input type="text" bind:value={nomePaciente} class="w-full border border-gray-300 rounded p-2" />
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Cartão do SUS</label>
                        <input type="text" bind:value={cns} class="w-full border border-gray-300 rounded p-2" />
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">USF Origem</label>
                        <input type="text" bind:value={usfOrigem} class="w-full border border-gray-300 rounded p-2" />
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Data de Nascimento</label>
                        <input type="date" bind:value={datanascimento} class="w-full border border-gray-300 rounded p-2" />
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Data Recebimento</label>
                        <input type="date" bind:value={dataMalote} class="w-full border border-gray-300 rounded p-2" />
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Telefone</label>
                        <input type="text" bind:value={telefone} class="w-full border border-gray-300 rounded p-2" />
                    </div>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-1 gap-4 mt-4">
                    <div class="md:col-span-2">
                        <label class="block text-sm font-medium text-gray-700 mb-1">Observações</label>
                        <textarea bind:value={observacoes} rows="3" class="w-full border border-gray-300 rounded p-2"></textarea>
                    </div>
                </div>
                <button on:click={salvarPaciente} class="mt-4 bg-emerald-800 text-white px-6 py-2 rounded hover:bg-emerald-900">Salvar Alterações</button>
            </section>
            
            <section class="bg-gray-50 p-4 sm:p-6 rounded-lg">
                <h2 class="text-xl font-bold text-emerald-800 mb-4">Histórico de Agendamentos</h2>
                {#if agendamentos.length === 0}
                    <div class="text-center py-8 bg-white rounded-md shadow-sm">
                        <p class="text-gray-500">Nenhum agendamento encontrado.</p>
                    </div>
                {:else}
                    <div class="space-y-4">
                        {#each agendamentos as ag}
                            <div class="bg-white border border-gray-200 rounded-lg shadow-sm overflow-hidden">
                                <div class="p-4 bg-gray-50 border-b">
                                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-3">
                                        <div>
                                            <p class="text-xs font-semibold text-gray-600 uppercase tracking-wider">Local</p>
                                            <p class="text-base font-medium text-gray-900">{ag.localAgendado.replace(/_/g, ' ')}</p>
                                        </div>
                                        <div>
                                            <p class="text-xs font-semibold text-gray-600 uppercase tracking-wider">Data</p>
                                            <p class="text-base font-medium text-gray-900">{ag.dataAgendada}</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="p-4">
                                    <h4 class="text-sm font-semibold text-gray-700 mb-2">Itens Agendados:</h4>
                                    <ul class="space-y-2">
                                        {#each historico.filter(h => h.agendamentoId === ag.id) as h}
                                            <li class="flex justify-between items-center text-sm">
                                                <span class="text-gray-800">{h.especialidadeSolicitada.replace(/_/g, ' ')}</span>
                                                <span class="px-2 py-0.5 rounded-full text-xs font-semibold bg-blue-100 text-blue-800">{h.status}</span>
                                            </li>
                                        {/each}
                                    </ul>
                                </div>
                            </div>
                        {/each}
                    </div>
                {/if}
            </section>

            <section class="bg-white rounded-lg shadow p-6">
                <h2 class="text-lg font-bold text-emerald-800 mb-4">Especialidades Pendentes</h2>
                 {#if especPendentes.length > 0}
                    <div class="border border-gray-200 rounded-md">
                        <ul class="divide-y divide-gray-200">
                            {#each especPendentes as e (e.id)}
                                <li class="p-3 flex justify-between items-center hover:bg-gray-50 transition-colors">
                                    <span class="text-gray-800 font-medium">{e.especialidadeSolicitada.replace(/_/g, ' ')}</span>
                                    <div class="flex items-center space-x-2">
                                        <span class="px-3 py-1 text-xs font-semibold rounded-full bg-orange-100 text-orange-800">{e.status}</span>
                                        <button 
                                            on:click={() => removerEspecialidade(e.id)} 
                                            class="p-1 rounded-full bg-red-100 text-red-600 hover:bg-red-200 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
                                            title="Remover Especialidade"
                                        >
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
                                            </svg>
                                        </button>
                                    </div>
                                </li>
                            {/each}
                        </ul>
                    </div>
                 {:else}
                    <div class="text-center py-6 px-4 bg-gray-50 rounded-md border border-dashed">
                        <p class="text-gray-500">Nenhuma especialidade pendente.</p>
                    </div>
                 {/if}
            </section>

            <section class="bg-white rounded-lg shadow p-6">
                <div class="space-y-4 p-4 border border-gray-200 rounded-lg">
                    <h3 class="text-md font-semibold text-gray-800 mb-2">Adicionar Nova Especialidade</h3>
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Especialidade</label>
                            <select bind:value={novaEspecialidadeObj.especialidadeSolicitada} class="w-full border border-gray-300 rounded p-2" required>
                                <option value="" disabled>Selecione...</option>
                                <optgroup label="Consultas">
                                    {#each opcoesEspecialidades.especialidadesMedicas as opcoes}
                                        <option value={opcoes.value}>{opcoes.label}</option>
                                    {/each}
                                </optgroup>
                                <optgroup label="Exames e Procedimentos">
                                    {#each opcoesEspecialidades.examesEProcedimentos as opcoes}
                                        <option value={opcoes.value}>{opcoes.label}</option>
                                    {/each}
                                </optgroup>
                            </select>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
                            <select bind:value={novaEspecialidadeObj.status} class="w-full border border-gray-300 rounded p-2">
                                <option value="AGUARDANDO">Aguardando</option>
                                <option value="AGENDADO">Agendado</option>
                            </select>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Prioridade</label>
                            <select bind:value={novaEspecialidadeObj.prioridade} class="w-full border border-gray-300 rounded p-2">
                                <option value="NORMAL">Normal</option>
                                <option value="URGENTE">Urgente</option>
                                <option value="EMERGENCIA">Emergência</option>
                            </select>
                        </div>
                    </div>
                    <button on:click={adicionarEspecialidade} class="mt-4 bg-emerald-800 text-white px-6 py-2 rounded hover:bg-emerald-900">Adicionar</button>
                </div>
            </section>
        {/if}
    </main>
 </div>
</div>