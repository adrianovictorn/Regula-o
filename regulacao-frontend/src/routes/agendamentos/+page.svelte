<script lang="ts">
  import { onMount } from 'svelte';
  import { jsPDF } from 'jspdf';
  // autoTable importado mas não usado diretamente, mantido por compatibilidade com seu código original
  import autoTable from 'jspdf-autotable'; 

  // dados vindos do load()
  export let data: {
    agendamentos: {
      solicitacaoId: number;
      nomePaciente: string;
      cpfPaciente: string;
      usfOrigem: string;
      especialidadesPendentes: string[];
    }[];
  };

  // lista de solicitações pendentes
  let pendentes = data.agendamentos;

  // dados do formulário
  let solicitacaoId: number | null = null;
  let nomePaciente = '';
  let cpfPaciente = '';
  let usfOrigem = '';
  let especialidadesPendentes: string[] = [];
  let especialidadeSolicitada: string | null = null;

  let dataAgendada = '';
  let turno: 'MANHA' | 'TARDE' = 'MANHA';
  let localAgendado = ''; // Este bind:value deve armazenar o valor do Enum (e.g., 'MULTIIMAGEM')
  let observacoes = '';

  // lista de locais com nome de exibição (mantendo o valor do Enum para o backend)
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
  const brasaoUrl = 'https://upload.wikimedia.org/wikipedia/commons/9/97/Bras%C3%A3o_de_Concei%C3%A7%C3%A3o_do_Almeida.png';

  // Função para obter o label do local a partir do seu valor (Enum)
  function getLocalLabel(value: string) {
    const found = locais.find(loc => loc.value === value);
    return found ? found.label : value.replace(/_/g, ' '); // Fallback para o replace se não encontrar
  }

  // Função auxiliar para carregar a imagem de forma assíncrona
  async function loadImage(url: string): Promise<HTMLImageElement> {
    return new Promise((resolve, reject) => {
      const img = new Image();
      img.onload = () => resolve(img);
      img.onerror = (e) => reject(new Error(`Falha ao carregar imagem: ${url}. Erro: ${e}`));
      img.crossOrigin = 'Anonymous'; // Importante para evitar problemas de CORS em alguns casos
      img.src = url;
    });
  }

  // quando o usuário escolhe uma solicitação
  function onSelectSolicitacao(ev: Event) {
    const id = +(ev.target as HTMLSelectElement).value;
    const sel = pendentes.find(p => p.solicitacaoId === id);
    if (sel) {
      solicitacaoId = sel.solicitacaoId;
      nomePaciente = sel.nomePaciente;
      cpfPaciente = sel.cpfPaciente;
      usfOrigem = sel.usfOrigem;
      especialidadesPendentes = sel.especialidadesPendentes;
      especialidadeSolicitada = null; // reset
    }
  }

  async function submeterForm() {
    // 1) grava no back
    const res = await fetch(`api/agendamentos/${solicitacaoId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        especialidadeSolicitada,
        localAgendado, // Envia o valor do Enum para o backend
        dataAgendada,
        observacoes
      })
    });

    const doc = new jsPDF({ unit: 'pt', format: 'a4', orientation: 'portrait' });
    const margin = { top: 80, left: 40, right: 40 };
    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();

    let currentY = margin.top;

    // --- Adicionar o brasão da URL ---
    const imgWidth = 60; // Largura desejada para a imagem
    const imgHeight = 60; // Altura desejada para a imagem
    const imgX = (pageWidth / 2) - (imgWidth / 2); // Centralizar horizontalmente
    const imgY = margin.top - 70; // Posição acima do cabeçalho "Central de Regulação"

    try {
      const brasaoImage = await loadImage(brasaoUrl);
      doc.addImage(brasaoImage, 'PNG', imgX, imgY, imgWidth, imgHeight);
      console.log('Brasão carregado da URL e adicionado com sucesso!');
    } catch (error) {
      console.error('Erro ao adicionar brasão:', error);
      alert('Ocorreu um erro ao adicionar o brasão ao PDF. O PDF será gerado sem ele. Verifique o console para mais detalhes.');
      // Opcional: Desenhe um placeholder se a imagem não carregar
      doc.setFontSize(8);
      doc.setTextColor('#b91c1c');
      doc.text('Brasão indisponível', imgX, imgY + (imgHeight / 2));
    }
    // --- Fim da adição do brasão ---

    // Ajuste o currentY para dar espaço após o brasão
    currentY = imgY + imgHeight + 30; // Começa o texto do cabeçalho abaixo do brasão com um espaçamento

    // Cabeçalho
    doc.setFontSize(20);
    doc.setTextColor('#115e59');
    doc.setFont('helvetica', 'bold');
    doc.text('Central de Regulação', pageWidth / 2, currentY, { align: 'center' });
    currentY += 24;
    doc.setFontSize(14);
    doc.setTextColor('#333');
    doc.text('Conceição do Almeida', pageWidth / 2, currentY, { align: 'center' });

    currentY += 40;

    // Título do Comprovante
    doc.setFontSize(18);
    doc.setTextColor('#115e59');
    doc.setFont('helvetica', 'bold');
    doc.text('Comprovante de Agendamento', pageWidth / 2, currentY, { align: 'center' });

    currentY += 30;

    const informacoes = [
      { label: 'ID Solicitação', value: solicitacaoId },
      { label: 'Paciente', value: nomePaciente },
      { label: 'CPF', value: cpfPaciente },
      { label: 'USF Origem', value: usfOrigem },
      { label: 'Especialidade', value: especialidadeSolicitada },
      // Formata a data para DD/MM/YYYY
      { label: 'Data Agendada ', value: dataAgendada ? new Date(dataAgendada + 'T00:00:00').toLocaleDateString('pt-BR') : '' },
      { label: 'Turno', value: turno },
      { label: 'Local', value: getLocalLabel(localAgendado) }, // Usa a função para exibir o nome correto
      { label: 'Observações', value: observacoes || '–' }
    ];

    // Alternar cores e adicionar um leve padding/margin visual
    let bgColor1 = '#f5f5f5';
    let bgColor2 = '#ffffff';
    const rowHeight = 30; // Altura de cada linha de informação
    const textIndent = 10; // Recuo do texto dentro da caixa colorida

    informacoes.forEach((info, index) => {
      const bgColor = index % 2 === 0 ? bgColor1 : bgColor2;

      // Fundo colorido para a linha de informação
      doc.setFillColor(bgColor);
      doc.rect(margin.left, currentY - 12, pageWidth - margin.left - margin.right, rowHeight, 'F');

      // Texto do label
      doc.setFontSize(12);
      doc.setTextColor('#333');
      doc.setFont('helvetica', 'bold');
      doc.text(`${info.label}:`, margin.left + textIndent, currentY);

      // Texto do valor
      doc.setFont('helvetica', 'normal');
      doc.setTextColor('#333'); // Reset para cor padrão

      // Enfatizar Local, Procedimento e Data
      if (['Local', 'Especialidade', 'Data Agendada'].some(chave => info.label.includes(chave))) {
        doc.setTextColor('#115e59'); // Cor de destaque
        doc.setFont('helvetica', 'bold');
        doc.setFontSize(13); // Tamanho de destaque
      }

      // Se for Observações, calcular altura e espaço maior
      if (info.label === 'Observações') {
        const obsText = String(info.value);
        // Ajustar largura para o texto de observações e posição vertical
        const splitText = doc.splitTextToSize(obsText, pageWidth - margin.left - margin.right - textIndent - 100); 
        doc.text(splitText, margin.left + 100, currentY);
        currentY += splitText.length * 14 + 10; // Ajuste dinâmico da altura da linha
      } else {
        doc.text(`${info.value}`, margin.left + 100, currentY);
        currentY += rowHeight; // Avança para a próxima linha
      }

      // Reset estilo para a próxima iteração
      doc.setFont('helvetica', 'normal');
      doc.setFontSize(12);
      doc.setTextColor('#333');
    });

    currentY += 20;

    // Orientação final
    doc.setFontSize(12);
    doc.setTextColor('#b91c1c'); // Vermelho para chamar atenção
    doc.setFont('helvetica', 'bold');
    doc.text('Orientação:', margin.left, currentY);

    currentY += 20;
    doc.setFont('helvetica', 'normal');
    doc.setTextColor('#333');
    doc.text('Confirmar ou desmarcar com antecedência o atendimento: (75) 98312-2478', margin.left, currentY);

    currentY += 40;

    // Rodapé
    const hoje = new Date().toLocaleDateString('pt-BR'); // Já está no formato DD/MM/YYYY
    doc.setFontSize(10);
    doc.setTextColor('#999');
    doc.text(`Emitido em: ${hoje}`, margin.left, pageHeight - 30);
    doc.text(`Regula System`, pageWidth - margin.right, pageHeight - 30, { align: 'right' });
    doc.text(`Sistema Desenvolvido por Adriano Victor N. Ribeiro, Filipe Ribeiro`, margin.left, pageHeight - 45);
    doc.save(`comprovante_agendamento_${solicitacaoId}.pdf`);
  }
</script>

<div class="flex h-screen bg-gray-100">

  <aside class="w-64 bg-gray-800 text-white flex flex-col py-8 shadow-lg">
    <h2 class="text-2xl font-bold text-center mb-8">Regula System</h2>
    <nav class="flex-1 flex flex-col space-y-2 px-6">
      <a href="/home" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Dashboard</a>
      <a href="/cadastrar" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Nova Solicitação</a>
      <a href="/exames" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Laboratório</a>
      <a href="/agendamentos" class="py-2 px-4 rounded bg-emerald-700">Agendamento</a>
      <a href="/paciente" class="py-2 px-4 rounded hover:bg-emerald-800">Paciente</a>
      <a href="/" class="py-2 px-4 rounded hover:bg-emerald-800 transition">Exportar Dados</a>
    </nav>
    <div class="px-6 mt-4 text-sm text-emerald-200">v1.0 • Adriano Victor, Filipe Ribeiro © 2025</div>
  </aside>

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Agendar Consulta</h1>
      <div>Bem-vindo(a), Usuário</div>
    </header>

    <main class="flex-1 overflow-auto p-6">
      <div class="bg-white rounded-lg shadow-lg p-6">
        <h2 class="text-2xl font-bold text-emerald-800 mb-6">Novo Agendamento</h2>

        <form on:submit|preventDefault={submeterForm} class="space-y-6">
          <div class="flex flex-col">
            <label class="text-sm font-medium text-gray-700 mb-1">Selecionar Solicitação</label>
            <select bind:value={solicitacaoId} on:change={onSelectSolicitacao} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500">
              <option value="" disabled selected>Selecione a solicitação</option>
              {#each pendentes as p}
                <option value={p.solicitacaoId}>{p.nomePaciente} — {p.cpfPaciente} — {p.usfOrigem}</option>
              {/each}
            </select>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="text-sm font-medium text-gray-700 mb-1">Nome do Paciente</label>
              <input type="text" value={nomePaciente} readonly class="w-full bg-gray-100 border border-gray-300 rounded-lg p-2" />
            </div>
            <div>
              <label class="text-sm font-medium text-gray-700 mb-1">CPF</label>
              <input type="text" value={cpfPaciente} readonly class="w-full bg-gray-100 border border-gray-300 rounded-lg p-2" />
            </div>
            <div>
              <label class="text-sm font-medium text-gray-700 mb-1">USF Origem</label>
              <input type="text" value={usfOrigem} readonly class="w-full bg-gray-100 border border-gray-300 rounded-lg p-2" />
            </div>
          </div>

          <div class="flex flex-col">
            <label class="text-sm font-medium text-gray-700 mb-1">Especialidade a Agendar</label>
            <select bind:value={especialidadeSolicitada} disabled={!especialidadesPendentes.length} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500">
              <option value="" disabled selected>
                {#if !especialidadesPendentes.length}Selecione a solicitação primeiro{:else}Escolha a especialidade{/if}
              </option>
              {#each especialidadesPendentes as esp}
                <option value={esp}>{esp}</option>
              {/each}
            </select>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="text-sm font-medium text-gray-700 mb-1">Data Agendada</label>
              <input type="date" bind:value={dataAgendada} class="w-full border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
            </div>
            <div>
              <label class="text-sm font-medium text-gray-700 mb-1">Turno</label>
              <select bind:value={turno} class="w-full border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500">
                <option value="MANHA">Manhã</option>
                <option value="TARDE">Tarde</option>
              </select>
            </div>
          </div>

          <div class="flex flex-col">
            <label class="text-sm font-medium text-gray-700 mb-1">Local do Agendamento</label>
            <select bind:value={localAgendado} class="w-full border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required>
              <option value="" disabled selected>Selecione o local...</option>
              {#each locais as loc}
                <option value={loc.value}>{loc.label}</option>
              {/each}
            </select>
          </div>

          <div class="flex flex-col">
            <label class="text-sm font-medium text-gray-700 mb-1">Observações</label>
            <textarea bind:value={observacoes} rows="3" class="w-full border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"></textarea>
          </div>

          <button type="submit" class="w-full bg-emerald-800 text-white py-3 rounded-lg hover:bg-emerald-900 transition">Agendar</button>
        </form>
      </div>
    </main>
  </div>
</div>