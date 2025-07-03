<script lang="ts">
    import { onMount } from "svelte";
    import { page } from '$app/stores';
    import { goto } from '$app/navigation';
    import { getApi, postApi, putApi, deleteApi } from '$lib/api';
    import { opcoesEspecialidades } from '$lib/Especialidades.js';
    import Menu from "$lib/Menu.svelte";

    // --- Estado do Componente com Svelte 5 Runes ---
    let solicitacao = $state<any>(null);
    let agendamentos = $state<any[]>([]);
    let especialidades = $state<any[]>([]); // Estado unificado para especialidades
    let isLoading = $state(true);
    let error = $state<string | null>(null);

    function getNomeEspecialidade(valorEnum: string): string {
        const todasAsOpcoes = [
            ...opcoesEspecialidades.especialidadesMedicas,
            ...opcoesEspecialidades.examesEProcedimentos
        ];
        const opcao = todasAsOpcoes.find(opt => opt.value === valorEnum);
        return opcao ? opcao.label : valorEnum.replace(/_/g, ' ');
    }
    
    function formatarData(dataString: string | null): string {
        if (!dataString) return 'N/A';
        const data = new Date(dataString);
        return data.toLocaleDateString('pt-BR', { timeZone: 'UTC' });
    }

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
        const id = $page.params.id;
        try {
            const [resSolicitacao, resAgendamentos] = await Promise.all([
                getApi(`solicitacoes/${id}`),
                getApi(`agendamentos/solicitacao/${id}`),
            ]);

            if (!resSolicitacao.ok) {
                const errorData = await resSolicitacao.text();
                throw new Error(`Falha ao buscar os dados do paciente: ${errorData}`);
            }

            solicitacao = await resSolicitacao.json();
            especialidades = solicitacao.especialidades || [];

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
            telefone = solicitacao.telefone || '';

        } catch (e: any) {
            error = e.message;
        } finally {
            isLoading = false;
        }
    });

    // --- Funções de Ação ---

    async function salvarPaciente() {
        if (!solicitacao) return;
        const payload = { nomePaciente, cpfPaciente, cns, telefone, datanascimento, usfOrigem, dataMalote, observacoes };
        const res = await putApi(`solicitacoes/${solicitacao.id}`, payload);
        if (res.ok) {
            alert('Paciente atualizado com sucesso');
        } else {
            alert('Erro ao atualizar paciente');
        }
    }

    async function adicionarEspecialidade() {
        if (!novaEspecialidadeObj.especialidadeSolicitada) {
            alert('Selecione uma especialidade para adicionar.');
            return;
        }
        if (!solicitacao) return;

        const res = await postApi(`solicitacoes/${solicitacao.id}/especialidades`, novaEspecialidadeObj);
        if (res.ok) {
            alert('Especialidade adicionada com sucesso!');
            location.reload();
        } else {
            alert('Erro ao adicionar especialidade');
        }
    }

    async function removerEspecialidade(especialidadeId: number) {
        if (!confirm('Tem certeza que deseja remover esta especialidade?')) return;
        try {
            const res = await deleteApi(`solicitacoes/especialidades/${especialidadeId}`);
            if (res.ok) {
                alert('Especialidade removida com sucesso!');
                location.reload();
            } else {
                const errorText = await res.text();
                alert(`Erro ao remover especialidade: ${res.status} - ${errorText}`);
            }
        } catch (e: any) {
            alert(`Erro na requisição: ${e.message}`);
        }
    }

    async function removerAgendamento(agendamentoId: number) {
        if (!confirm('Tem certeza que deseja remover este agendamento e desvincular os itens associados?')) return;
        try {
            const res = await deleteApi(`agendamentos/${agendamentoId}`);
            if (res.ok) {
                alert('Agendamento removido com sucesso!');
                location.reload();
            } else {
                const errorText = await res.text();
                alert(`Erro ao remover agendamento: ${res.status} - ${errorText}`);
            }
        } catch (e: any) {
            alert(`Erro na requisição: ${e.message}`);
        }
    }

    async function handlePrioridadeChange(especialidadeId: number, event: Event) {
        const novaPrioridade = (event.currentTarget as HTMLSelectElement).value;
        if (!confirm(`Tem certeza que deseja alterar a prioridade para ${novaPrioridade}?`)) {
            location.reload();
            return;
        }
        try {
            const res = await putApi(`especialidades/${especialidadeId}`, { prioridade: novaPrioridade });
            if (res.ok) {
                alert('Prioridade atualizada com sucesso!');
                const index = especialidades.findIndex(e => e.id === especialidadeId);
                if (index !== -1) {
                    especialidades[index].prioridade = novaPrioridade;
                }
            } else {
                const errorText = await res.text();
                alert(`Erro ao atualizar prioridade: ${res.status} - ${errorText}`);
                location.reload();
            }
        } catch (e: any) {
            alert(`Erro na requisição: ${e.message}`);
            location.reload();
        }
    }

    // Valores derivados para exibir na tela de forma reativa
    let historico = $derived(especialidades);
    let especPendentes = $derived(historico.filter((e: any) => e.status === 'AGUARDANDO'));

