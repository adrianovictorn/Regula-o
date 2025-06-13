export async function load ({ fetch }) {
    const res = await fetch("http://localhost:8080/api/solicitacoes");
    if (!res.ok) {
      throw new Error("Não conseguiu se comunicar com o servidor!");
    }
    const solicitacoes = await res.json();

    const filtradas = solicitacoes.filter(s => 
      s.usfOrigem === 'USF05' &&
      s.especialidades.some(esp => esp.status === 'AGUARDANDO')
    );

    return { solicitacoes: filtradas };
  }
