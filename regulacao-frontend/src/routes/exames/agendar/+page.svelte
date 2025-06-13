<script lang="ts">
  import { onMount } from 'svelte';
  import { jsPDF } from 'jspdf';
  import { invalidateAll } from '$app/navigation';

  // A API do SvelteKit injeta aqui o objeto `data` vindo de +page.js
  export let data: {
    agendamentos: {
      solicitacaoId: number;
      nomePaciente: string;
      cpfPaciente: string;
      usfOrigem: string;
      cns?: string;
      especialidadesPendentes: string[];
    }[];
  };

  // Sempre que `data.agendamentos` mudar, garantimos que `solicitacoes` seja um array válido
  $: solicitacoes = data?.agendamentos ?? [];

  // ID da solicitação selecionada no <select>
  let solicitacaoId: string = "";
  // Objeto completo da solicitação selecionada
  let solicitacaoSelecionada: typeof solicitacoes[number] | null = null;

  // Quando `solicitacoes` ou `solicitacaoId` mudam, recalculamos `solicitacaoSelecionada`
  $: {
    if (solicitacoes.length > 0 && solicitacaoId) {
      solicitacaoSelecionada =
        solicitacoes.find(
          s => String(s.solicitacaoId) === String(solicitacaoId)
        ) || null;
    } else {
      solicitacaoSelecionada = null;
    }
  }

  // ARRAY de exames marcados (usamos bind:group em cada checkbox)
  let examesSelecionados: string[] = [];

  // Outros campos do formulário
  let dataAgendada: string = "";
  let turno: 'MANHA' | 'TARDE' = 'MANHA';
  let localAgendado: string = "";
  let observacoes: string = "";

  // Lista fixa de locais (enum do backend → label de exibição)
  const locais = [
    { value: 'POLICLINICA_RECONVALE', label: 'Policlínica Reconvale' },
    { value: 'POLICLINICA_MUNICIPAL_DE_SANTO_ANTONIO_DE_JESUS', label: 'Policlínica Municipal de Santo Antônio de Jesus' },
    { value: 'HOSPITAL_DE_CONCEICAO_DO_ALMEIDA', label: 'Hospital de Conceição do Almeida' },
    { value: 'HOSPITAL_IRMA_DULCE', label: 'Hospital Irmã Dulce' },
    { value: 'HOSPITAL_DA_MULHER', label: 'Hospital da Mulher' },
    { value: 'LABCHECAP', label: 'Labchecap' },
    { value: 'HOSPITAL_ROBERTO_SANTOS', label: 'Hospital Roberto Santos' },
    { value: 'HOSPITAL_LUIS_ARGOLO', label: 'Hospital Luís Argolo' },
    { value: 'HOSPITAL_DOS_OLHOS_BAHIA', label: 'Hospital dos Olhos Bahia' },
    { value: 'CLINICA_A_MAIS_SAUDE', label: 'Clínica A Mais Saúde' },
    { value: 'HOSPITAL_ARISTIDES', label: 'Hospital Aristides' },
    { value: 'APAE', label: 'APAE' },
    { value: 'CDI', label: 'CDI' },
    { value: 'CEDAF', label: 'CEDAF' },
    { value: 'CLINICA_ELIZ', label: 'Clínica Eliz' },
    { value: 'MULTICENTRO_DA_CARLOS_GOMES', label: 'Multicentro da Carlos Gomes' },
    { value: 'MULTIIMAGEM', label: 'Multi Imagem' },
    { value: 'HOSPITAL_MUNICIPAL', label: 'Hospital Municipal' },
    { value: 'HOSPITAL_ORTOPEDICO', label: 'Hospital Ortopédico' },
    { value: 'HOSPITAL_DO_SUBURBIO', label: 'Hospital do Subúrbio' },
    { value: 'PRIMAGEM', label: 'Primagem' },
    { value: 'PONTO_ALTO_IMAGEM', label: 'Ponto Alto Imagem' },
    { value: 'HOSPITAL_REGIONAL', label: 'Hospital Regional' },
    { value: 'HOSPITAL_SANTA_IZABEL', label: 'Hospital Santa Izabel' }
  ];

  // URL pública do brasão de Conceição do Almeida
  const brasaoUrl =
    'https://upload.wikimedia.org/wikipedia/commons/9/97/Bras%C3%A3o_de_Concei%C3%A7%C3%A3o_do_Almeida.png';

  // Função: transforma o valor do enum em label legível
  function getLocalLabel(value: string) {
    const found = locais.find(loc => loc.value === value);
    return found ? found.label : value.replace(/_/g, ' ');
  }

  // Carrega imagem assíncrona (para inserir no PDF)
  async function loadImage(url: string): Promise<HTMLImageElement> {
    return new Promise((resolve, reject) => {
      const img = new Image();
      img.onload = () => resolve(img);
      img.onerror = e =>
        reject(new Error(`Falha ao carregar imagem: ${url}. Erro: ${e}`));
      img.crossOrigin = 'Anonymous';
      img.src = url;
    });
  }

  // Gera o PDF do comprovante, recebendo todos os dados em `dadosPDF`
  async function gerarComprovantePDF(dadosPDF: {
    solicitacaoId: number;
    nomePaciente: string;
    cpfPaciente: string;
    usfOrigem: string;
    cns?: string;
    examesSelecionados: string[];
    dataAgendada: string;
    turno: 'MANHA' | 'TARDE';
    localAgendado: string;
    observacoes: string;
  }) {
    const doc = new jsPDF({ unit: 'pt', format: 'a4', orientation: 'portrait' });
    const margin = { top: 80, left: 40, right: 40, bottom: 60 };
    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();
    let currentY = margin.top;

    const imgWidth = 60;
    const imgHeight = 60;
    const imgX = pageWidth / 2 - imgWidth / 2;
    const imgY = margin.top - 70;

    try {
      const brasaoImage = await loadImage(brasaoUrl);
      doc.addImage(brasaoImage, 'PNG', imgX, imgY, imgWidth, imgHeight);
    } catch (error) {
      console.error('Erro ao adicionar brasão ao PDF:', error);
      alert(
        'Ocorreu um erro ao carregar o brasão para o PDF. O PDF será gerado sem ele.'
      );
      doc.setFontSize(8);
      doc.setTextColor('#b91c1c');
      doc.text('Brasão indisponível', imgX + imgWidth / 2, imgY + imgHeight / 2, {
        align: 'center',
        baseline: 'middle'
      });
    }

    currentY = imgY + imgHeight + 25;

    // Cabeçalho fixo
    doc.setFontSize(20);
    doc.setTextColor('#115e59');
    doc.setFont('helvetica', 'bold');
    doc.text('Central de Regulação', pageWidth / 2, currentY, {
      align: 'center'
    });
    currentY += 24;
    doc.setFontSize(14);
    doc.setTextColor('#333333');
    doc.setFont('helvetica', 'normal');
    doc.text('Conceição do Almeida', pageWidth / 2, currentY, {
      align: 'center'
    });
    currentY += 40;

    // Título do comprovante
    doc.setFontSize(18);
    doc.setTextColor('#115e59');
    doc.setFont('helvetica', 'bold');
    doc.text('Comprovante de Agendamento', pageWidth / 2, currentY, {
      align: 'center'
    });
    currentY += 35;

    // Informações básicas (ID, Paciente, CPF, USF, CNS opcional)
    const informacoesBase = [
      { label: 'ID Solicitação', value: String(dadosPDF.solicitacaoId) },
      { label: 'Paciente', value: dadosPDF.nomePaciente || '' },
      { label: 'CPF', value: dadosPDF.cpfPaciente || '' },
      { label: 'USF Origem', value: dadosPDF.usfOrigem || '' },
      ...(dadosPDF.cns
        ? [{ label: 'CNS', value: dadosPDF.cns }]
        : [])
    ];

    let todasInfoFormatadas: {
      label: string;
      value: string;
      isHeaderExames?: boolean;
      isExameItem?: boolean;
    }[] = [...informacoesBase];

    // Se houver exames selecionados, adiciona um cabeçalho e depois cada item
    if (
      dadosPDF.examesSelecionados &&
      dadosPDF.examesSelecionados.length > 0
    ) {
      todasInfoFormatadas.push({
        label: 'Exames Agendados',
        value: dadosPDF.examesSelecionados.join(', ') // Junta o array em uma string
      });
    }

    // Informações do agendamento (data, turno, local, observações)
    const informacoesAgendamento = [
      {
        label: 'Data Agendada',
        value: dadosPDF.dataAgendada
          ? new Date(dadosPDF.dataAgendada + 'T00:00:00').toLocaleDateString(
              'pt-BR'
            )
          : 'Não definida'
      },
      {
        label: 'Turno',
        value:
          dadosPDF.turno === 'MANHA'
            ? 'Manhã'
            : dadosPDF.turno === 'TARDE'
            ? 'Tarde'
            : dadosPDF.turno
      },
      {
        label: 'Local',
        value: getLocalLabel(dadosPDF.localAgendado)
      },
      {
        label: 'Observações',
        value: dadosPDF.observacoes || 'Nenhuma'
      }
    ];
    todasInfoFormatadas = [
      ...todasInfoFormatadas,
      ...informacoesAgendamento
    ];

    const bgColor1 = '#f8f9fa';
    const bgColor2 = '#ffffff';
    const baseRowHeight = 28;
    const textIndent = 10;
    const valueIndentOffset = 120;
    const listItemIndent = 20;

    // Desenha cada linha de informação com background alternado
    todasInfoFormatadas.forEach((info, index) => {
      const bgColor = index % 2 === 0 ? bgColor1 : bgColor2;
      let textForValue = String(info.value);
      let availableWidthForValue: number;
      let valueX: number;

      const labelX = margin.left + textIndent;

      if (info.isExameItem) {
        availableWidthForValue =
          pageWidth -
          margin.left -
          margin.right -
          textIndent -
          listItemIndent -
          5;
        valueX = margin.left + textIndent + listItemIndent;
      } else if (info.label) {
        availableWidthForValue =
          pageWidth -
          margin.left -
          margin.right -
          textIndent -
          valueIndentOffset -
          5;
        valueX = margin.left + textIndent + valueIndentOffset;
      } else {
        availableWidthForValue =
          pageWidth - margin.left - margin.right - textIndent - 5;
        valueX = margin.left + textIndent;
      }

      if (availableWidthForValue <= 0) availableWidthForValue = 1;

      const lines = doc.splitTextToSize(textForValue, availableWidthForValue);
      let effectiveRowHeight = baseRowHeight;

      // Se for Observações ou lista de exames (com múltiplas linhas), ajusta altura
      if (
        info.label === 'Observações' ||
        (info.isExameItem && lines.length > 1) ||
        (info.isHeaderExames &&
          info.value === '' &&
          dadosPDF.examesSelecionados.length > 0)
      ) {
        const calculatedHeight = lines.length * 14 + 14;
        effectiveRowHeight = Math.max(baseRowHeight, calculatedHeight);
        if (info.isHeaderExames) effectiveRowHeight = baseRowHeight;
      } else if (info.isExameItem) {
        const calculatedHeight = lines.length * 14 + 10;
        effectiveRowHeight = Math.max(baseRowHeight, calculatedHeight);
      }

      // Desenho do background
      doc.setFillColor(bgColor);
      doc.rect(
        margin.left,
        currentY - 14,
        pageWidth - margin.left - margin.right,
        effectiveRowHeight,
        'F'
      );

      const textBaselineY = currentY;

      // Label (se existir)
      if (info.label) {
        doc.setFontSize(11);
        doc.setFont('helvetica', 'bold');
        doc.setTextColor(
          info.isHeaderExames ||
            ['Local', 'Data Agendada', 'Exames Agendados:'].includes(
              info.label
            )
            ? '#0d6efd'
            : '#212529'
        );
        doc.text(info.label, labelX, textBaselineY);
      }

      // Valor (quando não for cabeçalho vazio)
      if (!(info.isHeaderExames && info.value === '')) {
        doc.setFontSize(11);
        doc.setFont('helvetica', 'normal');
        doc.setTextColor(
          (info.isHeaderExames ||
            (['Local', 'Data Agendada'].includes(info.label) &&
              info.value !== ''))
            ? '#0d6efd'
            : '#343a40'
        );

        if (
          (info.isHeaderExames ||
            ['Local', 'Data Agendada'].includes(info.label)) &&
          info.value !== ''
        ) {
          doc.setFont('helvetica', 'bold');
          if (info.isHeaderExames) doc.setFontSize(11);
        }
        if (info.isExameItem) {
          doc.setFont('helvetica', 'normal');
          doc.setTextColor('#343a40');
        }
        doc.text(lines, valueX, textBaselineY);
      }

      currentY += effectiveRowHeight;
    });

    currentY += Math.max(20, baseRowHeight - 10);

    // Orientações finais
    doc.setFontSize(11);
    doc.setTextColor('#dc3545');
    doc.setFont('helvetica', 'bold');
    doc.text('Orientação Importante:', margin.left, currentY);
    currentY += 18;
    doc.setFont('helvetica', 'normal');
    doc.setTextColor('#212529');
    doc.text(
      '• Comparecer com 15 minutos de antecedência.',
      margin.left + 10,
      currentY
    );
    currentY += 16;
    doc.text(
      '• Levar RG, CPF, Cartão do SUS e este comprovante.',
      margin.left + 10,
      currentY
    );
    currentY += 16;
    doc.text(
      '• Em caso de não comparecimento, favor informar com antecedência através do telefone: (75) 98312-2478.',
      margin.left + 10,
      currentY
    );

    // Rodapé com data e hora
    const hoje = new Date().toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
    const hora = new Date().toLocaleTimeString('pt-BR', {
      hour: '2-digit',
      minute: '2-digit'
    });

    doc.setFontSize(9);
    doc.setTextColor('#6c757d');
    doc.text(`Emitido em: ${hoje} às ${hora}`, margin.left, pageHeight - margin.bottom + 20);
    doc.text(`Regula System v1.0`, pageWidth - margin.right, pageHeight - margin.bottom + 20, {
      align: 'right'
    });
    doc.text(
      `Desenvolvido por Adriano Victor N. Ribeiro, Filipe Ribeiro`,
      margin.left,
      pageHeight - margin.bottom + 35
    );

    // Salva com nome baseado no paciente e ID
    doc.save(
      `comprovante_${dadosPDF.nomePaciente.replace(/\s+/g, '_')}_${dadosPDF.solicitacaoId}.pdf`
    );
  }

  // Função executada ao clicar em "Agendar Exames Selecionados"
  async function enviarAgendamento() {
    if (!solicitacaoId) {
      alert('Por favor, selecione uma solicitação primeiro.');
      return;
    }
    if (!solicitacaoSelecionada) {
      alert('Dados da solicitação não carregados. Selecione novamente.');
      return;
    }
    if (examesSelecionados.length === 0) {
      alert('Por favor, selecione pelo menos um exame para agendar.');
      return;
    }
    if (!dataAgendada) {
      alert('Por favor, preencha a Data Agendada.');
      return;
    }
    if (!localAgendado) {
      alert('Por favor, selecione o Local do Agendamento.');
      return;
    }

    // Monta o payload exatamente como o backend espera
    const body = {
      examesSelecionados,
      dataAgendada,
      localAgendado,
      observacoes
    };

    try {
      const resposta = await fetch(
        `/api/agendamentos/${solicitacaoId}`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body)
        }
      );

      if (resposta.ok) {
        alert('Agendamento realizado com sucesso!');

        // Atualização local imediata (para feedback instantâneo)
        solicitacaoSelecionada.especialidadesPendentes =
          solicitacaoSelecionada.especialidadesPendentes.filter(
            nomeEsp => !examesSelecionados.includes(nomeEsp)
          );
        // Dispara reatividade
        solicitacaoSelecionada = { ...solicitacaoSelecionada };

        // Dados para gerar o PDF
        const dadosParaPDF = {
          solicitacaoId: solicitacaoSelecionada.solicitacaoId,
          nomePaciente: solicitacaoSelecionada.nomePaciente,
          cpfPaciente: solicitacaoSelecionada.cpfPaciente,
          usfOrigem: solicitacaoSelecionada.usfOrigem,
          cns: solicitacaoSelecionada.cns,
          examesSelecionados,
          dataAgendada,
          turno,
          localAgendado,
          observacoes
        };
        await gerarComprovantePDF(dadosParaPDF);

        // Recarrega todos os dados da página (load de +page.js)
        await invalidateAll();

        // Reseta campos do formulário
        dataAgendada = "";
        turno = "MANHA";
        localAgendado = "";
        observacoes = "";
        examesSelecionados = [];

        // Se a solicitação tiver ficado sem exames pendentes,
        // seleciona outra ou zera o dropdown
        setTimeout(() => {
          const aindaExiste = solicitacoes.some(
            s => String(s.solicitacaoId) === String(solicitacaoId)
          );
          if (!aindaExiste) {
            solicitacaoId = "";
          }
        }, 100);
      } else {
        // Erro vindo do backend
        const erroTexto = await resposta.text();
        let erroMsg = `Erro desconhecido. Código: ${resposta.status}`;
        try {
          const erroData = JSON.parse(erroTexto);
          erroMsg = erroData.mensagem || erroMsg;
        } catch {
          if (erroTexto) erroMsg = erroTexto.substring(0, 200);
        }
        alert(`Erro ao agendar: ${erroMsg}`);
      }
    } catch (err) {
      console.error('Erro na função enviarAgendamento:', err);
      alert('Erro de conexão ou ao processar o agendamento.');
    }
  }