</script>

<svelte:head>
    <title>Paciente - {solicitacao?.nomePaciente || 'Carregando...'}</title>
</svelte:head>

<div class="flex h-screen bg-gray-100">
    <Menu activePage="/paciente" />

    <div class="flex-1 flex flex-col">
        <header class="bg-emerald-700 text-white p-4 flex justify-between items-center shadow-md">
            {#if isLoading}
                <h1 class="text-xl font-semibold">Carregando Dados do Paciente...</h1>
            {:else if solicitacao}
                <h1 class="text-xl font-semibold">Dados do Paciente</h1>
                <div><span class="font-bold">CPF:</span> {solicitacao.cpfPaciente}</div>
            {/if}
        </header>

        <main class="p-4 md:p-6 lg:p-8 overflow-auto space-y-6">
            {#if isLoading}
                <div class="text-center p-10">Carregando...</div>
            {:else if error}
                <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4" role="alert">
                    <p class="font-bold">Erro ao carregar</p>
                    <p>{error}</p>
                </div>
            {:else if solicitacao}
                <!-- Seção Editar Paciente -->
                <section class="bg-white rounded-lg shadow p-6">
                    <h2 class="text-lg font-bold text-emerald-800 mb-4 border-b pb-2">Editar Paciente</h2>
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Nome</label>
                            <input type="text" bind:value={nomePaciente} class="w-full border-gray-300 rounded-md shadow-sm focus:border-emerald-500 focus:ring-emerald-500" />
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Cartão do SUS</label>
                            <input type="text" bind:value={cns} class="w-full border-gray-300 rounded-md shadow-sm focus:border-emerald-500 focus:ring-emerald-500" />
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">USF Origem</label>
                            <input type="text" bind:value={usfOrigem} class="w-full border-gray-300 rounded-md shadow-sm focus:border-emerald-500 focus:ring-emerald-500" />
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Data de Nascimento</label>
                            <input type="date" bind:value={datanascimento} class="w-full border-gray-300 rounded-md shadow-sm focus:border-emerald-500 focus:ring-emerald-500" />
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Data Recebimento</label>
                            <input type="date" bind:value={dataMalote} class="w-full border-gray-300 rounded-md shadow-sm focus:border-emerald-500 focus:ring-emerald-500" />
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Telefone</label>
                            <input type="text" bind:value={telefone} class="w-full border-gray-300 rounded-md shadow-sm focus:border-emerald-500 focus:ring-emerald-500" />
                        </div>
                    </div>
                    <div class="mt-4">
                        <label class="block text-sm font-medium text-gray-700 mb-1">Observações</label>
                        <textarea bind:value={observacoes} rows="3" class="w-full border-gray-300 rounded-md shadow-sm focus:border-emerald-500 focus:ring-emerald-500"></textarea>
                    </div>
                    <button on:click={salvarPaciente} class="mt-4 bg-emerald-700 text-white px-6 py-2 rounded-md hover:bg-emerald-800 transition-colors shadow">Salvar Alterações</button>
                </section>
                
                <!-- Seção Histórico de Agendamentos -->
                <section class="bg-white rounded-lg shadow p-6">
                    <h2 class="text-lg font-bold text-emerald-800 mb-4 border-b pb-2">Histórico de Agendamentos</h2>
                    {#if agendamentos.length === 0}
                        <div class="text-center py-8 bg-gray-50 rounded-md">
                            <p class="text-gray-500">Nenhum agendamento encontrado.</p>
                        </div>
                    {:else}
                        <div class="space-y-4">
                            {#each agendamentos as ag}
                                <div class="bg-white border border-gray-200 rounded-lg shadow-sm overflow-hidden">
                                    <div class="p-4 bg-gray-50 border-b flex justify-between items-start gap-4">
                                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-2 flex-grow">
                                            <div>
                                                <p class="text-xs font-semibold text-gray-500 uppercase tracking-wider">Local</p>
                                                <p class="text-base font-medium text-gray-900">{ag.localAgendado.replace(/_/g, ' ')}</p>
                                            </div>
                                            <div>
                                                <p class="text-xs font-semibold text-gray-500 uppercase tracking-wider">Data</p>
                                                <p class="text-base font-medium text-gray-900">{formatarData(ag.dataAgendada)}</p>
                                            </div>
                                        </div>
                                        <button on:click={() => removerAgendamento(ag.id)}
                                            class="p-2 rounded-full text-gray-400 hover:bg-red-100 hover:text-red-600 transition-colors flex-shrink-0"
                                            title="Remover Agendamento e Itens Associados">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                                <path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                            </svg>
                                        </button>
                                    </div>
                                    <div class="p-4">
                                        <h4 class="text-sm font-semibold text-gray-700 mb-2">Itens Agendados:</h4>
                                        <ul class="space-y-2">
                                            {#each historico.filter(h => h.agendamentoId === ag.id) as h}
                                                <li class="flex justify-between items-center text-sm">
                                                    <span class="text-gray-800">{getNomeEspecialidade(h.especialidadeSolicitada)}</span>
                                                    <div class="flex items-center space-x-3">
                                                        <span class="px-2 py-0.5 rounded-full text-xs font-semibold bg-blue-100 text-blue-800">{h.status}</span>
                                                        <button on:click={() => removerEspecialidade(h.id)} 
                                                            class="p-2 rounded-full text-gray-400 hover:bg-red-100 hover:text-red-600 transition-colors"
                                                            title="Remover Especialidade Agendada">
                                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                                                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
                                                            </svg>
                                                        </button>
                                                    </div>
                                                </li>
                                            {/each}
                                        </ul>
                                    </div>
                                </div>
                            {/each}
                        </div>
                    {/if}
                </section>

                <!-- Seção Especialidades Pendentes -->
                <section class="bg-white rounded-lg shadow p-6">
                    <h2 class="text-lg font-bold text-emerald-800 mb-4 border-b pb-2">Especialidades Pendentes</h2>
                     {#if especPendentes.length > 0}
                        <div class="border border-gray-200 rounded-md">
                            <ul class="divide-y divide-gray-200">
                                {#each especPendentes as e (e.id)}
                                    <li class="p-3 flex justify-between items-center hover:bg-gray-50">
                                        <span class="text-gray-800 font-medium">{getNomeEspecialidade(e.especialidadeSolicitada)}</span>
                                        <div class="flex items-center space-x-3">
                                            <span class="px-3 py-1 text-xs font-semibold rounded-full bg-yellow-100 text-yellow-800">{e.status}</span>
                                            <select bind:value={e.prioridade} 
                                                    on:change={(event) => handlePrioridadeChange(e.id, event)}
                                                    class="text-sm border-gray-300 rounded-md shadow-sm focus:ring-emerald-500 focus:border-emerald-500 transition duration-150 ease-in-out py-1">
                                                <option value="NORMAL">Normal</option>
                                                <option value="URGENTE">Urgente</option>
                                                <option value="EMERGENCIA">Emergência</option>
                                            </select>
                                            <button on:click={() => removerEspecialidade(e.id)} 
                                                class="p-2 rounded-full text-gray-400 hover:bg-red-100 hover:text-red-600 transition-colors"
                                                title="Remover Especialidade">
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

                <!-- Seção Adicionar Nova Especialidade -->
                <section class="bg-white rounded-lg shadow p-6">
                     <h2 class="text-lg font-bold text-emerald-800 mb-4 border-b pb-2">Adicionar Nova Especialidade</h2>
                    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <div class="lg:col-span-2">
                            <label class="block text-sm font-medium text-gray-700 mb-1">Especialidade</label>
                            <select bind:value={novaEspecialidadeObj.especialidadeSolicitada} class="w-full border-gray-300 rounded-md shadow-sm focus:border-emerald-500 focus:ring-emerald-500" required>
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
                            <label class="block text-sm font-medium text-gray-700 mb-1">Prioridade</label>
                            <select bind:value={novaEspecialidadeObj.prioridade} class="w-full border-gray-300 rounded-md shadow-sm focus:border-emerald-500 focus:ring-emerald-500">
                                <option value="NORMAL">Normal</option>
                                <option value="URGENTE">Urgente</option>
                                <option value="EMERGENCIA">Emergência</option>
                            </select>
                        </div>
                    </div>
                    <button on:click={adicionarEspecialidade} class="mt-4 bg-emerald-700 text-white px-6 py-2 rounded-md hover:bg-emerald-800 transition-colors shadow">Adicionar</button>
                </section>
            {/if}
        </main>
    </div>
</div>
