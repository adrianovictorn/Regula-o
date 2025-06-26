<script lang="ts">
  import { onMount } from "svelte";
  import { getApi } from '$lib/api';
  import jsPDF from 'jspdf';
  import autoTable from 'jspdf-autotable';
  import { opcoesEspecialidades } from '$lib/Especialidades.js';

  // --- Estado do Componente ---
  let dataSelecionada = $state(new Date().toISOString().split('T')[0]);
  let loadingType = $state<string | null>(null);
  let allSolicitacoes = $state<any[]>([]);
  let isLoadingData = $state(true);

  // --- ESTRUTURA DE RELATÓRIOS ---
  const tiposDeRelatorio = [
    {
      label: 'Laboratório',
      color: 'bg-emerald-600',
      hover: 'hover:bg-emerald-700',
      icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M7.5 21L3 16.5m0 0L7.5 12M3 16.5h13.5m0-13.5L21 7.5m0 0L16.5 12M21 7.5H7.5" />`,
      especialidades: [
        'HEMOGRAMA_COMPLETO', 'GLICEMIA_JEJUM', 'HEMOGLOBINA_GLICADA_HBA1C', 
        'COLESTEROL_TOTAL', 'HDL_COLESTEROL', 'LDL_COLESTEROL', 'VLDL_COLESTEROL', 
        'TRIGLICERIDEOS', 'UREIA', 'CREATININA', 'SODIO', 'POTASSIO', 'ACIDO_URICO', 
        'SUMARIO_DE_URINA_EAS', 'UROCULTURA_COM_ANTIBIOGRAMA', 'PARASITOLOGICO_DE_FEZES', 
        'PESQUISA_SANGUE_OCULTO_FEZES', 'TESTE_RAPIDO_GRAVIDEZ_TIG', 'TESTE_RAPIDO_HIV', 
        'SOROLOGIA_HIV', 'TESTE_RAPIDO_SIFILIS', 'VDRL', 'TESTE_RAPIDO_HEPATITE_B', 
        'HBSAG', 'ANTI_HBS', 'TESTE_RAPIDO_HEPATITE_C', 'ANTI_HCV', 
        'TSH_HORMONIO_TIREOESTIMULANTE', 'T4_LIVRE', 'PSA_TOTAL', 'PSA_LIVRE', 
        'BACILOSCOPIA_DE_ESCARRO_BAAR', 'CULTURA_DE_ESCARRO'
      ]
    },
    {
      label: 'Raio X',
      color: 'bg-sky-600',
      hover: 'hover:bg-sky-700',
      icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75l3 3m0 0l3-3m-3 3v-7.5M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />`,
      especialidades: [
        'RAIO_X_TORAX_PA', 'RAIO_X_TORAX_PA_PERFIL', 'RAIO_X_SEIOS_DA_FACE', 
        'RAIO_X_COLUNA_CERVICAL', 'RAIO_X_COLUNA_DORSAL', 'RAIO_X_COLUNA_LOMBO_SACRA', 
        'RAIO_X_ABDOMEN_SIMPLES', 'RAIO_X_ABDOMEN_AGUDO', 'RAIO_X_ARTICULACAO_COXO_FEMURAL_BACIA', 
        'RAIO_X_JOELHO', 'RAIO_X_MAO_OU_QUIRODACTILOS', 'RAIO_X_PE_OU_PODODACTILOS'
      ]
    },
    {
      label: 'Ultrassonografia',
      color: 'bg-indigo-600',
      hover: 'hover:bg-indigo-700',
      icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M9 4.5v15m6-15v15m-10.875 0h15.75c.621 0 1.125-.504 1.125-1.125V5.625c0-.621-.504-1.125-1.125-1.125H4.125C3.504 4.5 3 5.004 3 5.625v12.75c0 .621.504 1.125 1.125 1.125z" />`,
      especialidades: [
        'ULTRASSONOGRAFIA_PARTES_MOLES', 'ULTRASSONOGRAFIA_ABDOMINAL_TOTAL', 
        'ULTRASSONOGRAFIA_ABDOMEN_SUPERIOR', 'ULTRASSONOGRAFIA_PELVICA_VIA_ABDOMINAL', 
        'ULTRASSONOGRAFIA_PELVICA_TRANSVAGINAL', 'ULTRASSONOGRAFIA_OBSTETRICA', 
        'ULTRASSONOGRAFIA_VIAS_URINARIAS', 'ULTRASSONOGRAFIA_PROSTATA_VIA_ABDOMINAL', 
        'ULTRASSONOGRAFIA_TIREOIDE', 'ULTRASSONOGRAFIA_ARTICULAR'
      ]
    },
    {
      label: 'Cardiologia',
      color: 'bg-rose-600',
      hover: 'hover:bg-rose-700',
      icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z" />`,
      especialidades: [
        'CARDIOLOGISTA', 'TESTE_ERGOMETRICO', 'HOLTER', 'MAPA', 
        'ECOCARDIOGRAMA_TRANSTORACICO_MODO_M_BIDIMENSIONAL_DOPPLER', 'CATETERISMO_CARDIACO_ESQUERDO_DIAGNOSTICO'
      ]
    },
     {
      label: 'Doppler',
      color: 'bg-teal-600',
      hover: 'hover:bg-teal-700',
      icon: `<path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />`,
      especialidades: [
        'ULTRASSONOGRAFIA_DOPPLER_ARTERIAL_MEMBRO_INFERIOR_UNILATERAL',
        'ULTRASSONOGRAFIA_DOPPLER_ARTERIAL_MEMBRO_INFERIOR_BILATERAL',
        'ULTRASSONOGRAFIA_DOPPLER_VENOSO_MEMBRO_INFERIOR_UNILATERAL',
        'ULTRASSONOGRAFIA_DOPPLER_VENOSO_MEMBRO_INFERIOR_BILATERAL',
        'ULTRASSONOGRAFIA_DOPPLER_CAROTIDAS_E_VERTEBRAIS'
      ]
    },{
      label: 'Pediatria',
      color: 'bg-blue-300',
      hover: 'hover:bg-blue-400'
    }
  ];


  onMount(async () => {
    try {
      const res = await getApi('solicitacoes');
      if (!res.ok) throw new Error('Falha ao carregar dados do servidor.');
      allSolicitacoes = await res.json();
    } catch (e) {
      alert('Não foi possível carregar os dados das solicitações.');
      console.error(e);
    } finally {
      isLoadingData = false;
    }
  });

  async function gerarPlanilha(especialidades: string[], label: string) {
    if (!dataSelecionada) {
      alert('Por favor, selecione uma data.');
      return;
    }
    loadingType = label;

    try {
      const tiposQueryParam = especialidades.join(',');
      
      // CORREÇÃO: Removido o '/api' do início da URL
      const checkUrl = `exportar/verificar-dados?tipos=${encodeURIComponent(tiposQueryParam)}&data=${dataSelecionada}&label=${encodeURIComponent(label)}`;
      const response = await getApi(checkUrl);
      
      if (!response.ok) throw new Error('Falha ao verificar dados com o servidor.');
      
      const result = await response.json();
      if (result.dadosDisponiveis) {
        // CORREÇÃO: Removido o '/api' do início da URL
        const downloadUrl = `exportar/planilha?tipos=${encodeURIComponent(tiposQueryParam)}&data=${dataSelecionada}&label=${encodeURIComponent(label)}`;
        const fileResponse = await getApi(downloadUrl);
        const blob = await fileResponse.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `relatorio_${label.replace(/\s+/g, '_').toLowerCase()}_${dataSelecionada}.xlsx`;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);
      } else {
        alert(`Nenhum dado encontrado para o relatório de ${label} na data ${new Date(dataSelecionada + 'T12:00:00').toLocaleDateString('pt-BR')}.`);
      }
    } catch (error) {
      console.error(`Erro ao gerar planilha de ${label}:`, error);
      alert("Ocorreu um erro de comunicação com o servidor. Tente novamente.");
    } finally {
      setTimeout(() => { loadingType = null; }, 1000);
    }
  }

  function gerarPDFGeral() {
      // ... (nenhuma alteração nesta função)
      if (isLoadingData || allSolicitacoes.length === 0) {
          alert('Não há dados carregados para gerar o PDF.');
          return;
      }
      loadingType = 'PDF_GERAL';

      const doc = new jsPDF();
      const tableColumn = ["ID", "Paciente", "CPF", "USF", "Data Malote", "Especialidades Pendentes"];
      const tableRows: any[][] = [];

      allSolicitacoes.forEach(sol => {
          const pendentes = sol.especialidades
              .filter((e: any) => e.status === 'AGUARDANDO')
              .map((e: any) => e.especialidadeSolicitada)
              .join(', ');

          if (pendentes) {
              const solicitacaoData = [
                  sol.id,
                  sol.nomePaciente,
                  sol.cpfPaciente,
                  sol.usfOrigem,
                  new Date(sol.dataMalote + 'T12:00:00').toLocaleDateString('pt-BR'),
                  pendentes
              ];
              tableRows.push(solicitacaoData);
          }
      });
      
      if(tableRows.length === 0){
          alert("Nenhuma solicitação com pendências encontrada para gerar o relatório.");
          loadingType = null;
          return;
      }

      autoTable(doc, {
          head: [tableColumn],
          body: tableRows,
          startY: 20,
          theme: 'striped',
          headStyles: { fillColor: [22, 160, 133] }
      });
      
      doc.text("Relatório Geral de Solicitações Pendentes", 14, 15);
      doc.save(`relatorio_geral_pendentes_${new Date().toISOString().split('T')[0]}.pdf`);

      setTimeout(() => { loadingType = null; }, 1000);
  }

