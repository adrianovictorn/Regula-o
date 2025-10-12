<script lang="ts">
    import { getApi, postApi } from "$lib/api";
    import RoleBasedMenu from "$lib/RoleBasedMenu.svelte";
    import UserMenu from "$lib/UserMenu.svelte";
    import { onMount } from "svelte";

    interface Cidade{
        id: number
        nomeCidade: string
        codigoIBGE: string
        cep: string
    }

    interface Transporte {
        id: number
        nomeVeiculo: string
        vagas: number
        tipoVeiculo: string
        modelo: string 
    }

    interface LocalAgendamento{
        id: number
        nomeLocal: string
        endereco: string
        numero: string
        cidadeNome: string
    }

    interface Solicitacao{
        id: number
        nomePaciente: string
        cpfPaciente: string
        cns: string
        datanascimento: string
        usfOrigem: string
        telefone: string
    }


    let cidades = $state<Cidade[]>([]);
    let transportes = $state<Transporte[]>([]);
    let solicitacoes = $state<Solicitacao[]>([]);
    let locaisagendamento = $state<LocalAgendamento[]>([]);
    
    let transporteAgendaId = $state<number> ()
    let cidadeAgendaId = $state<number> ()
    let localAgendamento = $state<string[]>([""])
    let pacienteSelecionados =  $state<string[]>([""]);
    let dataAgendamento = $state<string>()
    

    async function carregarCidades() {
        try{
            const res = await getApi("cidades");

            if(!res.ok){
                alert('Erro ao receber dados de cidade !')
            }

            const data = (await res.json()) as Cidade[]
            cidades = data;
        } catch{
            alert('Erro ao receber dados do servidor')
        }
    }

    async function carregarTransportes() {
        try{
            const res = await getApi("transporte")

            if(!res.ok){
                alert("Erro ao receber dados de transporte! ")
            }

            const data = (await res.json()) as Transporte[];
            transportes = data 
        } catch{
            alert("Erro ao se conectar ao servidor !")
        }
    }

    async function carregarLocaisAgendamento() {
        try{
            const res = await getApi("local/agendamento")
            
            if(!res.ok){
                alert('Erro ao buscar dados ao endpoint')
            }

            const data = (await res.json()) as LocalAgendamento[];
            locaisagendamento = data
        } catch{
            alert('Erro ao se conectar ao servidor !')
        }
    }

    async function carregarSolicitacoes() {
        
        try{
            const res = await getApi("solicitacoes")

            if(!res.ok){
                alert("Erro ao buscar dados do endpoint")
            }

            const data = (await res.json()) as Solicitacao[];
            solicitacoes = data
        }catch{
            alert("Erro ao se conectar ao servidor !")
        }
    }

    onMount(() => {
        carregarCidades();
        carregarLocaisAgendamento();
        carregarSolicitacoes();
        carregarTransportes();
    })

    async function cadastrarAgendamentoTransporte(e: Event){
        e.preventDefault();
        const payload = { 
            transporteId : transporteAgendaId,
            cidadeId: cidadeAgendaId,
            data: dataAgendamento,
            novasSolicitacoes: pacienteSelecionados.filter((v) => v !== "").map((v) => Number(v)),
            locaisAgendamento: localAgendamento.filter((v) => v !== "").map((v) => Number(v))

        }

        try{
            const res = await postApi("agendamento/transporte/agendar/solicitacoes", payload)

            if(!res.ok){
                alert("Erro ao enviar dados ao servidor !")
            }

            const data = await res.json();
            console.log(data)

        }catch{
            alert("Erro ao se conectar ao servidor !")
        }
    }


    function adicionarPaciente(){
       pacienteSelecionados = [...pacienteSelecionados,""]
    }

    function removerPaciente(i: number){
        pacienteSelecionados = pacienteSelecionados.toSpliced(i,1)
    }

    function adicionarLocalAgendamento(){
        localAgendamento = [...localAgendamento, ""]
    }

    function removerLocalAgendamento(i: number){
        localAgendamento = localAgendamento.toSpliced(i,1)
    }

    
</script>

