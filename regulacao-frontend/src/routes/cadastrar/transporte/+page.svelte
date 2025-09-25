<script lang="ts">
    import { postApi } from "$lib/api";
    import RoleBasedMenu from "$lib/RoleBasedMenu.svelte"
    import UserMenu from "$lib/UserMenu.svelte"


    let novoNomeVeiculo = $state('')
    let novaVaga = $state()
    let tipoVeiculoNovo = $state('')
    let modeloVeiculoNovo = $state('')

async function cadastrarTransporte(){

    const payload = {
        nomeVeiculo : novoNomeVeiculo,
        vagas : novaVaga,
        TipoVeiculo: tipoVeiculoNovo,
        modelo : modeloVeiculoNovo
    }

    try{
        const res = await postApi("/transporte/cadastrar",payload)

        if(!res.ok){
            alert ("Erro ao enviar dados para o servidor")
        }
    }catch{
        alert("Erro ao se conectar ao servidor !")
    }
    
}

</script>

<div class="flex min-h-screen bg-gray-100">
    <RoleBasedMenu activePage="/cadastrar/cidade"/>

    <div class="flex-1 flex flex-col">
        <header class="bg-emerald-700 p-4 text-white items-center flex justify-between">
            <h1 class="text-xl font-bold">Cadastro de Transporte</h1>
            <UserMenu/>
        </header>
    </div>

    <main>
        
    </main>
</div>