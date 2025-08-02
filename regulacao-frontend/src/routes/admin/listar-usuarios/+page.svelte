<script>
    import { getApi } from "$lib/api";
    import Menu from "$lib/Menu.svelte";
    import UserHome from "$lib/UserHome.svelte";
    import UserMenu from "$lib/UserMenu.svelte";
    import { error } from "@sveltejs/kit";
    import { onMount } from "svelte";


    let usuarios = $state('');
async function listarUsuarios() {
    try {
        const res = await getApi("users");
        if(!res.ok){
            alert ('Erro ao receber dados');
        }

        return usuarios = res.json();
    } catch(e){
        error(e);
    }
}

onMount( () => {
    listarUsuarios();
})

</script>


<div class="flex h-screen bg-gray-100">
  
  <Menu activePage="/admin/cadastrar-usuario" />

  <div class="flex-1 flex flex-col">
    <header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
      <h1 class="text-xl font-semibold">Painel de Controle</h1>
    </header>

      <main class="flex gap-2 flex">
            
        <div>
            {#each usuarios as users}
            <div>
                <p>{users.role}</p>
                <p>{users.nome}</p>
            </div>
            {/each}
        </div>
      </main>
  </div>


</div>