</script>

<div class="flex h-screen bg-gray-100">
  <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">Regula System</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Nova Solicitação</a>
      <a href="/exames" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Laboratório</a>
      <a href="/agendamentos" class="py-2 px-4 rounded bg-emerald-700">Agendamento Múltiplo</a>
      <a href="/paciente" class="py-2 px-4 rounded hover:bg-emerald-800">Paciente</a>
      <a href="/" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
  </aside>

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Agendar Múltiplos Exames</h1>
      <div>Bem-vindo(a), Usuário</div>
    </header>

    <main class="flex-1 overflow-auto p-6">
      <div class="bg-white rounded-lg shadow-lg p-6">
        <h2 class="text-2xl font-bold text-emerald-800 mb-6">Novo Agendamento de Múltiplos Exames</h2>

        <form class="space-y-6" on:submit|preventDefault={enviarAgendamento}>
          <!-- Seletor de Solicitação -->
          <div class="flex flex-col">
            <label for="selectSolicitacao" class="text-sm font-medium text-gray-700 mb-1">
              Selecionar Solicitação
            </label>
            <select
              id="selectSolicitacao"
              bind:value={solicitacaoId}
              class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
            >
              <option value="" disabled>Selecione a solicitação</option>
              {#each solicitacoes as s (s.solicitacaoId)}
                <option value={s.solicitacaoId}>
                  {s.nomePaciente} — {s.cpfPaciente}
                </option>
              {/each}
            </select>
          </div>

          <!-- Dados do Paciente (readonly) -->
          <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div>
              <label class="text-sm font-medium text-gray-700 mb-1">Nome do Paciente</label>
              <input
                type="text"
                value={solicitacaoSelecionada?.nomePaciente || ""}
                readonly
                class="w-full bg-gray-100 border border-gray-300 rounded-lg p-2"
              />
            </div>
            <div>
              <label class="text-sm font-medium text-gray-700 mb-1">CPF</label>
              <input
                type="text"
                value={solicitacaoSelecionada?.cpfPaciente || ""}
                readonly
                class="w-full bg-gray-100 border border-gray-300 rounded-lg p-2"
              />
            </div>
            <div>
              <label class="text-sm font-medium text-gray-700 mb-1">USF Origem</label>
              <input
                type="text"
                value={solicitacaoSelecionada?.usfOrigem || ""}
                readonly
                class="w-full bg-gray-100 border border-gray-300 rounded-lg p-2"
              />
            </div>
            <div>
              <label class="text-sm font-medium text-gray-700 mb-1">CNS</label>
              <input
                type="text"
                value={solicitacaoSelecionada?.cns || ""}
                readonly
                class="w-full bg-gray-100 border border-gray-300 rounded-lg p-2"
              />
            </div>
          </div>

          <!-- Lista de Exames Pendentes -->
          {#if solicitacaoSelecionada && solicitacaoSelecionada.especialidadesPendentes}
            <div class="flex flex-col mt-6">
              <label class="text-sm font-medium text-gray-700 mb-2">
                Exames Pendentes da Solicitação:
              </label>
              {#if solicitacaoSelecionada.especialidadesPendentes.length > 0}
                <div class="space-y-2 border border-gray-200 rounded-lg p-3 max-h-60 overflow-y-auto">
                  {#each solicitacaoSelecionada.especialidadesPendentes as especialidadeNome (especialidadeNome)}
                    <label class="flex items-center space-x-2 p-2 rounded hover:bg-gray-50 cursor-pointer">
                      <input
                        type="checkbox"
                        value={especialidadeNome}
                        bind:group={examesSelecionados}
                        class="form-checkbox h-4 w-4 text-emerald-600 rounded focus:ring-emerald-500"
                      />
                      <span class="text-gray-700">{especialidadeNome}</span>
                    </label>
                  {/each}
                </div>
              {:else}
                <p class="text-sm text-gray-500 italic mt-2">
                  Nenhum exame pendente para esta solicitação.
                </p>
              {/if}
            </div>

            {#if solicitacaoSelecionada.especialidadesPendentes.length > 0}
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-4">
                <div>
                  <label for="dataAgendada" class="text-sm font-medium text-gray-700 mb-1">
                    Data para os Exames Selecionados
                  </label>
                  <input
                    type="date"
                    id="dataAgendada"
                    bind:value={dataAgendada}
                    class="w-full border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
                    required
                  />
                </div>
                <div>
                  <label for="turno" class="text-sm font-medium text-gray-700 mb-1">Turno</label>
                  <select
                    id="turno"
                    bind:value={turno}
                    class="w-full border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
                  >
                    <option value="MANHA">Manhã</option>
                    <option value="TARDE">Tarde</option>
                  </select>
                </div>
              </div>
            {/if}
          {:else if solicitacaoId && solicitacaoSelecionada === null}
            <p class="text-sm text-orange-500 italic mt-2">
              A solicitação selecionada pode não existir ou não ter exames pendentes. Verifique a lista.
            </p>
          {/if}

          <!-- Local do Agendamento -->
          <div class="flex flex-col mt-4">
            <label for="localAgendado" class="text-sm font-medium text-gray-700 mb-1">
              Local para os Exames Selecionados
            </label>
            <select
              id="localAgendado"
              bind:value={localAgendado}
              class="w-full border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
              required
            >
              <option value="" disabled selected>Selecione o local...</option>
              {#each locais as loc (loc.value)}
                <option value={loc.value}>{loc.label}</option>
              {/each}
            </select>
          </div>

          <!-- Observações -->
          <div class="flex flex-col mt-4">
            <label for="observacoes" class="text-sm font-medium text-gray-700 mb-1">
              Observações Gerais
            </label>
            <textarea
              id="observacoes"
              bind:value={observacoes}
              rows="3"
              class="w-full border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"
            ></textarea>
          </div>

          <!-- Botão de envio -->
          <button
            type="submit"
            class="w-full bg-emerald-800 text-white py-3 rounded-lg hover:bg-emerald-900 transition mt-6"
            disabled={
              !solicitacaoId ||
              !solicitacaoSelecionada ||
              !solicitacaoSelecionada.especialidadesPendentes ||
              solicitacaoSelecionada.especialidadesPendentes.length === 0 ||
              !dataAgendada ||
              !localAgendado
            }
          >
            Agendar Exames Selecionados
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
    height: 1.1em;
    margin-right: 0.5em;
    position: relative;
    vertical-align: middle;
    width: 1.1em;
    cursor: pointer;
    flex-shrink: 0;
  }
  .form-checkbox:checked {
    background-color: #10b981; /* emerald-600 */
    border-color: #059669; /* emerald-700 */
  }
  .form-checkbox:checked::before {
    content: '✓';
    color: white;
    font-size: 0.8em;
    font-weight: bold;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    line-height: 1;
  }
  .form-checkbox:disabled + span {
    color: #9ca3af; /* gray-400 */
    text-decoration: line-through;
  }
  .form-checkbox:disabled {
    background-color: #e5e7eb; /* gray-200 */
    border-color: #d1d5db; /* gray-300 */
    cursor: not-allowed;
  }
</style>
