<script>
  import { goto } from '$app/navigation';
  import { onMount } from 'svelte';

  export let data; // Dados da função load (listaDeSolicitacoes, error)
  
  // Variáveis do formulário e estado
  let idSolicitacao = null; // ID da solicitação carregada
  let nomePaciente = '';
  let cpfPaciente = '';
  let cns = '';
  let datanascimento = '';
  let usfOrigem = '';
  let dataMalote = '';
  let observacoes = '';
  
  let inputsReadonly = false;

  // Lista de EXAMES derivada do enum EspecialidadesEnum (ItemCategoria.EXAME_OU_PROCEDIMENTO)
  // Esta lista será usada para os checkboxes e também para o dropdown de adição manual, se mantido.
const todosOsExamesDoEnum = [
    { value: 'DOPPLER', label: 'Doppler' },
    { value: 'MAMOGRAFIA', label: 'Mamografia' },
    { value: 'HOLTER', label: 'Holter' },
    { value: 'TESTE_ERGOMETRICO', label: 'Teste Ergométrico' },
    { value: 'MAPA', label: 'M.A.P.A.' },
    { value: 'TOMOGRAFIA', label: 'Tomografia Computadorizada' },
    { value: 'RESSONANCIA', label: 'Ressonância Magnética' },
    { value: 'CINTILOGRAFIA', label: 'Cintilografia' },
    { value: 'ELETRONEUROMIOGRAFIA', label: 'Eletroneuromiografia' },
    { value: 'CATETERISMO', label: 'Cateterismo' },
    { value: 'HEMOGRAMA_COMPLETO', label: 'Hemograma Completo' },
    { value: 'GLICEMIA_JEJUM', label: 'Glicemia de Jejum' },
    { value: 'HEMOGLOBINA_GLICADA_HBA1C', label: 'Hemoglobina Glicada (HbA1c)' },
    { value: 'COLESTEROL_TOTAL', label: 'Colesterol Total' },
    { value: 'HDL_COLESTEROL', label: 'HDL Colesterol' },
    { value: 'LDL_COLESTEROL', label: 'LDL Colesterol' },
    { value: 'VLDL_COLESTEROL', label: 'VLDL Colesterol' },
    { value: 'TRIGLICERIDEOS', label: 'Triglicerídeos' },
    { value: 'UREIA', label: 'Ureia' },
    { value: 'CREATININA', label: 'Creatinina' },
    { value: 'SODIO', label: 'Sódio' },
    { value: 'POTASSIO', label: 'Potássio' },
    { value: 'ACIDO_URICO', label: 'Ácido Úrico' },
    { value: 'SUMARIO_DE_URINA_EAS', label: 'Sumário de Urina (EAS)' },
    { value: 'UROCULTURA_COM_ANTIBIOGRAMA', label: 'Urocultura com Antibiograma' },
    { value: 'PARASITOLOGICO_DE_FEZES', label: 'Parasitológico de Fezes' },
    { value: 'PESQUISA_SANGUE_OCULTO_FEZES', label: 'Pesquisa de Sangue Oculto nas Fezes' },
    { value: 'TESTE_RAPIDO_GRAVIDEZ_TIG', label: 'Teste Rápido de Gravidez (TIG)' },
    { value: 'TESTE_RAPIDO_HIV', label: 'Teste Rápido para HIV' },
    { value: 'SOROLOGIA_HIV', label: 'Sorologia para HIV' },
    { value: 'TESTE_RAPIDO_SIFILIS', label: 'Teste Rápido para Sífilis' },
    { value: 'VDRL', label: 'VDRL' },
    { value: 'TESTE_RAPIDO_HEPATITE_B', label: 'Teste Rápido para Hepatite B' },
    { value: 'HBSAG', label: 'HBsAg' },
    { value: 'ANTI_HBS', label: 'Anti-HBs' },
    { value: 'TESTE_RAPIDO_HEPATITE_C', label: 'Teste Rápido para Hepatite C' },
    { value: 'ANTI_HCV', label: 'Anti-HCV' },
    { value: 'TSH_HORMONIO_TIREOESTIMULANTE', label: 'TSH' },
    { value: 'T4_LIVRE', label: 'T4 Livre' },
    { value: 'PSA_TOTAL', label: 'PSA Total' },
    { value: 'PSA_LIVRE', label: 'PSA Livre' },
    { value: 'BACILOSCOPIA_DE_ESCARRO_BAAR', label: 'Baciloscopia de Escarro (BAAR)' },
    { value: 'CULTURA_DE_ESCARRO', label: 'Cultura de Escarro' },
    { value: 'ELETROCARDIOGRAMA_ECG', label: 'Eletrocardiograma (ECG)' },
    { value: 'COLPOSCOPIA', label: 'Colposcopia' },
    { value: 'BIOPSIA_COLO_UTERINO', label: 'Biópsia de Colo Uterino' },
    { value: 'MAMOGRAFIA_BILATERAL', label: 'Mamografia Bilateral' },
    { value: 'ULTRASSONOGRAFIA_ABDOMINAL_TOTAL', label: 'USG Abdominal Total' },
    { value: 'ULTRASSONOGRAFIA_ABDOMEN_SUPERIOR', label: 'USG Abdômen Superior' },
    { value: 'ULTRASSONOGRAFIA_PELVICA_VIA_ABDOMINAL', label: 'USG Pélvica (Abdominal)' },
    { value: 'ULTRASSONOGRAFIA_PELVICA_TRANSVAGINAL', label: 'USG Pélvica (Transvaginal)' },
    { value: 'ULTRASSONOGRAFIA_OBSTETRICA', label: 'USG Obstétrica' },
    { value: 'ULTRASSONOGRAFIA_VIAS_URINARIAS', label: 'USG Vias Urinárias' },
    { value: 'ULTRASSONOGRAFIA_PROSTATA_VIA_ABDOMINAL', label: 'USG Próstata (Abdominal)' },
    { value: 'ULTRASSONOGRAFIA_TIREOIDE', label: 'USG Tireoide' },
    { value: 'ULTRASSONOGRAFIA_ARTICULAR', label: 'USG Articular' },
    { value: 'ULTRASSONOGRAFIA_DOPPLER_ARTERIAL_MEMBRO_INFERIOR_UNILATERAL', label: 'USG Doppler Arterial MI Unilateral' },
    { value: 'ULTRASSONOGRAFIA_DOPPLER_ARTERIAL_MEMBRO_INFERIOR_BILATERAL', label: 'USG Doppler Arterial MI Bilateral' },
    { value: 'ULTRASSONOGRAFIA_DOPPLER_VENOSO_MEMBRO_INFERIOR_UNILATERAL', label: 'USG Doppler Venoso MI Unilateral' },
    { value: 'ULTRASSONOGRAFIA_DOPPLER_VENOSO_MEMBRO_INFERIOR_BILATERAL', label: 'USG Doppler Venoso MI Bilateral' },
    { value: 'ULTRASSONOGRAFIA_DOPPLER_CAROTIDAS_E_VERTEBRAIS', label: 'USG Doppler Carótidas e Vertebrais' },
    { value: 'RAIO_X_TORAX_PA', label: 'Raio-X Tórax PA' },
    { value: 'RAIO_X_TORAX_PA_PERFIL', label: 'Raio-X Tórax PA/Perfil' },
    { value: 'RAIO_X_SEIOS_DA_FACE', label: 'Raio-X Seios da Face' },
    { value: 'RAIO_X_COLUNA_CERVICAL', label: 'Raio-X Coluna Cervical' },
    { value: 'RAIO_X_COLUNA_DORSAL', label: 'Raio-X Coluna Dorsal' },
    { value: 'RAIO_X_COLUNA_LOMBO_SACRA', label: 'Raio-X Coluna Lombo-Sacra' },
    { value: 'RAIO_X_ABDOMEN_SIMPLES', label: 'Raio-X Abdômen Simples' },
    { value: 'RAIO_X_ABDOMEN_AGUDO', label: 'Raio-X Abdômen Agudo' },
    { value: 'RAIO_X_ARTICULACAO_COXO_FEMURAL_BACIA', label: 'Raio-X Bacia' },
    { value: 'RAIO_X_JOELHO', label: 'Raio-X Joelho' },
    { value: 'RAIO_X_MAO_OU_QUIRODACTILOS', label: 'Raio-X Mão/Quirodáctilos' },
    { value: 'RAIO_X_PE_OU_PODODACTILOS', label: 'Raio-X Pé/Pododáctilos' },
    { value: 'TESTE_ERGOMETRICO_CONVENCIONAL', label: 'Teste Ergométrico Convencional' },
    { value: 'HOLTER_24_HORAS_3_CANAIS', label: 'Holter 24 Horas (3 Canais)' },
    { value: 'MAPA_24_HORAS_MONITORIZACAO_AMBULATORIAL_PRESSAO_ARTERIAL', label: 'MAPA 24 Horas' },
    { value: 'ENDOSCOPIA_DIGESTIVA_ALTA', label: 'Endoscopia Digestiva Alta' },
    { value: 'ENDOSCOPIA_DIGESTIVA_ALTA_COM_BIOPSIA', label: 'Endoscopia Digestiva Alta com Biópsia' },
    { value: 'COLONOSCOPIA', label: 'Colonoscopia' },
    { value: 'COLONOSCOPIA_COM_BIOPSIA', label: 'Colonoscopia com Biópsia' },
    { value: 'AUDIOMETRIA_TONAL_LIMIAR_COM_TESTES_DE_DISCRIMINACAO', label: 'Audiometria Tonal e Vocal' },
    { value: 'IMPEDANCIOMETRIA', label: 'Impedanciometria' },
    { value: 'FUNDOSCOPIA_BINOCULAR_INDIRETA', label: 'Fundoscopia Binocular Indireta' },
    { value: 'TONOMETRIA_APLANACAO', label: 'Tonometria de Aplanação' },
    { value: 'DENSITOMETRIA_OSSEA_COLUNA_E_FEMUR', label: 'Densitometria Óssea' },
    { value: 'ECOCARDIOGRAMA_TRANSTORACICO_MODO_M_BIDIMENSIONAL_DOPPLER', label: 'Ecocardiograma Transtorácico com Doppler' },
    { value: 'ESPIROMETRIA_PROVA_VENTILATORIA_COMPLETA', label: 'Espirometria Completa' },
    { value: 'ELETROENCEFALOGRAMA_CONVENCIONAL', label: 'Eletroencefalograma Convencional' },
    { value: 'TOMOGRAFIA_COMPUTADORIZADA_CRANIO', label: 'TC Crânio' },
    { value: 'TOMOGRAFIA_COMPUTADORIZADA_TORAX', label: 'TC Tórax' },
    { value: 'TOMOGRAFIA_COMPUTADORIZADA_ABDOMEN_TOTAL', label: 'TC Abdômen Total' },
    { value: 'RESSONANCIA_MAGNETICA_CRANIO_ENCEFALO', label: 'RM Crânio/Encéfalo' },
    { value: 'RESSONANCIA_MAGNETICA_COLUNA_CERVICAL', label: 'RM Coluna Cervical' },
    { value: 'RESSONANCIA_MAGNETICA_COLUNA_LOMBAR', label: 'RM Coluna Lombar' },
    { value: 'RESSONANCIA_MAGNETICA_JOELHO', label: 'RM Joelho' },
    { value: 'CINTILOGRAFIA_MIOCARDIO_PERFUSAO_REPOUSO_ESTRESSE', label: 'Cintilografia Miocárdio (Repouso/Estresse)' },
    { value: 'CINTILOGRAFIA_TIREOIDE_CAPTACAO', label: 'Cintilografia Tireoide com Captação' },
    { value: 'CINTILOGRAFIA_OSSEA', label: 'Cintilografia Óssea' },
    { value: 'ELETRONEUROMIOGRAFIA_MEMBROS_SUPERIORES', label: 'Eletroneuromiografia MMSS' },
    { value: 'ELETRONEUROMIOGRAFIA_MEMBROS_INFERIORES', label: 'Eletroneuromiografia MMII' },
    { value: 'CATETERISMO_CARDIACO_ESQUERDO_DIAGNOSTICO', label: 'Cateterismo Cardíaco Diagnóstico' },
    { value: 'AVALIACAO_URODINAMICA_COMPLETA', label: 'Avaliação Urodinâmica Completa' },
    { value: 'EXERESE_PEQUENAS_LESOES_PELE_ANATOMOPATOLOGICO', label: 'Exerese Pequenas Lesões de Pele + AP' }
].map(ex => ({ ...ex, selecionado: false })); // Adiciona a propriedade 'selecionado' para os checkboxes

  // Se você ainda quiser distinguir "laboratoriais" dos demais para a lista de checkboxes,
  // você precisará de uma lógica de filtro aqui. Por enquanto, 'examesDisponiveisParaCheckbox' usará todos os exames.
  let examesDisponiveisParaCheckbox = [...todosOsExamesDoEnum];

  // Guarda os exames que o usuário adiciona manualmente (se a seção for mantida)
  // ou os exames carregados de uma solicitação existente.
  let examesDaSolicitacaoAtual = []; 
  
  let listaDeSolicitacoesParaDropdown = [];
  let erroAoCarregarSolicitacoes = null;

  onMount(() => {
    if (data.listaDeSolicitacoes) {
      listaDeSolicitacoesParaDropdown = data.listaDeSolicitacoes;
    }
    if (data.error) {
      erroAoCarregarSolicitacoes = data.error;
      alert(data.error); // Considerar uma notificação menos intrusiva
    }
  });
  
  async function carregarDadosSolicitacao(solicitacaoIdParam) {
    if (!solicitacaoIdParam) {
      limparFormularioCompleto();
      return;
    }
    
    const res = await fetch(`/api/solicitacoes/${solicitacaoIdParam}`); //
    if (res.ok) {
      const s = await res.json(); // Este é o SolicitacaoViewDTO
      idSolicitacao = s.id;
      nomePaciente = s.nomePaciente || '';
      cpfPaciente = s.cpfPaciente || '';
      cns = s.cns || ''; 
      datanascimento = s.datanascimento ? s.datanascimento.split('T')[0] : '';
      usfOrigem = s.usfOrigem || '';
      dataMalote = s.dataMalote ? s.dataMalote.split('T')[0] : '';
      observacoes = s.observacoes || '';
      inputsReadonly = true;

      // Limpa seleções anteriores de checkboxes
      examesDisponiveisParaCheckbox.forEach(ex => ex.selecionado = false);
      
      // Mapeia as especialidades (exames) da solicitação carregada
      // para a lista 'examesDaSolicitacaoAtual'
      examesDaSolicitacaoAtual = s.especialidades ? 
        s.especialidades.map(esp => ({ // esp é SolicitacaoEspecialidadeViewDTO
            // O campo 'especialidadeSolicitada' no DTO é uma String.
            // Precisamos garantir que o value corresponde ao identificador do enum.
            especialidadeSolicitada: esp.especialidadeSolicitada, 
            status: esp.status,
            prioridade: esp.prioridade,
            // Se precisar do label para exibição, pode buscar em 'todosOsExamesDoEnum'
            label: todosOsExamesDoEnum.find(e => e.value === esp.especialidadeSolicitada)?.label || esp.especialidadeSolicitada
        })) : 
        [];

    } else {
      alert("Erro ao carregar detalhes da solicitação selecionada.");
      limparFormularioCompleto();
    }
  }

  function handleSelecaoSolicitacao(event) {
    const selectedId = event.target.value;
    if (selectedId) {
      carregarDadosSolicitacao(selectedId);
    } else {
      limparFormularioCompleto();
    }
  }
  
  function limparFormularioCompleto() {
    idSolicitacao = null;
    nomePaciente = '';
    cpfPaciente = '';
    cns = '';
    datanascimento = '';
    usfOrigem = '';
    dataMalote = '';
    observacoes = '';
    inputsReadonly = false;
    examesDisponiveisParaCheckbox.forEach(ex => ex.selecionado = false);
    examesDaSolicitacaoAtual = []; // Limpa os exames da solicitação anterior
    const selectElement = document.getElementById('selectSolicitacao');
    if (selectElement) selectElement.value = "";
  }

  // Seção para adicionar outros tipos de exames (que não são checkboxes)
  // Esta seção é relevante principalmente para NOVAS solicitações.
  function addOutroExame() {
    // Só permite adicionar se for uma nova solicitação ou se a lógica de edição permitir
     if (!idSolicitacao) {
        examesDaSolicitacaoAtual = [
        ...examesDaSolicitacaoAtual,
        { especialidadeSolicitada: '', status: 'AGUARDANDO', prioridade: 'NORMAL' }
        ];
     } else {
         // Poderia ter uma lógica para adicionar a uma solicitação existente aqui,
         // mas o fluxo principal de adição para existentes é via checkboxes por enquanto.
         alert("Para adicionar outros exames a uma solicitação existente, use o fluxo de edição da solicitação ou contate o suporte.");
     }
  }
  function removerOutroExame(index) {
    examesDaSolicitacaoAtual = examesDaSolicitacaoAtual.filter((_, i) => i !== index);
  }


  async function submeterForm() {
    if (idSolicitacao) { // Adicionar exames (dos checkboxes) a uma solicitação existente
      const examesCheckboxParaAdicionar = examesDisponiveisParaCheckbox
        .filter(ex => ex.selecionado)
        // Opcional: filtrar para não adicionar exames que já existem na solicitação
        .filter(exChk => !examesDaSolicitacaoAtual.some(exSol => exSol.especialidadeSolicitada === exChk.value))
        .map(ex => ({
          especialidadeSolicitada: ex.value,
          status: 'AGUARDANDO', // Valor padrão
          prioridade: 'NORMAL'  // Valor padrão
        }));

      if (examesCheckboxParaAdicionar.length === 0) {
        alert("Nenhum novo exame selecionado nos checkboxes para adicionar.");
        return;
      }

      let todosAdicionadosComSucesso = true;
      for (const exame of examesCheckboxParaAdicionar) {
        const payload = { // Conforme EspecialidadeAdicionarDTO
            especialidadeSolicitada: exame.especialidadeSolicitada, // Este deve ser o NOME DA CONSTANTE DO ENUM
            status: exame.status, // Enum StatusDaMarcacao
            prioridade: exame.prioridade // Enum PrioridadeDaMarcacaoEnum
        };
        const res = await fetch(`/api/solicitacoes/${idSolicitacao}/especialidades`, { //
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });
        if (!res.ok) {
          todosAdicionadosComSucesso = false;
          const errorData = await res.json().catch(() => ({}));
          alert(`Erro ao adicionar o exame ${exame.especialidadeSolicitada}: ${errorData.message || res.statusText}`);
          break; 
        }
      }

      if (todosAdicionadosComSucesso) {
        alert('Novos exames selecionados foram adicionados à solicitação!');
        // Recarregar os dados da solicitação para ver os exames atualizados
        await carregarDadosSolicitacao(idSolicitacao); 
      }

    } else { // Criar uma nova solicitação
      const examesLabDosCheckboxes = examesDisponiveisParaCheckbox
        .filter(ex => ex.selecionado)
        .map(ex => ({
          especialidadeSolicitada: ex.value, // NOME DA CONSTANTE DO ENUM
          status: 'AGUARDANDO',
          prioridade: 'NORMAL'
        }));
      
      // Combina exames dos checkboxes com os da seção "outros exames" (se houver)
      const outrosExamesValidos = examesDaSolicitacaoAtual.filter(e => e.especialidadeSolicitada);
      const todosOsExamesParaNova = [...examesLabDosCheckboxes, ...outrosExamesValidos];

      if (todosOsExamesParaNova.length === 0) {
          alert("Nenhum exame selecionado para a nova solicitação.");
          return;
      }

      const payloadNovaSolicitacao = { // Conforme SolicitacaoCreateDTO
        nomePaciente, cpfPaciente, cns, datanascimento,
        usfOrigem, dataMalote, observacoes,
        especialidades: todosOsExamesParaNova // Lista de SolicitacaoEspecialidadeCreateDTO
      };

      const res = await fetch(`/api/solicitacoes`, { //
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payloadNovaSolicitacao)
      });

      if (res.ok) {
        alert('Nova solicitação com exames cadastrada com sucesso!');
        limparFormularioCompleto();
        // goto('/exames'); // Opcional: redirecionar ou apenas limpar
      } else {
        const errorData = await res.json().catch(() => ({}));
        alert(`Erro ao cadastrar nova solicitação: ${errorData.message || res.statusText}`);
      }
    }
  }

  function formatarCPF(e) {
    let d = e.target.value.replace(/\D/g, '').slice(0,11);
    d = d.replace(/^(\d{3})(\d)/, '$1.$2').replace(/^(\d{3}\.\d{3})(\d)/, '$1.$2').replace(/(\d{3}\.\d{3}\.\d{3})(\d)/, '$1-$2');
    cpfPaciente = d;
  }

