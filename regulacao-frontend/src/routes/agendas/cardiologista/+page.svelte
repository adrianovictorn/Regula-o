<script lang="ts">
    import { getApi, putApi } from "$lib/api";
    import Menu2 from "$lib/Menu2.svelte";
    import UserMenu from "$lib/UserMenu.svelte";
    import { onMount } from "svelte";

    // Variáveis de estado para a UI
    let solicitacoesAgendadas = $state([]);
    let agendamentos = $state([]);
    let erro = $state(null);
    let carregando = $state(true);

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

            solicitacoesAgendadas = todasSolicitacoes.filter(solicitacao =>
                solicitacao.especialidades.some(esp =>
                    esp.status === 'AGENDADO' && esp.especialidadeSolicitada === 'CARDIOLOGISTA'
                )
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
            await carregarSolicitacoes();
        } catch (e) {
            alert(e.message);
        }
    }


    async function consultarData() {
      try{
        const res = await getApi('agendamentos');
        if(!res.ok){
          throw new Error('Erro ao receber dados de agendamento');
        }

        agendamentos =await res.json();
        return agendamentos;
      } catch(e){
        alert(e.message);
      }
    }

    // Carrega os dados quando o componente é montado
    onMount(() => {
        carregarSolicitacoes();
        consultarData();
    });

</script>

<svelte:head>
    <title>Dashboard Cardiologia</title>
</svelte:head>

<div class="flex h-screen bg-gray-100">
    <Menu2 activePage='/home' />
    <div class="flex-1 flex flex-col">
        <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
            <h1 class="text-xl font-semibold">Painel - Cardiologia</h1>
            <UserMenu />
        </header>

        <main class="flex-1 p-6 overflow-auto">
            {#if carregando}
                <p>Carregando painel...</p>
            {:else if erro}
                <p class="text-red-500">Erro: {erro}</p>
            {:else if solicitacoesAgendadas.length === 0}
                <p>Nenhuma solicitação de Cardiologia agendada encontrada.</p>
            {:else}
                <div class="space-y-4">
                    {#each solicitacoesAgendadas as s (s.id)}
                        <div class="rounded shadow bg-white p-4">
                            <p><b>Nome do Paciente:</b> {s.nomePaciente}</p>
                            <div class="flex space-x-4 text-sm">
                                <p><b>CPF:</b> {s.cpfPaciente}</p>
                                <p><b>USF:</b> {s.usfOrigem}</p>
                            </div>
                            
                            {#each s.especialidades as e (e.id)}
                                {#if e.especialidadeSolicitada === 'CARDIOLOGISTA' && e.status === 'AGENDADO'}
                                    <div class="mt-4 flex flex-row items-center justify-between p-2 bg-gray-50 rounded-lg">
                                        <span class="font-medium">{e.especialidadeSolicitada.replace('_', ' ')}</span>
                                        <div class="flex gap-2">
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