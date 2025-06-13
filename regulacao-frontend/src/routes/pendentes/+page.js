export async function load({fetch}){
    const res = await fetch('http://localhost:8080/api/solicitacoes');
    if(!res.ok) throw new Error('Falha ao buscar solicitações');
    const solicitacoes = await res.json();
    return {solicitacoes};
}