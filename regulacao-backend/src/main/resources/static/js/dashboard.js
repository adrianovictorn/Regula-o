
document.addEventListener("DOMContentLoaded", async function () {
    try {
        const response = await fetch("/api/dashboard"); // Rota da API para os dados
        const data = await response.json();

        document.getElementById("pendentes").innerText = data.pendentes;
        document.getElementById("agendadas").innerText = data.agendadas;
        document.getElementById("maisSolicitada").innerText = data.maisSolicitada;
        document.getElementById("urgentes").innerText = data.urgentes;
        document.getElementById("emergencias").innerText = data.emergencias;
    } catch (error) {
        console.error("Erro ao buscar os dados do dashboard", error);
    }
});
