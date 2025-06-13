export async function load ({ fetch }) {
  const res = await fetch("http://localhost:8080/api/solicitacoes");
  if (!res.ok) throw new Error("Não conseguiu se comunicar com o servidor !");
  const solicitacoes = await res.json();  // já é a lista
  return { solicitacoes };
}
