<script>
    export let data;
    const solicitacoes = data.solicitacoes.content;

    let buscar = '';

    $:filtradas = buscar.trim()?solicitacoes.filter(s => { const termo = buscar.toLowerCase();
      return(
        s.nomePaciente.toLowerCase().includes(termo) ||
        s.cpfPaciente.toLowerCase().includes(termo) ||
        s.usfOrigem.toLowerCase().includes(termo) ||
        s.especialidades.some(esp => esp.especialidadeSolicitada.toLowerCase().includes(termo))||
        s.especialidades.some(esp => esp.prioridade.toLowerCase().includes(termo)) 

      );

      return nomeMath || cpfMath || usfMath || espMath, esphPrioridadeMath;

    })
    : solicitacoes;


  </script>
  
  <div class="flex h-screen">

   <aside class="w-64 bg-gray-800 text-white flex flex-col items-center py-6">

      <nav class="space-y-4 flex flex-col items-center">
         <a href="#" class="hover:text-teal-300">Home</a>
         <a href="#" class="hover:text-teal-300">Solicitações</a>
         <a href="#" class="hover:text-teal-300">INFO2</a>
         <a href="#" class="hover:text-teal-300">INFO3</a>
      </nav>
   </aside>

   <div class="flex-1 flex flex-col">
      <header class="bg-emerald-900 text-white p-4 flex justify-center text-center">
         <h1 class="text-center text-xl">Regula System</h1>
      </header>

      <main class="flex-1 bg-gray-100 overflow-auto">

    


        <h2 class="flex gap-1 p-5 bg-white rounded justify-center items-center font-semibold">Lista de Pacientes Pendentes</h2>
        <div class="grid grid-cols-1 gap-4 m-2 max-w-5xl mx-auto">
            {#if solicitacoes.length === 0}
              <p class="text-center text-gray-500">Nenhuma solicitação encontrada.</p>
            {:else}
              <p class="text-center text-gray-500">Total de solicitações: {solicitacoes.length}</p>
            {/if}

            <div class="p-4">
              <input 
              type="text"
              placeholder="Busque por nome, CPF, Especialidade, Unidade de Saúde ou Prioridade"
              bind:value={buscar}
              class=" w-max bg-white rounded-lg shadow-md p-2
                     hover:shadow-lg transition duration-200
                      w-full mx-auto"
              >
            </div>

            {#each filtradas as s}
              <a
                href={`/solicitacao/${s.id}`}
                class="inline-block bg-white rounded-lg shadow-md p-4
                       hover:shadow-lg transition duration-200
                        w-full mx-auto"
              >
                <h3 class="text-lg font-bold mb-3">{s.nomePaciente}</h3>
    
                <!-- grid interno de 2 colunas -->
                <div class="grid grid-cols-2 gap-x-6 gap-y-2 text-sm">
                  <div class="bg-yellow-200 p-2 rounded-lg max-w-max">
                    <span class="font-semibold">CPF:</span>
                    <span>{s.cpfPaciente}</span>
                  </div>
                  <div>
                    <span class="font-semibold">USF:</span>
                    <span>{s.usfOrigem}</span>
                  </div>
                  <div>
                    <span class="font-semibold">Data:</span>
                    <span>{s.dataMalote}</span>
                  </div>
                  <div>
                    <span class="font-semibold">Prioridade:</span>
                    <span>{s.especialidades[0]?.prioridade}</span>
                  </div>
                  <div class="col-span-2 ">
                    <span class="font-semibold">Especialidade:</span>


                    {#each s.especialidades as esp, i}
                      <span class="bg-emerald-200 p-2 rounded-lg max-w-max mr-0.5">
                        {esp.especialidadeSolicitada}
                        {i < s.especialidades.length - 1 ? ', ' : ''}
                      </span>
                    {/each}


                  </div>
                  <div class="col-span-2">
                    <span class="font-semibold">Observações:</span>
                    <span>{s.observacoes}</span>
                  </div>
                </div>
              </a>
            {/each}

            {#if filtradas.length ===0}
              <p class="text-center text-gray-500">Nenhum solicitação encontrada.</p>
    
              
            {/if}
          </div>
        
          
            
    </main>
   </div>
  </div>

 