<script lang="ts">
  import { onMount } from 'svelte';
  import { jsPDF } from 'jspdf';
  import autoTable from 'jspdf-autotable';
  import { getApi, postApi } from '$lib/api';
  import { listarEspecialidadesCatalogo } from '$lib/especialidadesApi.js';
    import Menu from '$lib/Menu.svelte';
    import UserMenu from '$lib/UserMenu.svelte';

  interface Cidade {
        id: number
        nomeCidade: string
        codigoIBGE: string
        cep: string
    }

  interface LocalAgendamento {
    id: number
    nomeLocal: string
    endereco: string
    numero: string
    cidade?: Cidade | null
    cidadeId?: number | null
    cidadeNome?: string | null
  }

  let locaisAgendamento = $state<LocalAgendamento[]>([]);
  // --- Estado do Componente ---
  let solicitacoes = $state<any[]>([]);
  let isLoading = $state(true);
  let error = $state('');

  // Estado do formulário
  let solicitacaoId = $state('');
  let examesSelecionados = $state<string[]>([]);
  let dataAgendada = $state('');
  let turno = $state<'MANHA' | 'TARDE'>('MANHA');
  let localAgendamentoId = $state('');
  let observacoes = $state('');
  let valorBusca = $state('');
  let comboboxAberto = $state(false);

  // <<< NOVO: Dicionário e função para traduzir nomes de exames >>>
  // 1. Criamos um mapa para busca rápida dos nomes amigáveis.
  let especialidadeLabelMap = new Map<string, string>();
  let catalogoCarregado = false;
  async function carregarCatalogoEspecialidades() {
    if (catalogoCarregado && especialidadeLabelMap.size > 0) {
      return;
    }
    try {
      const lista = await listarEspecialidadesCatalogo();
      especialidadeLabelMap = new Map(lista.map((e:any) => [e.codigo, e.nome]));
      catalogoCarregado = true;
    } catch (e) {
      // mantém vazio; fallback usa replace('_',' ')
      console.warn('Falha ao carregar catálogo de especialidades', e);
    }
  }

  // 2. Função auxiliar para obter o nome amigável.
  function getEspecialidadeLabel(valor: string): string {
    return especialidadeLabelMap.get(valor) || valor.replace(/_/g, ' ');
  }
  // <<< FIM DA SEÇÃO NOVA >>>


  async function carregarSolicitacoesPendentes() {
    isLoading = true;
    error = '';
    try {
      const response = await getApi('agendamentos/pendentes');
      if (!response.ok) {
        throw new Error('Erro ao carregar as solicitações pendentes.');
      }
      solicitacoes = await response.json();
    } catch (e: any) {
      error = e.message;
      alert(e.message);
    } finally {
      isLoading = false;
    }
  }

  async function carregarLocaisAgendamento() {
    try {
      const res = await getApi('local/agendamento');

      if (!res.ok) {
        throw new Error('Erro ao receber dados do servidor!');
      }

      const data: LocalAgendamento[] = await res.json();
      locaisAgendamento = data;

      if (localAgendamentoId && !data.some((loc) => String(loc.id) === String(localAgendamentoId))) {
        localAgendamentoId = '';
      }
    } catch (error) {
      console.error(error);
      alert('Erro ao se conectar ao servidor!');
    }
  }

  onMount(async () => {
    await Promise.all([
      carregarCatalogoEspecialidades(),
      carregarSolicitacoesPendentes(),
      carregarLocaisAgendamento()
    ]);
  });

  let solicitacaoSelecionada = $derived(
    solicitacoes.find((s) => String(s.solicitacaoId) === String(solicitacaoId)) || null
  );

 

  
  const brasaoUrl =
    'https://upload.wikimedia.org/wikipedia/commons/9/97/Bras%C3%A3o_de_Concei%C3%A7%C3%A3o_do_Almeida.png';

  function getLocalLabel(value: string) {
    const found = locaisAgendamento.find((loc) => String(loc.id) === String(value));
    if (!found) {
      return 'Local não informado';
    }
    return found.cidadeNome ? `${found.nomeLocal} - ${found.cidadeNome}` : found.nomeLocal;
  }

  async function loadImage(url: string): Promise<HTMLImageElement> {
    return new Promise((resolve, reject) => {
      const img = new Image();
      img.onload = () => resolve(img);
      img.onerror = (e) => reject(new Error(`Falha ao carregar imagem: ${url}. Erro: ${e}`));
      img.crossOrigin = 'Anonymous';
      img.src = url;
    });
  }

  const solicitacoesFiltradas = $derived(()=> {
    if(!valorBusca){
      return solicitacoes;
    }
    return solicitacoes.filter(s => s.nomePaciente.toLowerCase().includes(valorBusca.toLowerCase() || s.cpfPaciente.includes(valorBusca))); 

  })

  // Adicione esta função
