<script lang="ts">
    import { getApi, putApi } from "$lib/api";
    import Menu2 from "$lib/Menu2.svelte";
    import UserMenu from "$lib/UserMenu.svelte";
    import { onMount } from "svelte";

    // Variáveis de estado
    let solicitacoesDeHoje = $state([]);
    let erro = $state(null);
    let carregando = $state(true);

    // Função para obter a data de hoje no formato YYYY-MM-DD
    function getHojeFormatado() {
        const hoje = new Date();
        const ano = hoje.getFullYear();
        const mes = String(hoje.getMonth() + 1).padStart(2, '0');
        const dia = String(hoje.getDate()).padStart(2, '0');
        return `${ano}-${mes}-${dia}`;
    }

    // Função para carregar e filtrar as solicitações
    async function carregarSolicitacoes() {
        carregando = true;
        erro = null;
        try {
            const res = await getApi('solicitacoes');
            if (!res.ok) {
                throw new Error('Falha ao carregar as solicitações');
            }
            const todasSolicitacoes = await res.json();
            const hoje = getHojeFormatado();

            // Filtra as solicitações pela data de hoje e pela especialidade
            solicitacoesDeHoje = todasSolicitacoes.filter(solicitacao =>
                solicitacao.especialidades.some(especialidade => {
                    // Encontra o agendamento correspondente a esta especialidade
                    const agendamento = solicitacao.agendamentos.find(ag => ag.id === especialidade.agendamentoId);
                    
                    return (
                        especialidade.status === 'AGENDADO' &&
                        especialidade.especialidadeSolicitada === 'CARDIOLOGISTA' &&
                        agendamento && agendamento.dataAgendada === hoje
                    );
                })
            );
        } catch (e) {
            erro = e.message;
        } finally {
            carregando = false;
        }
    }

    // Função para atualizar o status da especialidade
    async function confirmarSolicitacao(especialidadeId: number, novoStatus: 'REALIZADO' | 'CANCELADO') {
        try {
            const res = await putApi(`especialidades/${especialidadeId}`, { status: novoStatus });
            if (!res.ok) {
                throw new Error('Erro ao atualizar o status');
            }
            // Recarrega a lista para refletir a mudança
            alert(`Confirmar`)
            await carregarSolicitacoes();
        } catch (e) {
            alert(e.message);
        }
    }
    
    // Função para formatar a data para exibição (DD/MM/YYYY)
    function formatarData(dataString) {
        if (!dataString) return 'Data não informada';
        const [ano, mes, dia] = dataString.split('-');
        return `${dia}/${mes}/${ano}`;
    }

    // Carrega os dados quando o componente é montado
    onMount(() => {
        carregarSolicitacoes();
    });

</script>

<svelte:head>
    <title>Dashboard Cardiologia - Hoje</title>
</svelte:head>

<div class="flex h-screen bg-gray-100">
    <Menu2 activePage='/home' />
    <div class="flex-1 flex flex-col">
        <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
            <h1 class="text-xl font-semibold">Painel - Cardiologia (Agendados para Hoje)</h1>
            <UserMenu />
        </header>

        <main class="flex-1 p-6 overflow-auto">
            {#if carregando}
                <p class="text-center">Carregando painel...</p>
            {:else if erro}
                <p class="text-red-500 text-center">Erro: {erro}</p>
            {:else if solicitacoesDeHoje.length === 0}
                <p class="text-center">Nenhuma solicitação de Cardiologia agendada para hoje.</p>
            {:else}
                <div class="space-y-4">
                    {#each solicitacoesDeHoje as s (s.id)}
                        <div class="rounded shadow bg-white p-4">
                            <p><b>Nome do Paciente:</b> {s.nomePaciente}</p>
                            <div class="flex space-x-4 text-sm">
                                <p><b>CPF: </b> {s.cpfPaciente}</p>
                                <p><b>CNS: </b>{s.cns}</p>
                                <p><b>USF: </b> {s.usfOrigem}</p>
                            </div>
                            
                            {#each s.especialidades as e (e.id)}
                                {@const agendamento = s.agendamentos.find(ag => ag.id === e.agendamentoId)}
                                {#if e.especialidadeSolicitada === 'CARDIOLOGISTA' && e.status === 'AGENDADO' && agendamento?.dataAgendada === getHojeFormatado()}
                                    <div class="mt-4 flex flex-col sm:flex-row items-start sm:items-center justify-between p-3 bg-gray-50 rounded-lg gap-3">
                                        <div>
                                            <span class="font-medium">{e.especialidadeSolicitada.replace('_', ' ')}</span>
                                            <p class="text-sm text-blue-600 font-semibold">
                                                Data: {formatarData(agendamento.dataAgendada)}
                                            </p>
                                        </div>
                                        <div class="flex gap-2 flex-shrink-0">
                                            <button on:click={() => confirmarSolicitacao(e.id, 'REALIZADO')} class="rounded-lg bg-green-600 hover:bg-green-700 text-white px-3 py-1">Confirmar</button>
                                            <button on:click={() => confirmarSolicitacao(e.id, 'CANCELADO')} class="rounded-lg bg-red-600 hover:bg-red-700 text-white px-3 py-1">Faltou</button>
                                        </div>
                                    </div>
                                {/if}
                            {/each}
                        </div>
                    {/each}
                </div>
            {/if}
        </main>
    </div>
</div>