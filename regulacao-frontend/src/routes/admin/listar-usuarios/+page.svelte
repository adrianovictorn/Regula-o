<script>
    import { getApi } from "$lib/api";
    import Menu from "$lib/Menu.svelte";
    import ModalEditarUsuarios from "$lib/ModalEditarUsuarios.svelte";
    import UserHome from "$lib/UserHome.svelte";
    import UserMenu from "$lib/UserMenu.svelte";

    import { error } from "@sveltejs/kit";
    import { onMount } from "svelte";


    let usuarios = $state([]);
    let modalAberto =  $state(false);;
    let usuarioSelecionado = $state(null);

    function abrirModal(user){
        usuarioSelecionado = user;
        modalAberto = true;
        console.log('Passou aqui')
    }

    function fecharModal(){
        modalAberto = false;
    }
async function listarUsuarios() {
    try {
        const res = await getApi("users");
        if(!res.ok){
            alert ('Erro ao receber dados');
            return
        }

        return usuarios = await res.json();
    } catch(e){
        error(e);
    }
}

async function atualizarUsuario(userData) {
    try {
        // Faz a requisição para a API
        const res = await fetch(`/api/users/${userData.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(userData),
        });

        // Verifica se a requisição foi bem-sucedida
        if (res.ok) {
            // Pega os dados atualizados do usuário que a API retornou
            const usuarioAtualizado = await res.json();

            // Atualiza a lista local de usuários de forma reativa
            // O .map cria um novo array, o que ajuda o Svelte a detectar a mudança
            usuarios = usuarios.map((u) =>
                u.id === usuarioAtualizado.id ? usuarioAtualizado : u
            );
            
            alert('Usuário atualizado com sucesso!');
            fecharModal(); // Fecha o modal após o sucesso

        } else {
            // Se a API retornou um erro (ex: 404, 500)
            alert('Erro ao atualizar usuário.');
            console.error('Erro na resposta da API:', await res.text());
        }

    } catch (err) {
        // Se ocorreu um erro de rede ou outro problema
        console.error("Erro ao tentar atualizar:", err);
        alert("Erro na atualização.");
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

      <main >
            <ModalEditarUsuarios
                usuario={usuarioSelecionado}
                aberto={modalAberto}
                onClose={fecharModal}
                onSave={atualizarUsuario}
            />
            
       <div class="flex flex-col gap-4 p-6">
        {#each usuarios as users}
            <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-5 hover:shadow-md transition-shadow">
            <h2 class="text-lg font-semibold text-emerald-700 mb-1">{users.nome}</h2>
            <p class="text-gray-600"><strong>CPF:</strong> {users.cpf}</p>
            <p class="text-gray-600"><strong>Cargo:</strong> {users.role}</p>
             <button class="mt-3 text-sm text-white bg-emerald-600 hover:bg-emerald-700 px-4 py-2 rounded transition-colors" on:click={() => abrirModal(users)}>
                Ver detalhes
            </button>
            </div>
           
        {/each}
        </div>

      
      </main>
  </div>


</div>
