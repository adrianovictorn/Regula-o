<script lang="ts">
    import { getApi } from "$lib/api";


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
    { value: 'HOSPITAL_SANTA_IZABEL', label: 'Hospital Santa Izabel' },
    { value: 'CLINICA_MURITIBA', label: 'Clínica Muritiba' },
    { value: 'HOSPITAL_MARTAGAO_GESTEIRA', label: 'Hospital Martagão Gesteira' },
    { value: 'ADAB', label: 'ADAB' },
    { value: 'HOSPITAL_DA_CRIANCA', label: 'Hospital da Criança' }
  ];

  interface Agendamento{
    id: BigInt
    localAgendado: String
    dataAgendada: Date
    observacoes: String
    solicitacaoId: BigInt
    turno: String


  }

  let AgendamentosRecebidos = $state<Agendamento | null> (null)


  async function agendamentosForaDoMunicipio() {

    try{
        const res = await getApi("/agendamentos")

        if(!res.ok){
            alert('Erro ao receber dados dos Agendamentos !')
        }

        const data:Agendamento = await res.json();
        AgendamentosRecebidos = data;
    }catch{

    }
    
  }

</script>