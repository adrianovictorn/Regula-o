<script lang="ts">
    import { getApi, putApi } from "$lib/api";
    import Menu2 from "$lib/Menu2.svelte";
    import UserMenu from "$lib/UserMenu.svelte";
    import { onMount } from "svelte";

    // --- Estado do Componente ---
    let solicitacoesDeHoje = $state([]);
    let erro = $state(null);
    let carregando = $state(true);
    let total = $state(0);
    let dataHoje = $state(getHojeFormatado());
    
    // Lista de especialidades para este painel de Laboratório
    const especialidadesDoPainel = [
        'ASLO', 'ACIDO_URICO', 'ANALISE_CARACTERES_FISICOS_ELEMENTOS_SEDIMENTO_URINA',
        'ANTI_HBS', 'ANTI_HCV', 'ANTIBIOGRAMA', 'BACILOSCOPIA_DE_ESCARRO_BAAR',
        'BACILOSCOPIA_DIRETA_BAAR_TUBERCULOS_CONTROLE', 'BACILOSCOPIA_DIRETA_BAAR_TUBERCULOSE_DIAGNOSTICA',
        'BACTEROSCOPIA_GRAM', 'BILIRRUBINA_TOTAL_FRACOES', 'CALCIO', 'CLEARANCE_CREATININA',
        'COAGULOGRAMA', 'COLESTEROL_TOTAL', 'CONTAGEM_PLAQUETAS', 'CONTAGEM_RETICULOCITOS',
        'CREATININA', 'CULTURA_BACTERIAS_IDENTIFICACAO_UROCULTURA', 'CULTURA_DE_ESCARRO',
        'DETERMINACAO_CAPACIDADE_FIXACAO_ferro', 'DETERMINACAO_CURVA_GLICEMICA_2_DOSAGENS',
        'DETERMINACAO_LATEX', 'DETERMINACAO_TEMPO_COAGULACAO', 'DETERMINACAO_TEMPO_SANGRAMENTO_IVY',
        'DETERMINACAO_TEMPO_SANGRAMENTO_DUKE', 'DETERMINACAO_TEMPO_TROMBINA',
        'DETERMINACAO_VELOCIDADE_HEMOSSEDIMENTACAO_VHS', 'DETERMINACAO_DIRETA_REVERSA_GRUPO_ABO',
        'DETERMINACAO_QUANTITATIVA_PROTEINA_C_REATIVA', 'DOSAGEM_25_HIDROXIVITAMINA_D',
        'DOSAGEM_ALBUMINA', 'DOSAGEM_ALFA_1_ANTITRIPSINA', 'DOSAGEM_ALFA_FETOPROTEINA',
        'DOSAGEM_AMILASE', 'DOSAGEM_CALCIO_IONIZAVEL', 'DOSAGEM_COMPLEMENTO_C3',
        'DOSAGEM_COMPLEMENTO_C4', 'DOSAGEM_CORTISOL', 'DOSAGEM_CREATINOFOSFOQUINASE_CPK',
        'DOSAGEM_CREATINOFOSFOQUINASE_FRACAO_MB', 'DOSAGEM_DEHIDROEPIANDROSTERONA_DHEA',
        'DOSAGEM_DESIDROGENASE_LATICA', 'DOSAGEM_ESTRADIOL', 'DOSAGEM_ESTRIOL',
        'DOSAGEM_ESTRONA', 'DOSAGEM_FERRITINA', 'DOSAGEM_FERRO_SERICO',
        'DOSAGEM_FOSFATASE_ACIDA_TOTAL', 'DOSAGEM_FOSFORO',
        'DOSAGEM_GONADOTROFINA_CORIONICA_HUMANA_HCG_BETA_HCG', 'DOSAGEM_HAPTOGLOBINA',
        'DOSAGEM_HEMOGLOBINA', 'DOSAGEM_HEMOGLOBINA_GLICOSILADA',
        'DOSAGEM_HORMONIO_FOLICULO_ESTIMULANTE_FSH', 'DOSAGEM_HORMONIO_LUTEINIZANTE_LH',
        'DOSAGEM_IMUNOGLOBULINA_A_IGA', 'DOSAGEM_IMUNOGLOBULINA_E_IGE',
        'DOSAGEM_IMUNOGLOBULINA_M_IGM', 'DOSAGEM_INSULINA', 'DOSAGEM_LACTATO',
        'DOSAGEM_LIPASE', 'DOSAGEM_LITIO', 'DOSAGEM_MAGNESIO',
        'DOSAGEM_MICROALBUMINA_NA_URINA', 'DOSAGEM_MUCO_PROTEINAS',
        'DOSAGEM_PARATORMONIO', 'DOSAGEM_PROGESTERONA', 'DOSAGEM_PROLACTINA',
        'DOSAGEM_PROTEINAS_URINA_24_HORAS', 'DOSAGEM_PROTEINAS_TOTAIS',
        'DOSAGEM_PROTEINAS_TOTAIS_E_FRACOES', 'DOSAGEM_SULFATO_DE_HIDROEPIANDROSTERONA_DHEAS',
        'DOSAGEM_TESTOSTERONA', 'DOSAGEM_TESTOSTERONA_LIVRE', 'DOSAGEM_TIREOGLOBULINA',
        'DOSAGEM_TIROXINA_T4', 'DOSAGEM_TRANSFERRINA', 'DOSAGEM_TRIIODOTIRONINA_T3',
        'DOSAGEM_TROPONINA', 'DOSAGEM_VITAMINA_B12', 'DOSAGEM_ZINCO',
        'ELETROFORESE_DE_HEMOGLOBINA', 'ELETROFORESE_DE_LIPOPROTEINAS',
        'ERITROGRAMA', 'EXAME_CARACTERES_FISICOS_CONTAGEM_GLOBAL_ESPECIFICA_CELULAS',
        'FATOR_REUMATOIDE', 'FOSFATASE_ALCALINA', 'GAMA_GT', 'GLICEMIA_JEJUM',
        'GLICOSE', 'HBSAG', 'HDL_COLESTEROL', 'HEMATOCRITO', 'HEMOCULTURA',
        'HEMOGLOBINA_GLICADA_HBA1C', 'HEMOGRAMA_COMPLETO', 'IMUNOELETROFORESE_DE_PROTEINAS',
        'LDL_COLESTEROL', 'LEUCOGRAMA', 'PSA_LIVRE', 'PSA_TOTAL', 'PARASITOLOGICO_DE_FEZES',
        'PESQUISA_ANTICORPO_IGG_ANTICARDIOLIPINA', 'PESQUISA_ANTICORPO_IGM_DE_ANTICARDIOLIPINA',
        'PESQUISA_ANTICORPOS_ANTI_HELICOBACTER_PYLORI', 'PESQUISA_ANTICORPOS_ANTI_HIV_1_HIV_2_ELISA',
        'PESQUISA_ANTICORPOS_ANTI_HTLV_1_HTLV_2', 'PESQUISA_ANTICORPOS_ANTI_SM',
        'PESQUISA_ANTICORPOS_ANTI_SS_A_RO', 'PESQUISA_ANTICORPOS_ANTI_SS_B_LA',
        'PESQUISA_ANTICORPOS_ANTINUCLEO', 'PESQUISA_ANTICORPOS_ANTITIREOGLOBULINA',
        'PESQUISA_ANTICORPOS_CONTRA_ANTIGENO_E_VIRUS_HEPATITE_B_ANTI_HBE', 'PESQUISA_ANTICORPOS_CONTRA_VIRUS_HEPATITE_D_ANTI_HDV',
        'PESQUISA_ANTICORPOS_CONTRA_VIRUS_SARAMPO', 'PESQUISA_ANTICORPOS_IGG_ANTICITOMEGALOVIRUS',
        'PESQUISA_ANTICORPOS_IGG_ANTILEISHMANIAS', 'PESQUISA_ANTICORPOS_IGG_ANTITOXOPLASMA',
        'PESQUISA_ANTICORPOS_IGG_ANTITRYPANOSOMA_CRUZI', 'PESQUISA_ANTICORPOS_IGG_CONTRA_ANTIGENO_CENTRAL_VIRUS_HEPATITE_B_ANTI_HBC_IGG',
        'PESQUISA_ANTICORPOS_IGG_CONTRA_VIRUS_HEPATITE_A_HAV_IGG', 'PESQUISA_ANTICORPOS_IGG_CONTRA_VIRUS_RUBEOLA',
        'PESQUISA_ANTICORPOS_IGG_CONTRA_VIRUS_VARICELA_HERPES_ZOSTER', 'PESQUISA_ANTICORPOS_IGG_CONTRA_VIRUS_EPSTEIN_BARR',
        'PESQUISA_ANTICORPOS_IGM_ANTICITOMEGALOVIRUS', 'PESQUISA_ANTICORPOS_IGM_ANTILEISHMANIAS',
        'PESQUISA_ANTICORPOS_IGM_ANTITOXOPLASMA', 'PESQUISA_ANTICORPOS_IGM_ANTITRYPANOSOMA_CRUZI',
        'PESQUISA_ANTICORPOS_IGM_CONTRA_ANTIGENO_CENTRAL_VIRUS_HEPATITE_B_ANTI_HBC_IGM', 'PESQUISA_ANTICORPOS_IGM_CONTRA_VIRUS_HEPATITE_A_HAV_IGG',
        'PESQUISA_ANTICORPOS_IGM_CONTRA_VIRUS_RUBEOLA', 'PESQUISA_ANTIGENO_CARCINOEMBRIONARIO_CEA',
        'PESQUISA_ANTIGENO_E_VIRUS_HEPATITE_B_HBEAG', 'PESQUISA_FATOR_RH_INCLUI_D_FRACO',
        'PESQUISA_FATOR_REUMATOIDE_WAALER_ROSE', 'PESQUISA_HEMOGLOBINA_S',
        'PESQUISA_LARVAS_NAS_FEZES', 'PESQUISA_OVOS_CISTOS_PARASITAS',
        'PESQUISA_SANGUE_OCULTO_FEZES', 'PESQUISA_TRIPANOSSOMA',
        'PESQUISA_TROFOZOITAS_NAS_FEZES', 'POTASSIO', 'PROVA_RETRACAO_COAGULO',
        'PROVA_DO_LACO', 'REACAO_HEMAGLUTINACAO_TPHA_DIAGNOSTICO_SIFILIS',
        'REACAO_MONTENEGRO_ID', 'SODIO', 'SOROLOGIA_HIV', 'SUMARIO_DE_URINA_EAS',
        'T4_LIVRE', 'TGO', 'TGP', 'TP', 'TSH_HORMONIO_TIREOESTIMULANTE', 'TTPA',
        'TESTE_VDRL_DETECCAO_SIFILIS', 'TESTE_DIRETO_ANTIGLOBULINA_HUMANA_TAD',
        'TESTE_FTA_ABS_IGG_DIAGNOSTICO_SIFILIS', 'TESTE_FTA_ABS_IGM_DIAGNOSTICO_SIFILIS',
        'TESTE_RAPIDO_GRAVIDEZ_TIG', 'TESTE_RAPIDO_HEPATITE_B', 'TESTE_RAPIDO_HEPATITE_C',
        'TESTE_RAPIDO_HIV', 'TESTE_RAPIDO_SIFILIS', 'TESTE_TUBERCULINICO_PPD',
        'TIPO_SANGUINEO', 'TRIGLICERIDEOS', 'UREIA', 'UROCULTURA_COM_ANTIBIOGRAMA',
        'VDRL', 'VDRL_DETECCAO_SIFILIS_EM_GESTANTE', 'VLDL_COLESTEROL'
    ];


    function getHojeFormatado() {
        const hoje = new Date();
        const ano = hoje.getFullYear();
        const mes = String(hoje.getMonth() + 1).padStart(2, '0');
        const dia = String(hoje.getDate()).padStart(2, '0');
        return `${ano}-${mes}-${dia}`;
    }

    async function carregarSolicitacoes() {
        carregando = true;
        erro = null;
        try {
            const res = await getApi('solicitacoes');
            if (!res.ok) { throw new Error('Falha ao carregar as solicitações'); }
            
            const todasSolicitacoes = await res.json();
            const hoje = getHojeFormatado();

            solicitacoesDeHoje = todasSolicitacoes.map(sol => {
                const especialidadesFiltradas = sol.especialidades.filter(esp => {
                    const agendamento = sol.agendamentos.find(ag => ag.id === esp.agendamentoId);
                    return esp.status === 'AGENDADO' &&
                           especialidadesDoPainel.includes(esp.especialidadeSolicitada) &&
                           agendamento?.dataAgendada === hoje;
                });
                
                // Adiciona o estado 'selecionado' para os checkboxes
                const especialidadesComSelecao = especialidadesFiltradas.map(e => ({ ...e, selecionado: true }));

                return { ...sol, especialidades: especialidadesComSelecao, observacoesEdit: sol.observacoes || '' };
            }).filter(sol => sol.especialidades.length > 0);
            
            total = solicitacoesDeHoje.length

        } catch (e) {
            erro = e.message;
        } finally {
            carregando = false;
        }
    }
    
    // Função para marcar todos como um único status
    async function confirmarTodos(solicitacao, novoStatus: 'REALIZADO' | 'CANCELADO') {
        const idsParaAtualizar = solicitacao.especialidades.map(e => e.id);
        if (idsParaAtualizar.length === 0) return;

        const acao = novoStatus === 'REALIZADO' ? 'confirmar TODOS' : 'marcar FALTA em TODOS';
        if (confirm(`Tem certeza que deseja ${acao} os procedimentos deste paciente?`)) {
            try {
                const res = await putApi('especialidades/status/batch', { especialidadeIds: idsParaAtualizar, status: novoStatus });
                if (!res.ok) { throw new Error('Erro ao atualizar o status'); }
                await carregarSolicitacoes();
            } catch (e) {
                alert(e.message);
            }
        }
    }

    // Nova função para confirmação parcial
    async function submeterConfirmacaoParcial(solicitacao) {
        const realizadosIds = solicitacao.especialidades.filter(e => e.selecionado).map(e => e.id);
        const faltouIds = solicitacao.especialidades.filter(e => !e.selecionado).map(e => e.id);

        if (realizadosIds.length === 0 && faltouIds.length === 0) {
            alert("Nenhum procedimento para atualizar.");
            return;
        }
        
        try {
            // Envia as atualizações em paralelo
            const promises = [];
            if (realizadosIds.length > 0) {
                promises.push(putApi('especialidades/status/batch', { especialidadeIds: realizadosIds, status: 'REALIZADO' }));
            }
            if (faltouIds.length > 0) {
                promises.push(putApi('especialidades/status/batch', { especialidadeIds: faltouIds, status: 'CANCELADO' }));
            }
            
            // Atualiza as observações se houver mudança
            if (solicitacao.observacoesEdit !== solicitacao.observacoes) {
                promises.push(putApi(`solicitacoes/${solicitacao.id}`, { observacoes: solicitacao.observacoesEdit }));
            }

            await Promise.all(promises);
            await carregarSolicitacoes();

        } catch(e) {
            alert("Ocorreu um erro ao submeter as alterações.");
        }
    }

    function formatarData(dataString) {
        if (!dataString) return 'Data não informada';
        const [ano, mes, dia] = dataString.split('-');
        return `${dia}/${mes}/${ano}`;
    }

    onMount(() => {
        carregarSolicitacoes();
    });

