document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("formSolicitacao").addEventListener("submit", enviarFormulario);
    document.getElementById("addEspecialidadeButton").addEventListener("click", adicionarEspecialidades);
  });
  
  function adicionarEspecialidades() {
    const especialidadeContainer = document.getElementById("especialidadesContainer");
    const newNode = document.getElementById("especialidadeTemplate").content.cloneNode(true);
    especialidadeContainer.appendChild(newNode);
    reindexarEspecialidades();
  }
  
  function reindexarEspecialidades() {
    document
      .getElementById("especialidadesContainer")
      .querySelectorAll(".especialidade")
      .forEach((node, index) => {
        atualizarIndices(node, index);
      });
  }
  
  function atualizarIndices(node, index) {
    // Atualiza o texto do legend com o número da especialidade
    const legend = node.querySelector("legend");
    legend.textContent = `Especialidade ${index + 1}`;
  
    // Atualiza os atributos name e id dos campos dentro do node
    node.querySelectorAll("select").forEach((select) => {
      // Atualiza o atributo name (exemplo: de "especialidades[].especialidadeSolicitada" para "especialidades[0].especialidadeSolicitada")
      const originalName = select.getAttribute("name");
      const updatedName = originalName.replace(/\[\d*\]/, `[${index}]`);
      select.setAttribute("name", updatedName);
  
      // Atualiza o atributo id, se ele contiver "[]"
      const originalId = select.getAttribute("id");
      if (originalId && originalId.includes("[]")) {
        const updatedId = originalId.replace("[]", `${index}`);
        select.setAttribute("id", updatedId);
      }
    });
  }
  
  function removerEspecialidade(button) {
    button.closest(".especialidade").remove();
    reindexarEspecialidades();
  }
  
  async function enviarFormulario(event) {
    event.preventDefault();
  
    const dataMalote = document.getElementById("dataMalote").value;
    const nomePaciente = document.getElementById("nomePaciente").value;
    const cpfPaciente = document.getElementById("cpfPaciente").value;
    const usfOrigem = document.getElementById("usfOrigem").value;
    const observacoes = document.getElementById("observacoes").value;
  
    const especialidades = [];
    // Para cada especialidade adicionada, utilize os ids atualizados para extrair os valores
    document
      .querySelectorAll("#especialidadesContainer .especialidade")
      .forEach((div, index) => {
        const especialidadeSolicitada = div.querySelector(`#especialidadeSolicitada${index}`).value;
        const status = div.querySelector(`#status${index}`).value;
        const prioridade = div.querySelector(`#prioridade${index}`).value;
  
        especialidades.push({
          especialidadeSolicitada,
          status,
          prioridade,
        });
      });
  
    const solicitacaoData = {
      dataMalote,
      nomePaciente,
      cpfPaciente,
      usfOrigem,
      observacoes,
      especialidades,
    };
  
    try {
      const response = await fetch("/api/solicitacoes", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(solicitacaoData),
      });
  
      if (!response.ok) {
        throw new Error("Erro ao enviar solicitação!");
      }
  
      const result = await response.json();
      alert("Solicitação enviada com sucesso!");
      console.log(result);
    } catch (error) {
      alert("Erro ao enviar solicitação: " + error.message);
    }
  }
  