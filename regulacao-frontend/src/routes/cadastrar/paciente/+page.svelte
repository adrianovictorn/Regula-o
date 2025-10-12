<script lang="ts">
	import { goto } from '$app/navigation';
	import { getApi, postApi } from '$lib/api.js';
	import { listarEspecialidadesMedicas } from '$lib/especialidadesApi.js';
	import RoleBasedMenu from '$lib/RoleBasedMenu.svelte';
	import UserMenu from '$lib/UserMenu.svelte';
	import { onMount } from 'svelte';


	// --- ESTADO DO FORMULÁRIO ---
	let isLoading = false;

	// **AJUSTE 1: Unificar a lista de CIDs**
	// A lista de CIDs carregada da API. O #each usará esta variável.
	// Para combobox de CIDs por linha
	let termosCid: string[] = [''];
	let comboCidAberto: boolean[] = [false];

	// Array para guardar os IDs dos CIDs selecionados (um por linha)
	let cidsSelecionados: (number | null)[] = [null];

	// Campos do formulário
	let nomePaciente = '';
	let cpfPaciente = '';
	let cns = '';
	let telefone = '';
	let datanascimento = '';
	let usfOrigem = '';
	let dataMalote = '';
	let observacoes = '';
	let cid = '';

	let especialidadesCatalogo: { id: number; codigo: string; nome: string }[] = [];
	let especialidades = [{ especialidadeId: null as number | null, status: 'AGUARDANDO', prioridade: 'NORMAL', termo: '', aberto: false }];

	// --- FUNÇÕES DO FORMULÁRIO ---

	function addEspecialidade() {
		especialidades = [
			...especialidades,
			{ especialidadeId: null, status: 'AGUARDANDO', prioridade: 'NORMAL', termo: '', aberto: false }
		];
	}

	function addCid() {
		cidsSelecionados = [...cidsSelecionados, null];
		termosCid = [...termosCid, ''];
		comboCidAberto = [...comboCidAberto, false];
	}

	function removerCid(index: number) {
		cidsSelecionados = cidsSelecionados.filter((_, i) => i !== index);
		termosCid = termosCid.filter((_, i) => i !== index);
		comboCidAberto = comboCidAberto.filter((_, i) => i !== index);
	}

	function removerEspecialidade(i: number) {
		especialidades = especialidades.filter((_, idx) => idx !== i);
	}



	async function submeterForm() {
		isLoading = true;

		
		const payload = {
			nomePaciente,
			cpfPaciente: cpfPaciente.replace(/\D/g, ''),
			cns,
			telefone,
			datanascimento,
			usfOrigem,
			dataMalote,
			observacoes
		};

		try {
			const res = await postApi('solicitacoes', payload);

			if (res.ok) {
				alert('Solicitação cadastrada com sucesso!');
				limparCampos();
				goto('/cadastrar');
			} else if (res.status === 409) {
				alert('CPF já cadastrado, consulte o módulo Paciente');
			} else {
				alert(`Ocorreu um erro (${res.status}). Verifique os dados e tente novamente.`);
			}
		} catch (err) {
			console.error('Erro de conexão:', err);
			alert('Não foi possível conectar ao servidor. Verifique sua conexão.');
		} finally {
			isLoading = false;
		}
	}

		function limparCampos() {
			nomePaciente = '';
			cpfPaciente = '';
			cns = '';
			telefone = '';
			datanascimento = '';
			usfOrigem = '';
			dataMalote = '';
			observacoes = '';
		}

	function formatarCPF(e: Event) {
		const target = e.target as HTMLInputElement;
		let d = target.value.replace(/\D/g, '').slice(0, 11);
		d = d.replace(/^(\d{3})(\d)/, '$1.$2').replace(/^(\d{3}\.\d{3})(\d)/, '$1.$2').replace(/(\d{3}\.\d{3}\.\d{3})(\d)/, '$1-$2');
		cpfPaciente = d;
	}

	onMount(async () => {
		await Promise.all([
			listarEspecialidadesMedicas()
				.then((lista) => (especialidadesCatalogo = lista))
				.catch((e) => console.warn('Falha ao listar especialidades (catálogo):', e))
		]);
	});

	function normalize(s: string) {
		return (s || '')
			.toString()
			.normalize('NFD')
			.replace(/[\u0300-\u036f]/g, '')
			.toLowerCase();
	}

</script>

<svelte:head>
	<title>Consulta</title>
</svelte:head>

<div class="flex min-h-screen bg-gray-100">
	<RoleBasedMenu activePage="/cadastrar" />

	<div class="flex-1 flex flex-col">
		<header class="bg-emerald-700 text-white shadow p-4 flex items-center justify-between">
			<h1 class="text-xl font-semibold">Cadastro de Solicitações</h1>
			<UserMenu />
		</header>

		<main class="flex-1 overflow-auto p-6">
			<div class="bg-white rounded-lg shadow-lg p-6">
				<h2 class="text-2xl font-bold text-emerald-800 mb-6">Cadastro de Solicitação</h2>
				<form on:submit|preventDefault={submeterForm} class="space-y-6">
					<div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
						<div class="flex flex-col">
							<label class="text-sm font-medium text-gray-700 mb-1">Data do Recebimento</label>
							<input type="date" bind:value={dataMalote} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
						</div>
						<div class="flex flex-col">
							<label class="text-sm font-medium text-gray-700 mb-1">USF Origem</label>
							<select bind:value={usfOrigem} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required>
								<option value="" disabled>Selecione...</option>
								{#each ['USF01', 'USF02', 'USF03', 'USF04', 'USF05', 'USF06', 'HMCA'] as u}
									<option value={u}>{u}</option>
								{/each}
							</select>
						</div>
					</div>

					<div class="grid grid-row-5  lg:grid-cols-6 gap-4">
						<div class="flex flex-col">
							<label class="text-sm font-medium text-gray-700 mb-1">Nome do Paciente</label>
							<input type="text" bind:value={nomePaciente} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
						</div>
						<div class="grid grid-cols-4 gap-4">
							<div class="flex flex-col">
								<label class="text-sm font-medium text-gray-700 mb-1">CPF</label>
								<input type="text" bind:value={cpfPaciente} on:input={formatarCPF} placeholder="000.000.000-00" class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
							</div>
							<div class="flex flex-col">
								<label class="text-sm font-medium text-gray-700 mb-1">CNS</label>
								<input type="text" bind:value={cns} placeholder="Digite o cartão do SUS" maxlength="15" class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
							</div>
							<div class="flex flex-col">
								<label class="text-sm font-medium text-gray-700 mb-1">Telefone</label>
								<input type="text" bind:value={telefone} placeholder="(00)0 0000-0000" maxlength="15" class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" />
							</div>
							<div class="flex flex-col">
								<label class="text-sm font-medium text-gray-700 mb-1">Data de Nascimento</label>
								<input type="date" bind:value={datanascimento} class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500" required />
							</div>




						</div>

					

					<div class="flex flex-col">
						<label class="text-sm font-medium text-gray-700 mb-1">Observações</label>
						<textarea bind:value={observacoes} rows="4" maxlength="500" class="border border-gray-300 rounded-lg p-2 focus:ring-emerald-500 focus:border-emerald-500"></textarea>
					</div>

					

					<div class="grid grid-cols-1"><button type="submit" class="w-full bg-emerald-800 text-white py-3 rounded-lg hover:bg-emerald-900 transition">Enviar</button></div>
				</form>
			</div>
		</main>
	</div>
</div>
