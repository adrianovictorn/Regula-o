<script lang="ts">
    import { getApi } from "$lib/api";
    import RoleBasedMenu from "$lib/RoleBasedMenu.svelte";
    import { onMount } from "svelte";

    interface CidadeSummaryDTO{
        id: number
        nome: string
    }

    interface SolicitacaoSummaryDTO{
        id: number
        nomePaciente: string 
        cpf: string
    }

    interface TransporteSummaryDTO {
        id: number 
        vagas: number
        nome: string
    }

    interface LocalAgendamentoSummaryDTO{
        id: number
        nomeLocal: string
        endereco: string 
        numero: string

    }


    interface AgendamentoTransporte{
        localAgendamento: LocalAgendamentoSummaryDTO[]
        cidade: CidadeSummaryDTO
        transporte: TransporteSummaryDTO
        status: string
        solicitacao: SolicitacaoSummaryDTO[]
        data: string | number

    }


    let agendamentoTransporteList = $state<AgendamentoTransporte[]>([])
    let toggleDados = $state<boolean[]> ([])
    async function carregarAgendamentoTransporte(){
        try{
            const res = await getApi("agendamento/transporte")

            if(!res.ok){
                alert("Erro ao receber de agendamento transporte !")
            }

            const data = await res.json()
            agendamentoTransporteList = data;

            console.log(data)

            toggleDados = new Array(agendamentoTransporteList.length).fill(false)
        } catch{

        }
    }

    onMount(() => {
        carregarAgendamentoTransporte()
    })

    function formatarData (d: string | number){
        if(Array.isArray(d) && d.length === 3){
            const [y,m,day] = d;
            const dt = new Date(y, m-1 ,day);
            return dt.toLocaleDateString("pt-BR")
        }
        const dt = new Date(d as string);
        return isNaN(dt.getTime()) ? String(d) : dt.toLocaleDateString("pt-BR")
    }

    function exibirDados(index: number){
        toggleDados = toggleDados.map((v,i) =>(i === index ? !v : v))
    }


</script>

<div class="flex min-h-screen bg-gray-100">
    <RoleBasedMenu/> 
   
    <div class="flex flex-1 flex-col ">
        <header class="bg-emerald-700 py-4 px-4">
            <h1 class="text-white font-semibold">Consultar Agendamentos</h1>
        </header>

        <main class="flex-1 overflow-auto">
            <section class="m-6 p-6 bg-white">
                <h2 class="text-emerald-700 font-semibold text-lg mb-2">Agendamentos por Data</h2>

                <div class="bg-gray-100 p-3 rounded">
                    {#each agendamentoTransporteList as agT, i (i)}

                        <div>
                            <div class="bg-white grid grid-cols-4 gap-5 border rounded p-4 shadow align-middle">
                                <div class="flex flex-col">
                                    <h2 class="font-semibold">Data Agendamento:  </h2>
                                    <span>{formatarData(agT.data)}</span>
                                   
                                </div>
                                <div class="flex flex-col justify-center text-center">
                                    <h2 class="font-semibold">Total de Pacientes Agendados:</h2> 
                                    <span class="">{agT.solicitacao.length}</span>
                                </div>
                                <div class="flex flex-col items-center">
                                    <h2 class="font-semibold text-gray-800">Cidade</h2>
                                     <span>{agT.cidade.nome}</span>
                                </div>

                                <button type="button" class="justify-self-end" onclick={() => exibirDados(i)} aria-expanded={toggleDados[i]}>
                                    <svg viewBox="0 0 20 20" fill="currentColor" data-slot="icon" aria-hidden="true" class="-mr-1 size-5 text-gray-400">
                                    <path d="M5.22 8.22a.75.75 0 0 1 1.06 0L10 11.94l3.72-3.72a.75.75 0 1 1 1.06 1.06l-4.25 4.25a.75.75 0 0 1-1.06 0L5.22 9.28a.75.75 0 0 1 0-1.06Z" clip-rule="evenodd" fill-rule="evenodd" />
                                    </svg>
                                </button>
                               
                                
                            </div>
                            
                            {#if !toggleDados[i]}
                                <section class="border">
                                    <h2 class="text-lg text-emerald-700 font-bold mb-2 m-4">Detalhes do Agendamento</h2>

                                    <div class="grid grid-cols-1 gap-4 m-4">
                                        <h2 class="font-semibold text-sm">Dados do Transporte</h2>
                                        
                                        <div class="grid grid-cols-2 gap-4">
                                            <span>Identicação:{agT.transporte.nome}</span>
                                            <span>Número de Vagas:{agT.transporte.vagas}</span>
                                        </div>

                                        <h2 class="font-semibold text-sm">Pontos de Parada</h2>
                                        <div>
                                            <span>Hospital/Ponto de Parada: {agT.localAgendamento.map(loc => loc.nomeLocal)}</span>
                                        </div>

                                        <h2 class="font-semibold text-sm">Pacientes</h2>
                                        <div class="flex flex-col">
                                            <p>{agT.solicitacao.map(s => s.nomePaciente)}</p>
                                        </div>


                                        
                                    </div>
                                </section>
                            {/if}
                        </div>
                    {/each}
                </div>
            </section>
        </main>
    </div>

</div>