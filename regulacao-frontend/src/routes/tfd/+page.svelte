<script lang="ts">
    import { getApi } from "$lib/api";



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