function selecionarSolicitacao(solicitacao) {
  // Preenche o campo de busca com o nome do paciente para feedback visual
  valorBusca = `${solicitacao.nomePaciente} — ${solicitacao.cpfPaciente}`; 

  // Define o ID da solicitação na sua variável de estado principal
  solicitacaoId = solicitacao.solicitacaoId; 

  // Fecha a lista de opções
    comboboxAberto = false;
  }


  async function gerarComprovantePDF(dadosPDF: {
    solicitacaoId: number;
    nomePaciente: string;
    cpfPaciente: string;
    usfOrigem: string;
    cns?: string;
    examesSelecionados: string[];
    dataAgendada: string;
    turno: 'MANHA' | 'TARDE';
    localAgendamentoId: string;
    observacoes: string;
  }) {
    const doc = new jsPDF({ unit: 'pt', format: 'a4', orientation: 'portrait' });
    const margin = { top: 80, left: 40, right: 40, bottom: 60 };
    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();
    let currentY = margin.top;
    
    const corDestaque = '#0D7244';
    const corPadrao = '#1f2937';
    const corLabel = '#374151';

    const imgWidth = 60;
    const imgHeight = 60;
    const imgX = pageWidth / 2 - imgWidth / 2;
    const imgY = margin.top - 70;

    try {
      const brasaoImage = await loadImage(brasaoUrl);
      doc.addImage(brasaoImage, 'PNG', imgX, imgY, imgWidth, imgHeight);
    } catch (error) {
      console.error('Erro ao adicionar brasão ao PDF:', error);
    }

    currentY = imgY + imgHeight + 25;
    
    doc.setFontSize(20);
    doc.setTextColor('#115e59');
    doc.setFont('helvetica', 'bold');
    doc.text('Central de Regulação', pageWidth / 2, currentY, { align: 'center' });
    currentY += 24;
    doc.setFontSize(14);
    doc.setTextColor('#333333');
    doc.setFont('helvetica', 'normal');
    doc.text('Conceição do Almeida', pageWidth / 2, currentY, { align: 'center' });
    currentY += 40;
    doc.setFontSize(18);
    doc.setFont('helvetica', 'bold');
    doc.text('Comprovante de Agendamento', pageWidth / 2, currentY, { align: 'center' });
    currentY += 35;

    // <<< ALTERADO: Traduz os nomes dos exames antes de criar a lista de infos >>>
    const nomesAmigaveis = dadosPDF.examesSelecionados.map(exame => getEspecialidadeLabel(exame));

    const allInfo = [
        { label: 'ID Solicitação', value: String(dadosPDF.solicitacaoId), color: corDestaque, style: 'bold' },
        { label: 'Paciente',       value: dadosPDF.nomePaciente, color: corPadrao, style: 'bold' },
        { label: 'CPF',            value: dadosPDF.cpfPaciente, color: corPadrao },
        { label: 'USF Origem',     value: dadosPDF.usfOrigem, color: corPadrao },
        ...(dadosPDF.cns ? [{ label: 'CNS', value: dadosPDF.cns, color: corPadrao }] : []),
        // Usa a lista de nomes traduzidos
        { label: 'Exames Agendados', value: nomesAmigaveis.join(', '), color: corDestaque, style: 'bold' },
        { label: 'Data Agendada',  value: new Date(dadosPDF.dataAgendada + 'T00:00:00').toLocaleDateString('pt-BR'), color: corDestaque, style: 'bold' },
        { label: 'Turno',          value: dadosPDF.turno === 'MANHA' ? 'Manhã' : 'Tarde', color: corPadrao },
        { label: 'Local',          value: getLocalLabel(dadosPDF.localAgendamentoId), color: corDestaque, style: 'bold' },
        { label: 'Observações',    value: dadosPDF.observacoes || 'Nenhuma', color: corPadrao }
    ];

    allInfo.forEach((info, index) => {
        const bgColor = index % 2 === 0 ? '#f3f4f6' : '#ffffff';
        const lines = doc.splitTextToSize(String(info.value), pageWidth - margin.left - margin.right - 130);
        const rowHeight = Math.max(28, lines.length * 14 + 14);
        
        doc.setFillColor(bgColor);
        doc.rect(margin.left, currentY - 14, pageWidth - margin.left - margin.right, rowHeight, 'F');
        
        doc.setFontSize(11);
        doc.setFont('helvetica', 'bold');
        doc.setTextColor(corLabel);
        doc.text(info.label, margin.left + 10, currentY);

        const style = info.style || 'normal';
        const color = info.color || corPadrao;
        
        doc.setFont('helvetica', style as any);
        doc.setTextColor(color);
        doc.text(lines, margin.left + 130, currentY);
        
        currentY += rowHeight;
    });

    currentY += 20;
    doc.setFontSize(11);
    doc.setTextColor('#dc3545');
    doc.setFont('helvetica', 'bold');
    doc.text('Orientação Importante:', margin.left, currentY);
    currentY += 18;
    doc.setFont('helvetica', 'normal');
    doc.setTextColor('#212529');
    doc.text('• Comparecer com 15 minutos de antecedência.', margin.left + 10, currentY);
    currentY += 16;
    doc.text('• Levar RG, CPF, Cartão do SUS e este comprovante.', margin.left + 10, currentY);
    currentY += 16;
    doc.text('• Em caso de não comparecimento, favor informar com antecedência.', margin.left + 10, currentY);

    const hoje = new Date().toLocaleDateString('pt-BR');
    const hora = new Date().toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    doc.setFontSize(9);
    doc.setTextColor('#6c757d');
    doc.text(`Emitido em: ${hoje} às ${hora}`, margin.left, pageHeight - margin.bottom + 20);
    doc.text(`SIRGE System v1.0`, pageWidth - margin.right, pageHeight - margin.bottom + 20, { align: 'right' });

    doc.save(`comprovante_${dadosPDF.nomePaciente.replace(/\s+/g, '_')}_${dadosPDF.solicitacaoId}.pdf`);
  }

  function enviarAgendamento(event: SubmitEvent) {
    event.preventDefault(); // Prevenção do comportamento padrão

    if (!solicitacaoSelecionada) { // Simplificado para checar apenas a solicitação selecionada
      alert('Por favor, selecione uma solicitação válida.');
      return;
    }
    if (examesSelecionados.length === 0) {
      alert('Selecione pelo menos um exame para agendar.');
      return;
    }
    if (!dataAgendada || !localAgendamentoId) {
      alert('Preencha a data e o local do agendamento.');
      return;
    }

    const localIdNumber = localAgendamentoId ? Number(localAgendamentoId) : null;

   const body: Record<string, unknown> = {
     examesSelecionados,
     dataAgendada,
     observacoes,
     turno,
      localAgendado: null
    };

    if (localIdNumber !== null) {
      body.localAgendamentoId = localIdNumber;
    } else {
      body.localAgendamentoId = null;
    }

    try {
      postApi(`agendamentos/${solicitacaoId}`, body).then(async (resposta) => {
        if (resposta.ok) {
          alert('Agendamento realizado com sucesso!');

          await gerarComprovantePDF({
            solicitacaoId: solicitacaoSelecionada.solicitacaoId,
            nomePaciente: solicitacaoSelecionada.nomePaciente,
            cpfPaciente: solicitacaoSelecionada.cpfPaciente,
            usfOrigem: solicitacaoSelecionada.usfOrigem,
            cns: solicitacaoSelecionada.cns,
            examesSelecionados,
            dataAgendada,
            turno,
            localAgendamentoId,
            observacoes
          });

          // Resetar formulário
          solicitacaoId = '';
          examesSelecionados = [];
          dataAgendada = '';
          localAgendamentoId = '';
          observacoes = '';
          turno = 'MANHA';
          valorBusca = '';

          await carregarSolicitacoesPendentes();
          await carregarLocaisAgendamento();
        } else {
          let mensagemErro = '';
          try {
            const texto = await resposta.text();
            mensagemErro = texto ? JSON.parse(texto).message ?? texto : '';
          } catch {
            // mantém mensagemErro vazio caso parsing falhe
          }

          if (!mensagemErro) {
            mensagemErro = resposta.status === 403
              ? 'Acesso negado. Verifique se você está autenticado e possui permissão para agendar.'
              : 'Verifique os dados e tente novamente.';
          }

          alert(`Erro ao agendar: ${mensagemErro}`);
        }
      });
    } catch (err) {
      console.error('Erro na submissão do formulário:', err);
      alert('Erro de conexão. Verifique sua rede e tente novamente.');
    }
  }
