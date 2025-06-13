<script>
  export let agendamentos = [];
</script>

<table class="min-w-full border">
  <thead>
    <tr class="bg-gray-200">
      <th class="px-2 py-1">Paciente</th>
      <th class="px-2 py-1">CPF</th>
      <th class="px-2 py-1">Especialidade</th>
      <th class="px-2 py-1">Local</th>
      <th class="px-2 py-1">Data e Hora</th>
      <th class="px-2 py-1">Observações</th>
    </tr>
  </thead>
  <tbody>
    {#each agendamentos as a}
      <tr class="even:bg-gray-50">
        <td class="px-2 py-1">{a.nomePaciente}</td>
        <td class="px-2 py-1">{a.cpfPaciente}</td>
        <td class="px-2 py-1">{a.especialidade}</td>
        <td class="px-2 py-1">{a.localAgendado.replace(/_/g,' ')}</td>
        <td class="px-2 py-1">
          {new Date(a.dataAgendada)
            .toLocaleString('pt-BR', { hour12: false })}
        </td>
        <td class="px-2 py-1">{a.observacoes}</td>
      </tr>
    {/each}
  </tbody>
</table>
