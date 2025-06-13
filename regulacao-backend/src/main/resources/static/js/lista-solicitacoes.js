
let tabela;
let template;


function populateSolicitacoes(solicitacoes) {

    function populateSolicitacoes(solicitacoes) {
        const container = document.getElementById('lista-solicitacoes');
        container.innerHTML = ''; // Limpa o conteúdo anterior, se houver
    
        const template = document.getElementById('template-solicitacao');
    
        solicitacoes.forEach(s => {
            const clone = template.content.cloneNode(true);
    
            // Preenche os campos com os dados da solicitação
            clone.querySelector('.campo-usfOrigem strong').textContent = s.id; // ou s.numeroSolicitacao
            clone.querySelector('.nomePaciente').textContent = s.nomePaciente;
            clone.querySelector('.dataMalote').textContent = s.dataMalote;
            clone.querySelector('.observacoes').textContent = s.observacoes;
    
            // Preenche a lista de especialidades
            const especialidadesUl = clone.querySelector('.campo-especialidades-lista');
            s.especialidades.forEach(e => {
                const li = document.createElement('li');
                li.textContent = e;
                especialidadesUl.appendChild(li);
            });
    
            // Adiciona o clone ao container
            container.appendChild(clone);
        });
    }
    
}


document.addEventListener('DOMContentLoaded', function () {

    document.getElementById('form-filtro').addEventListener('submit', function (event) {
        event.preventDefault();

        const formData = new FormData(this);

        // Construa a string de query params
        const params = new URLSearchParams();

        // Itera sobre os dados do FormData e adiciona ao URLSearchParams
        formData.forEach((value, key) => {
            // Para cada campo do formulário, adiciona o par chave-valor à URL
            if (value) {  // Se o valor não estiver vazio
                params.append(key, value);
            }
        });

        const apiUrl = `/api/solicitacoes?${params.toString()}`;

        const response = fetch(apiUrl, { method: 'GET' }).then(response => response.json())
            .then(data => {
                console.log('Dados recebidos:', data);
                populateSolicitacoes(data);
            }).catch(error => {
                console.error('Erro ao fazer a requisição:', error);
            });

    })


})