</script>

<div class="flex h-screen bg-gray-100">
  <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">Regula System</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Nova Solicitação</a>
      <a href="/exames" class="py-2 px-4 rounded bg-emerald-700 transition">Laboratório</a>
      <a href="/agendar" class="py-2 px-4 rounded hover:bg-emerald-800 ">Agendamento</a>
      <a href="/paciente" class="py-2 px-4 rounded hover:bg-emerald-800">Paciente</a>
      <a href="/exportar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
  </aside>

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Solicitação de Exames</h1>
      <div>Bem-vindo(a), Usuário</div>
    </header>

    <main class="flex-1 overflow-auto p-6">
      <div class="bg-white rounded-lg shadow-lg p-6">
        
        <div class="mb-6">
          <label for="selectSolicitacao" class="block text-lg font-medium text-gray-700 mb-2">Carregar Solicitação Existente:</label>
          <select id="selectSolicitacao" on:change={handleSelecaoSolicitacao} class="border border-gray-300 rounded-lg p-3 w-full focus:ring-emerald-500 focus:border-emerald-500 text-base">
            <option value="">Selecione uma solicitação</option>
            {#if listaDeSolicitacoesParaDropdown && listaDeSolicitacoesParaDropdown.length > 0}
              {#each listaDeSolicitacoesParaDropdown as sol (sol.id)}
                <option value={sol.id}>{sol.nomePaciente} (CPF: {sol.cpfPaciente || 'N/A'})</option>
              {/each}
            {:else if !erroAoCarregarSolicitacoes}
              <option disabled>Carregando solicitações...</option>
            {/if}
          </select>
          {#if erroAoCarregarSolicitacoes}
            <p class="text-red-500 text-sm mt-1">{erroAoCarregarSolicitacoes}</p>
          {/if}
        </div>

        <h2 class="text-2xl font-bold text-emerald-800 mb-6 border-b pb-3">
          {#if idSolicitacao}Adicionar Exames à Solicitação ID: {idSolicitacao}{:else}Nova Solicitação com Exames{/if}
        </h2>

        <form on:submit|preventDefault={submeterForm} class="space-y-8">
          
          <fieldset class="border border-gray-300 p-4 rounded-lg">
            <legend class="text-xl font-semibold text-gray-700 px-2">Dados do Paciente</legend>
            <div class="grid grid-cols-1 lg:grid-cols-4 gap-6 mt-4">
              <div class="flex flex-col">
                <label for="nomePaciente" class="text-sm font-medium text-gray-700 mb-1">Nome</label>
                <input id="nomePaciente" type="text" bind:value={nomePaciente} readonly={inputsReadonly} class:bg-gray-100={inputsReadonly} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
              </div>
              <div class="flex flex-col">
                <label for="cpfPaciente" class="text-sm font-medium text-gray-700 mb-1">CPF</label>
                <input id="cpfPaciente" type="text" bind:value={cpfPaciente} on:input={formatarCPF} readonly={inputsReadonly} class:bg-gray-100={inputsReadonly} placeholder="000.000.000-00" class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
              </div>
              <div class="flex flex-col">
                <label for="cns" class="text-sm font-medium text-gray-700 mb-1">CNS</label>
                <input id="cns" type="text" bind:value={cns} readonly={inputsReadonly} class:bg-gray-100={inputsReadonly} placeholder="Cartão do SUS" maxlength="15" class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
              </div>
              <div class="flex flex-col">
                <label for="datanascimento" class="text-sm font-medium text-gray-700 mb-1">Data de Nasc.</label>
                <input id="datanascimento" type="date" bind:value={datanascimento} readonly={inputsReadonly} class:bg-gray-100={inputsReadonly} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
              </div>
            </div>
          </fieldset>

          <fieldset class="border border-gray-300 p-4 rounded-lg">
            <legend class="text-xl font-semibold text-gray-700 px-2">Detalhes da Solicitação</legend>
             <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-4">
                <div class="flex flex-col">
                    <label for="dataMalote" class="text-sm font-medium text-gray-700 mb-1">Data Recebimento</label>
                    <input id="dataMalote" type="date" bind:value={dataMalote} readonly={inputsReadonly} class:bg-gray-100={inputsReadonly} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
                </div>
                <div class="flex flex-col">
                    <label for="usfOrigem" class="text-sm font-medium text-gray-700 mb-1">USF Origem</label>
                    <select id="usfOrigem" bind:value={usfOrigem} disabled={inputsReadonly} class:bg-gray-100={inputsReadonly} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required>
                        <option value="" disabled>Selecione...</option>
                        {#each ['USF01','USF02','USF03','USF04','USF05','USF06'] as u}
                            <option value={u}>{u}</option>
                        {/each}
                    </select>
                </div>
            </div>
          
          </fieldset>
          
          {#if idSolicitacao && examesDaSolicitacaoAtual.length > 0}
            <fieldset class="border border-gray-300 p-4 rounded-lg">
                <legend class="text-xl font-semibold text-gray-700 px-2">Exames Já Presentes na Solicitação (ID: {idSolicitacao})</legend>
                <ul class="list-disc pl-5 mt-2 text-sm">
                    {#each examesDaSolicitacaoAtual as exameExistente (exameExistente.especialidadeSolicitada + exameExistente.id)}
                        <li>
                            {exameExistente.label} 
                            (Status: {exameExistente.status}, Prioridade: {exameExistente.prioridade})
                        </li>
                    {/each}
                </ul>
            </fieldset>
          {/if}

          <fieldset class="border border-gray-300 p-4 rounded-lg">
            <legend class="text-xl font-semibold text-gray-700 px-2">
              {#if idSolicitacao}Adicionar Novos Exames à Solicitação{:else}Selecionar Exames para Nova Solicitação{/if}
            </legend>
            <p class="text-xs text-gray-500 mb-3">Marque os exames desejados. Eles serão adicionados com status "AGUARDANDO" e prioridade "NORMAL".</p>
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-x-4 gap-y-3 mt-4 max-h-96 overflow-y-auto pr-2">
              {#each examesDisponiveisParaCheckbox as exameLab, i (exameLab.value)}
                <label for="exameChk-{i}" class="flex items-center space-x-2 p-2 border border-gray-200 rounded-md hover:bg-gray-50 transition cursor-pointer">
                  <input type="checkbox" id="exameChk-{i}" bind:checked={exameLab.selecionado} class="form-checkbox h-4 w-4 text-emerald-600 rounded focus:ring-emerald-500">
                  <span class="text-sm text-gray-700 select-none">{exameLab.label}</span>
                </label>
              {/each}
            </div>
          </fieldset>
        

          <button type="submit" class="w-full bg-emerald-700 text-white py-3 rounded-lg hover:bg-emerald-800 transition text-lg font-semibold">
            {#if idSolicitacao}Enviar{:else}Criar Nova Solicitação com Exames{/if}
          </button>
        </form>
      </div>
    </main>
  </div>
</div>

<style>
  .form-checkbox {
    appearance: none;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 0.25rem;
    display: inline-block;
    height: 1.1em; /* Ajustado para melhor alinhamento */
    margin-right: 0.5em;
    position: relative;
    vertical-align: middle;
    width: 1.1em; /* Ajustado para melhor alinhamento */
    cursor: pointer;
    flex-shrink: 0; /* Para não encolher dentro do flex container */
  }
  .form-checkbox:checked {
    background-color: #10b981; /* emerald-600 */
    border-color: #059669; /* emerald-700 */
  }
  .form-checkbox:checked::before {
    content: '✓';
    color: white;
    font-size: 0.8em; /* Ajustado */
    font-weight: bold;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    line-height: 1;
  }
  select:disabled, input:read-only {
    cursor: not-allowed;
    background-color: #f3f4f6; /* bg-gray-100 */
  }
  /* Para garantir que o texto ao lado do checkbox não quebre estranhamente */
  label.flex span {
    flex-grow: 1;
    word-break: break-word;
  }
</style>