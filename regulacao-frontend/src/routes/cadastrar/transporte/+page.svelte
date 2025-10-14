<script lang="ts">
    import { postApi } from "$lib/api";
    import RoleBasedMenu from "$lib/RoleBasedMenu.svelte"
    import UserMenu from "$lib/UserMenu.svelte"

    let novoNomeVeiculo = $state('')
    let novaVaga = $state<number>(0)
    let tipoVeiculoNovo = $state('')
    let modeloVeiculoNovo = $state('')

async function cadastrarTransporte(){

    const payload = {
        nomeVeiculo : novoNomeVeiculo,
        vagas : novaVaga,
        tipoVeiculo: tipoVeiculoNovo,
        modelo : modeloVeiculoNovo
    }

    try{
        const res = await postApi("transporte/cadastrar",payload)

        if(!res.ok){
            alert ("Erro ao enviar dados para o servidor")
        }

        if(res.ok){
            alert ("Novo transporte Cadastrado !")
            novoNomeVeiculo = '';
            novaVaga = 0
            tipoVeiculoNovo = '';
            modeloVeiculoNovo = '';
            
        }

        const data = await res.json()
        console.log(data)
    }catch{
        alert("Erro ao se conectar ao servidor !")
    }
    
}

</script>
<svelte:head>
    <title> Cadastro Veicular</title>    
</svelte:head>

<div class="flex min-h-screen bg-gray-100">
    <RoleBasedMenu activePage="/cadastrar/transporte"/>

    <div class="flex-1 flex flex-col">
            <header class="bg-emerald-700 p-4 text-white items-center flex justify-between">
                <h1 class="text-xl font-bold">Cadastro de Transporte</h1>
                <UserMenu/>
            </header>
            
            <main class="flex-1 overflow-auto p-6">
                <div class="grid gap-6 lg:grid-cols-1">
                    <section class="space-y-6 p-6 bg-white">
                        <h2 class="text-emerald-700 font-bold">Novo Transporte</h2>
                        <form onsubmit={cadastrarTransporte} class="space-y-4 ">
                            <div class="flex flex-col">
                                <label 
                                for="nomeVeiculo" 
                                class="font-medium text-sm text-gray-700 mb-1">Nome do Veiculo
                                </label>
                                
                                <input 
                                id="nomeVeiculo"
                                type="text" 
                                placeholder="Identifique o nome do veículo..." 
                                bind:value={novoNomeVeiculo}
                                class="border border-gray-300 rounded-lg px-3 py-2"
                                >
                            </div>
                        
                            <div class="flex flex-col ">
                                <label 
                                for="vaga"
                                class="font-medium text-sm text-gray-700 mb-1 ">
                                    Vagas
                                </label>
                                <input
                                id="vaga" 
                                type="text"
                                placeholder="Digite a quantidade de vagas esse veiculo possui !"
                                bind:value={novaVaga}
                                class="border border-gray-300 rounded-lg px-3 py-2"
                                >

                            </div>
                        
                            <div class="flex flex-col">
                                <label for="tipo" class="font-medium text-gray-700 mb-1 text-sm">Tipo</label>
                                <select id="tipo" bind:value={tipoVeiculoNovo} class="border border-gray-300 rounded-lg px-3 py-2">
                                    <option value="">Selecione...</option>
                                    <option value="VAN">Onibus</option>
                                    <option value="AMBULANCIA">Ambulância</option>
                                </select>
                            </div>
                        
                        <div class="flex flex-col">
                            <label for="modelo" class="font-medium text-sm text-gray-700 mb-1">
                                Modelo
                            </label>
                            <input
                            id="modelo"
                            type="text" 
                            placeholder="Descreva o modelo..." 
                            bind:value={modeloVeiculoNovo}
                            class="border border-gray-300 rounded-lg px-3 py-2"
                            >
                        
                        </div>
                        
                        <div class="flex align-middle justify-center px-3 py-2 rounded-lg bg-emerald-700 text-white">
                            <button type="submit">Cadastrar</button>
                        </div>
                    </form>
                </section>
            </div>
        </main>
    </div>
</div>