</script>

<svelte:head>
    <title>Agenda do Laboratório</title>
</svelte:head>

<div class="flex h-screen bg-gray-100">
    <Menu2/>
    <div class="flex-1 flex flex-col">
        <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
            <h1 class="text-xl font-semibold">Agenda do Dia - Laboratório</h1>
            <UserMenu />
        </header>

        <main class="flex-1 p-6 overflow-auto">
            {#if carregando}
                <p class="text-center text-gray-500 animate-pulse">Carregando painel...</p>
            {:else if erro}
                <p class="text-red-500 text-center">Erro: {erro}</p>
            {:else if solicitacoesDeHoje.length === 0}
                <div class="text-center p-10 bg-white rounded-lg shadow-sm">
                    <h2 class="text-xl font-semibold text-gray-700">Nenhum paciente para hoje.</h2>
                    <p class="text-gray-500 mt-2">Não há agendamentos de laboratório para a data de hoje.</p>
                </div>
            {:else}
                <div class="py-3">
                    <h2><b>Total de Procedimentos previstos para hoje:</b> {total}</h2>
                    <h2><b>Data do Atendimento:</b> {formatarData(dataHoje)}</h2>
                </div>

                <div class="bg-white rounded-lg shadow-md">
                    <div class="grid grid-cols-12 gap-4 p-4 border-b font-bold text-emerald-800 bg-emerald-100 rounded-t-lg">
                        <div class="col-span-4">Paciente</div>
                        <div class="col-span-8">Exames Agendados</div>
                    </div>

                    <div class="flex flex-col">
                        {#each solicitacoesDeHoje as s (s.id)}
                            <div class="grid grid-cols-12 gap-4 p-4 items-start border-t">
                                <div class="col-span-4">
                                    <p class="font-semibold text-gray-800">{s.nomePaciente}</p>
                                    <p class="text-sm text-gray-500 font-mono">CPF: {s.cpfPaciente}</p>
                                </div>
                                
                                <div class="col-span-8 space-y-3">
                                    <button
                                        class="text-xs text-blue-600 hover:underline mb-2"
                                        on:click={() => s.especialidades.forEach(e => e.selecionado = true)}
                                    >
                                        Selecionar Todos
                                    </button>
                                    
                                    <div class="grid grid-cols-2 gap-x-4 gap-y-2">
                                        {#each s.especialidades as especialidade (especialidade.id)}
                                            <label class="flex items-center space-x-2 cursor-pointer">
                                                <input type="checkbox" bind:checked={especialidade.selecionado} class="h-4 w-4 rounded border-gray-300 text-emerald-600 focus:ring-emerald-500">
                                                <span class="text-sm text-gray-700 capitalize">{(especialidade.especialidadeSolicitada.replace(/_/g, ' ')).toLowerCase()}</span>
                                            </label>
                                        {/each}
                                    </div>
                                    
                                    <div class="mt-4">
                                        <label class="text-xs font-semibold text-gray-600">Observações:</label>
                                        <textarea bind:value={s.observacoesEdit} rows="2" class="mt-1 w-full p-2 border border-gray-300 rounded-md text-sm"></textarea>
                                    </div>

                                    <div class="flex justify-end gap-2 mt-3">
                                        <button on:click={() => submeterConfirmacaoParcial(s)} class="rounded-md bg-blue-600 hover:bg-blue-700 text-white px-3 py-1.5 text-sm font-semibold shadow-sm">Confirmar Selecionados</button>
                                        <button on:click={() => confirmarTodos(s, 'CANCELADO')} class="rounded-md bg-red-600 hover:bg-red-700 text-white px-3 py-1.5 text-sm font-semibold shadow-sm">Faltou a Todos</button>
                                    </div>
                                </div>
                            </div>
                        {/each}
                    </div>
                </div>
            {/if}
        </main>
    </div>
</div>