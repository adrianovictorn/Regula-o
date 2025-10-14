<script lang="ts">
    import { getApi, postApi } from "$lib/api.js";
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

    interface Motorista {
        id: number
        nome: string
        telefone?: string
    }

    type Turno = 'MANHA' | 'TARDE' | 'NAO_INFORMADO';

    interface PacienteFormulario {
        solicitacaoId: string;
        localId: string;
        turno: Turno;
        retornaMesmoDia: boolean;
    }

    function criarPacienteVazio(): PacienteFormulario {
        return {
            solicitacaoId: '',
            localId: '',
            turno: 'NAO_INFORMADO',
            retornaMesmoDia: false
        };
    }

    let cidades: Cidade[] = [];
    let transportes: Transporte[] = [];
    let motoristas: Motorista[] = [];
    let solicitacoes: Solicitacao[] = [];
    let locaisagendamento: LocalAgendamento[] = [];

    let transporteAgendaId = '';
    let cidadeAgendaId = '';
    let motoristaSelecionadoId = '';
    let pacientesFormulario: PacienteFormulario[] = [criarPacienteVazio()];
    let dataAgendamento = '';
    let horaSaidaAgendamento = '';
    

    function resetarFormulario() {
        transporteAgendaId = '';
        cidadeAgendaId = '';
        motoristaSelecionadoId = '';
        pacientesFormulario = [criarPacienteVazio()];
        dataAgendamento = '';
        horaSaidaAgendamento = '';
    }

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

    async function carregarLocaisAgendamento(cidadeId?: number) {
        try{
            const endpoint = cidadeId ? `local/agendamento?cidadeId=${cidadeId}` : "local/agendamento";
            const res = await getApi(endpoint)
            
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

    async function carregarMotoristas() {
        try{
            const res = await getApi("motoristas")

            if(!res.ok){
                alert("Erro ao buscar motoristas!")
            }

            const data = await res.json() as Motorista[];
            motoristas = data;
        }catch{
            alert("Erro ao se conectar ao servidor !")
        }
    }

    async function aoSelecionarCidade(event: Event) {
        const value = (event.target as HTMLSelectElement).value;
        cidadeAgendaId = value;
        await carregarLocaisAgendamento(value ? Number(value) : undefined);
        pacientesFormulario = pacientesFormulario.map((paciente) => ({
            ...paciente,
            localId: ''
        }));
    }

    onMount(async () => {
        await carregarCidades();
        await carregarLocaisAgendamento();
        await carregarSolicitacoes();
        await carregarTransportes();
        await carregarMotoristas();
    })

    async function cadastrarAgendamentoTransporte(e: Event){
        e.preventDefault();

        const transporteId = transporteAgendaId ? Number(transporteAgendaId) : null;
        const cidadeId = cidadeAgendaId ? Number(cidadeAgendaId) : null;
        const motoristaId = motoristaSelecionadoId ? Number(motoristaSelecionadoId) : null;

        if (!transporteId || !cidadeId || !dataAgendamento) {
            alert("Informe transporte, cidade e data do agendamento.");
            return;
        }

        const pacientesPayloadBrutos = pacientesFormulario
            .map((paciente) => ({
                idTexto: paciente.solicitacaoId.trim(),
                localTexto: paciente.localId.trim(),
                turno: paciente.turno ?? 'NAO_INFORMADO',
                retorna: paciente.retornaMesmoDia ?? false
            }))
            .filter((entrada) => entrada.idTexto.length > 0);

        if (pacientesPayloadBrutos.some((entrada) => entrada.localTexto.length === 0)) {
            alert('Selecione o local de atendimento para todos os pacientes.');
            return;
        }

        const pacientesPayload = pacientesPayloadBrutos.map((entrada) => ({
            solicitacaoId: Number(entrada.idTexto),
            localAgendamentoId: Number(entrada.localTexto),
            turno: entrada.turno,
            retornaMesmoDia: entrada.retorna
        }));

        if (pacientesPayload.some((paciente) => Number.isNaN(paciente.solicitacaoId) || Number.isNaN(paciente.localAgendamentoId))) {
            alert('Alguma seleção de paciente ou local é inválida.');
            return;
        }

        const idLista = pacientesPayload.map((paciente) => paciente.solicitacaoId);
        if (new Set(idLista).size !== idLista.length) {
            alert('Existem pacientes duplicados na seleção.');
            return;
        }

        const locaisDoPercurso = Array.from(new Set(pacientesPayload.map((paciente) => paciente.localAgendamentoId)));

        const payload = { 
            transporteId,
            cidadeId,
            data: dataAgendamento,
            motoristaId,
            horaSaida: horaSaidaAgendamento ? horaSaidaAgendamento : null,
            pacientes: pacientesPayload,
            locaisAgendamento: locaisDoPercurso

        }

        try{
            const res = await postApi("agendamento/transporte/agendar/solicitacoes", payload)

            if(!res.ok){
                const mensagemErro = await res.text();
                throw new Error(mensagemErro || "Erro ao enviar dados ao servidor!");
            }

            await res.json();
            resetarFormulario();
            await carregarLocaisAgendamento();
            alert("Agendamento salvo com sucesso!");

        }catch(error: any){
            alert(error?.message ?? "Erro ao se conectar ao servidor!");
        }
    }


    function adicionarPaciente(){
        pacientesFormulario = [...pacientesFormulario, criarPacienteVazio()];
    }

    function removerPaciente(i: number){
        pacientesFormulario = pacientesFormulario.toSpliced(i,1);
        if (pacientesFormulario.length === 0) {
            pacientesFormulario = [criarPacienteVazio()];
        }
    }

    function limparCampos(){
    }

    
</script>

<div class="flex min-h-screen bg-gray-100">
    <RoleBasedMenu activePage="/agendar/transporte" />
    
    <div class="flex flex-1 flex-col">
        <header class="bg-emerald-700 text-white font-semibold p-4 shadow flex items-center justify-between">
            <h1 class="text-lg">Agendamento de Transporte</h1>

            <UserMenu/>
        </header>

        <main class="flex-1 overflow-auto p-6"> 

            <section class="bg-white rounded-xl shadow p-6 border border-emerald-100">
                <h2 class="text-base font-semibold text-emerald-800 mb-4">Nova agenda</h2>

                <form class="grid grid-cols-1 gap-4" on:submit={cadastrarAgendamentoTransporte}>
                    <div class="grid gap-4 md:grid-cols-4">
                        <div class="flex flex-col">
                            <label for="data" class="text-gray-700 font-medium text-sm mb-1">Data do Agendamento</label>
                            <input type="date" id="data" class="border border-gray-300 rounded-lg px-3 py-2 focus:border-emerald-500 focus:outline-none" bind:value={dataAgendamento}>
                        </div>
                        <div class="flex flex-col">
                            <label for="cidade" class="text-gray-700 font-medium text-sm mb-1">Cidade</label>
                            <select  id="cidade" class="rounded-lg border border-gray-300 px-3 py-2 " bind:value={cidadeAgendaId} on:change={aoSelecionarCidade}>
                                <option value="">Selecione...</option>
                                {#each cidades  as  cidade}
                                    <option value={String(cidade.id)}>{cidade.nomeCidade} - {cidade.codigoIBGE}</option>
                                {/each}
                            </select>
                        </div>

                        
                        <div class="flex flex-col">
                            <label for="transporte" class="text-sm text-gray-700 font-medium mb-1">Transporte</label>
                            <select id="transporte" class="border border-gray-300 px-3 py-2 rounded-lg focus:border-emerald-500 focus:outline-none" bind:value={transporteAgendaId}>
                                <option value="">Selecione</option>
                                {#each transportes as trans}
                                    <option value={String(trans.id)}>{trans.nomeVeiculo} - {trans.vagas}</option>
                                {/each}
                            </select>
                        </div>
                        <div class="flex flex-col">
                            <label for="motorista" class="text-sm text-gray-700 font-medium mb-1">Motorista</label>
                            <select id="motorista" class="border border-gray-300 px-3 py-2 rounded-lg focus:border-emerald-500 focus:outline-none" bind:value={motoristaSelecionadoId}>
                                <option value="">Selecione</option>
                                {#each motoristas as motorista}
                                    <option value={String(motorista.id)}>{motorista.nome}</option>
                                {/each}
                            </select>
                        </div>
                    </div>

                    <div class="flex flex-col">
                        <label for="hora-saida" class="text-gray-700 font-medium text-sm mb-1">Hora de saída</label>
                        <input type= "time" id="hora-saida" class="border border-gray-300 rounded-lg px-3 py-2 focus:border-emerald-500 focus:outline-none" bind:value={horaSaidaAgendamento}>
                    </div>



                                        
                    {#each pacientesFormulario as paciente, index (index) }
                        <div class="grid gap-2 rounded-lg border border-gray-200 bg-gray-50 p-4">
                            <label for={"solicitacao-" + index} class="text-gray-700 text-sm font-medium">Paciente {index + 1}</label>
                            <select
                                id={"solicitacao-" + index}
                                class="rounded-lg border border-gray-300 px-3 py-2 focus:border-emerald-500 focus:outline-none"
                                bind:value={paciente.solicitacaoId}
                            >
                                <option value="">Selecione...</option>
                                {#each solicitacoes as sol}
                                    <option value={String(sol.id)}>Nome: {sol.nomePaciente} - CPF ({sol.cpfPaciente})</option>
                                {/each}
                            </select>
                            <div class="flex flex-col">
                            <label for={"local-" + index} class="text-xs font-medium text-gray-600">Local de atendimento</label>
                            <select
                                id={"local-" + index}
                                class="border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-emerald-200"
                                bind:value={paciente.localId}
                            >
                                <option value="">Selecione...</option>
                                {#each locaisagendamento as loc}
                                    <option value={String(loc.id)}>{loc.nomeLocal}</option>
                                {/each}
                            </select>
                            </div>
                            <div class="grid gap-3 md:grid-cols-2">
                                <div class="flex flex-col">
                                    <label for={"turno-" + index} class="text-xs font-medium text-gray-600">Turno</label>
                                    <select
                                        id={"turno-" + index}
                                        class="border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-emerald-200"
                                        bind:value={paciente.turno}
                                    >
                                        <option value="MANHA">Manhã</option>
                                        <option value="TARDE">Tarde</option>
                                        <option value="NAO_INFORMADO">Não informado</option>
                                    </select>
                                </div>
                                <label for={"retorno-" + index} class="flex items-center gap-2 text-xs font-medium text-gray-600 md:pt-6">
                                    <input
                                        id={"retorno-" + index}
                                        type="checkbox"
                                        class="h-4 w-4 rounded border-gray-300 text-emerald-600 focus:ring-emerald-500"
                                        bind:checked={paciente.retornaMesmoDia}
                                    />
                                    Retorno no mesmo dia
                                </label>
                            </div>
                            <div class="flex justify-end">
                                <button type="button" class="px-3 py-1 text-sm border border-red-300 text-red-500 rounded hover:bg-red-50 transition" on:click={() => removerPaciente(index)}>
                                    Remover
                                </button>
                            </div>
                        
                        </div>
                    {/each}
                        
                    <div>
                        <button type="button" on:click={adicionarPaciente} class="text-emerald-700 hover:text-emerald-900 cursor-pointer font-medium text-sm">+ Adicionar paciente</button>
                    </div>
                       

                    <div class="flex justify-end">
                        <button type="submit" class="bg-emerald-700 px-6 py-2 rounded-lg text-white font-medium hover:bg-emerald-900 cursor-pointer transition">Cadastrar</button>
                    </div>
                </form>
            </section>
        </main>
    </div>
</div>