</script>

<div class="flex h-screen bg-gray-100">
  <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">SIRG</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Nova Solicitação</a>
      <a href="/exames" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Laboratório</a>
      <a href="/agendar" class="py-2 px-4 rounded hover:bg-emerald-800">Agendamento</a>
      <a href="/paciente" class="py-2 px-4 rounded hover:bg-emerald-800">Paciente</a>
      <a href="/exportar" class="py-2 px-4 rounded bg-emerald-700 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • © 2025</div>
  </aside>

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Gerar Planilhas de Encaminhamento</h1>
      <div>Bem-vindo(a), Usuário</div>
    </header>

    <main class="flex-1 p-6 overflow-auto">
      <div class="max-w-5xl mx-auto space-y-8">
        <div class="bg-white p-4 rounded-lg shadow-md">
            <label for="data" class="block text-base font-semibold text-gray-800 mb-2">
              🗓️ Selecione a data para os relatórios:
            </label>
            <input
              type="date"
              id="data"
              bind:value={dataSelecionada}
              class="w-full md:w-auto border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
            />
        </div>
        
        <section>
          <h2 class="text-xl font-bold text-gray-700 mb-3">Relatórios por Tipo de Procedimento</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-5 gap-5">
            {#each tiposDeRelatorio as relatorio}
              <button 
                onclick={() => gerarPlanilha(relatorio.especialidades, relatorio.label)} 
                disabled={loadingType === relatorio.label} 
                class="p-4 {relatorio.color} text-white rounded-lg shadow-lg {relatorio.hover} hover:scale-105 transform transition-all duration-200 flex flex-col items-center justify-center h-32 disabled:bg-gray-400 disabled:cursor-wait disabled:scale-100"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                  {@html relatorio.icon}
                </svg>
                <span class="text-lg font-bold text-center">{relatorio.label}</span>
                {#if loadingType === relatorio.label}
                  <span class="text-xs mt-1 animate-pulse">Gerando...</span>
                {/if}
              </button>
            {/each}
          </div>
        </section>
        
        <section>
            <h2 class="text-xl font-bold text-gray-700 mb-3">Relatórios Gerais</h2>
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-5">
              <button onclick={gerarPDFGeral} disabled={isLoadingData || loadingType === 'PDF_GERAL'} class="p-4 bg-blue-600 text-white rounded-lg shadow-lg hover:bg-blue-700 hover:scale-105 transform transition-all duration-200 flex flex-col items-center justify-center h-32 disabled:bg-gray-400 disabled:cursor-wait disabled:scale-100">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
                <span class="text-lg font-bold text-center">PDF Geral de Pendentes</span>
                 {#if loadingType === 'PDF_GERAL'}<span class="text-xs mt-1 animate-pulse">Gerando...</span>{/if}
              </button>
            </div>
        </section>
      </div>
    </main>
    </div>
</div>