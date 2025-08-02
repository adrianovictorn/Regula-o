<script>
  export let usuario;
  export let aberto = false;
  export let onClose = () => {

  };
  export let onSave = (data) => {};

  let nome = "";
  let cpf = "";
  let password = "";
  let role = "";

    $: if (aberto){
        nome = usuario?.nome ?? "";
        cpf = usuario?.cpf ?? "";
        password = usuario?.password ?? "";
        role = usuario?.role ?? "";
    }
</script>

{#if aberto}
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-xl max-w-md shadow">
       <input type="text" bind:value={nome} class="border rounded p-2 w-full mb-2" />
        <input type="text" bind:value={cpf} class="border rounded p-2 w-full mb-2" />
        <input type="password" bind:value={password} class="border rounded p-2 w-full mb-2" />
        <select bind:value={role} class="border rounded p-2 w-full mb-2">
            <option value="ADMIN">Administrador</option>
            <option value="USER">Usuário Padrão</option>
            <option value="ENFERMEIRO">Enfermeiro</option>
            <option value="MEDICO">Médico</option>
            <option value="RECEPCIONISTA">Recepcionista</option>
        </select>

        <div class="mt-4 flex justify-end gap-2">
            <button 
            type="button"
            class="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400"
            on:click={onClose}>Cancelar</button>

              <button
                type="button"
                class="px-4 py-2 rounded bg-emerald-600 hover:bg-emerald-700 text-white"
                on:click={() =>
                onSave({
                    id: usuario.id,
                    nome,
                    cpf,
                    password,
                    role,
                })
                }
            >
                Salvar
            </button>
        </div>

        
        Editar Usuário
    </div>
  </div>
{/if}