</script>

<svelte:head>
    <title>Agendamento</title>
</svelte:head>

<div class="flex h-screen bg-gray-100">
  <Menu activePage="/agendar" />

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Agendar Múltiplos Exames</h1>
          <UserMenu/>
    </header>

    <main class="flex-1 overflow-auto p-6">
      {#if isLoading}
        <div class="text-center p-10">
          <p class="text-lg text-gray-600">Carregando solicitações...</p>
        </div>
      {:else if error}
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
          <strong class="font-bold">Ocorreu um erro:</strong>
          <span class="block sm:inline">{error}</span>
        </div>
      {:else}
        <div class="bg-white rounded-lg shadow-lg p-6">
          <h2 class="text-2xl font-bold text-emerald-800 mb-6">Novo Agendamento</h2>

          <form class="space-y-6" onsubmit={enviarAgendamento}>
            <div class="flex flex-col">
              <label for="selectSolicitacao" class="text-sm font-medium text-gray-700 mb-1">
                Selecionar Solicitação Pendente
              </label>
             <div class="relative">
                    <input 
                      id="combobox-agendamento"
                      type="text" 
                      bind:value={valorBusca}
                      onfocus={() => comboboxAberto = true}
                      onblur={() => setTimeout(() => { comboboxAberto = false }, 150)}
                      placeholder="Digite o nome ou CPF para buscar..."
                      class="border border-gray-300 rounded-lg p-2 w-full focus:ring-emerald-500 focus:border-emerald-500"
                    />

                    {#if comboboxAberto && solicitacoesFiltradas().length > 0}
                      <ul class="absolute z-10 w-full bg-white border border-gray-200 rounded-lg mt-1 max-h-60 overflow-y-auto shadow-lg">
                        {#each solicitacoesFiltradas() as s (s.solicitacaoId)}
                          <li 
                            onmousedown={() => selecionarSolicitacao(s)}
                            class="p-3 hover:bg-emerald-100 cursor-pointer"
                          >
                            {s.nomePaciente} — {s.cpfPaciente}
                          </li>
                        {/each}
                      </ul>
                    {/if}
</div>
              

            {#if solicitacaoSelecionada}
              <div class="border-t pt-6 space-y-4">
                <h3 class="text-lg font-semibold text-gray-800">Dados do Paciente</h3>
                <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
                  <div>
                    <label class="text-sm font-medium text-gray-700">Nome</label>
                    <input type="text" value={solicitacaoSelecionada.nomePaciente} readonly class="w-full bg-gray-100 border-gray-300 rounded-lg p-2" />
                  </div>
                  <div>
                    <label class="text-sm font-medium text-gray-700">CPF</label>
                    <input type="text" value={solicitacaoSelecionada.cpfPaciente} readonly class="w-full bg-gray-100 border-gray-300 rounded-lg p-2" />
                  </div>
                  <div>
                    <label class="text-sm font-medium text-gray-700">USF Origem</label>
                    <input type="text" value={solicitacaoSelecionada.usfOrigem} readonly class="w-full bg-gray-100 border-gray-300 rounded-lg p-2" />
                  </div>
                   <div>
                    <label class="text-sm font-medium text-gray-700">CNS</label>
                    <input type="text" value={solicitacaoSelecionada.cns || 'N/A'} readonly class="w-full bg-gray-100 border-gray-300 rounded-lg p-2" />
                  </div>
                </div>

                <div class="flex flex-col mt-4">
                  <label class="text-sm font-medium text-gray-700 mb-2">Exames Pendentes:</label>
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
                          <span class="text-gray-700">{getEspecialidadeLabel(especialidadeNome)}</span>
                        </label>
                      {/each}
                    </div>
                  {:else}
                    <p class="text-sm text-gray-500 italic mt-2">Nenhum exame pendente para esta solicitação.</p>
                  {/if}
                </div>

                {#if solicitacaoSelecionada.especialidadesPendentes.length > 0}
                  <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-4">
                    <div>
                      <label for="dataAgendada" class="text-sm font-medium text-gray-700 mb-1">Data do Agendamento</label>
                      <input type="date" id="dataAgendada" bind:value={dataAgendada} class="w-full border border-gray-300 rounded-lg p-2" required />
                    </div>
                    <div>
                      <label for="turno" class="text-sm font-medium text-gray-700 mb-1">Turno</label>
                      <select id="turno" bind:value={turno} class="w-full border border-gray-300 rounded-lg p-2">
                        <option value="MANHA">Manhã</option>
                        <option value="TARDE">Tarde</option>
                      </select>
                    </div>
                  </div>
                  <div class="flex flex-col mt-4">
                    <label for="localAgendamentoId" class="text-sm font-medium text-gray-700 mb-1">Local do Agendamento</label>
                    <select id="localAgendamentoId" bind:value={localAgendamentoId} class="w-full border border-gray-300 rounded-lg p-2" required>
                      <option value="" disabled>Selecione o local...</option>
                      {#if locaisAgendamento.length === 0}
                        <option disabled>Nenhum local cadastrado</option>
                      {:else}
                        {#each locaisAgendamento as loc}
                          <option value={loc.id}>
                            {loc.nomeLocal}
                            {#if loc.cidadeNome}
                              {' '}- {loc.cidadeNome}
                            {/if}
                          </option>
                        {/each}
                      {/if}
                    </select>
                  </div>
                  <div class="flex flex-col mt-4">
                    <label for="observacoes" class="text-sm font-medium text-gray-700 mb-1">Observações</label>
                    <textarea id="observacoes" bind:value={observacoes} rows="3" class="w-full border border-gray-300 rounded-lg p-2"></textarea>
                  </div>

                  <button
                    type="submit"
                    class="w-full bg-emerald-800 text-white py-3 rounded-lg hover:bg-emerald-900 transition mt-6 disabled:bg-gray-400"
                    disabled={examesSelecionados.length === 0}
                  >
                    Agendar Exames Selecionados
                  </button>
                {/if}
              </div>
            {/if}
          </form>
        </div>
      {/if}
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
    position: relative;
    vertical-align: middle;
    width: 1.1em;
    cursor: pointer;
    flex-shrink: 0;
  }
  .form-checkbox:checked {
    background-color: #10b981;
    border-color: #059669;
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
</style>