<div class="flex min:h-screen bg-white">
    <RoleBasedMenu/>
    
    <div class="flex flex-1 flex-col">
        <header class="bg-emerald-700 text-white font-semibold p-4 text-shadow flex items-center justify-between ">
            <h1>Agendamento de Transporte</h1>

            <UserMenu/>
        </header>

        <main class="flex-1 overflow bg-gray-100"> 

            <div class= "m-6 bg-white rounded-lg shadow-lg p-6 ">

                <form class="grid grid-cols-1 gap-4" onsubmit={cadastrarAgendamentoTransporte} >
                    <div class="grid grid-cols-3 gap-2">
                        <div class="flex flex-col ">
                            <label for="data" class="text-gray-700 font-medium text-sm mb-1">Data do Agendamento</label>
                            <input type="date" id="data" class="border border-gray-300 rounded-lg py-2" bind:value={dataAgendamento}>
                        </div>
                        <div class="flex flex-col">
                            <label for="cidade" class="text-gray-700 font-medium text-sm mb-1">Cidade</label>
                            <select  id="cidade" class="rounded-lg border-gray-300 py-2" bind:value={cidadeAgendaId}>
                                <option value="0">Selecione...</option>
                                {#each cidades  as  cidade}
                                    <option value={cidade.id}>{cidade.nomeCidade} - {cidade.codigoIBGE}</option>
                                {/each}
                            </select>
                        </div>

                        
                        <div class="flex flex-col">
                            <label for="transporte" class="font-sm text-gray-700 font-medium mb-1">Transporte</label>
                            <select id="transporte" class="border border-gray-300 py-2 rounded-lg" bind:value={transporteAgendaId}>
                                <option value="">Selecione</option>
                                {#each transportes as trans}
                                    <option value={trans.id}>{trans.nomeVeiculo} - {trans.vagas}</option>
                                {/each}
                            </select>
                        </div>
                    </div>

                    {#each localAgendamento as idLoc, i (i) }
                        <div class="flex flex-col">
                            <label for="local-agendamento" class="text-gray-700 font-medium font-sm mb-1">Local de Agendamento {i+1}</label>
                            <select name={"solicitacao-" + 1} id="local-agendamento" class="border border-gray-300 rounded-lg py-2" bind:value={localAgendamento[i]}>
                                <option value="">Selecione...</option>
                                {#each locaisagendamento as loc}
                                    <option value={loc.id}>{loc.nomeLocal} </option>
                                {/each}
                            </select>
                            <div class="flex flex-col">
                                <button type="button" class="border rounded-sm p-1" onclick={() => removerLocalAgendamento(i)}> Remover</button>
                            </div>
                        </div>

                        {/each}
                        <div>
                            <button type="button" onclick={adicionarLocalAgendamento}>+ Adicionar Pontos de Parada</button>
                        </div>

                    
                    {#each pacienteSelecionados as idSelecionados, i (i) }
                        <div class="flex flex-col">
                            <label for="solicitacao" class="text-gray-700 font-sm font-medium mb-1">Paciente {i+1}</label>
                            <select id={"solicitacao-" + 1} class="rounded-lg border border-gray-300 py-2" bind:value={pacienteSelecionados[i]}>
                                <option value="">Selecione...</option>
                                
                                {#each solicitacoes as sol }
                                    <option value={sol.id}>Nome: {sol.nomePaciente} - CPF ({sol.cpfPaciente})</option>
                                {/each}
                                
                            </select>
                            <button type="button" class="px-2 py-1 border rounded" onclick={() => removerPaciente(i)}>
                                Remover
                                </button>
                        
                        </div>
                    {/each}
                        
                    <div>
                        <button type="button" onclick={adicionarPaciente} class="text-emerald-700 hover:text-emerald-900 cursor-pointer font-lg font-semi-bold">+ Adicionar Paciente </button>
                    </div>
                       

                    <div class="flex flex-col align-middle">
                        <button type="submit" class="bg-emerald-700 p-2 rounded-lg text-white hover:bg-emerald-900 cursor-pointer">Cadastrar</button>
                    </div>
                </form>
            </div>
        </main>
    </div>
</div>