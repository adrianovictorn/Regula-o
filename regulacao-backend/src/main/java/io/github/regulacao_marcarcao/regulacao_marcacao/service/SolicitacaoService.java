package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamentoDTO.AgendamentoSolicitacaoSimpleViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacaoEspecialidadeDTO.EspecialidadeAdicionarDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.AgendamentoSolicitacaoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoSolicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoEspecialidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.EspecialidadesEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusDaMarcacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.AgendamentoSolicitacaoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoEspecialidadeRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.SolicitacaoSpecification;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoListFiltersDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.solicitacoesDTO.SolicitacaoPublicViewDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final AgendamentoSolicitacaoRepository agendamentoRepository;
    private final SolicitacaoEspecialidadeRepository especialidadeRepository;


    @Transactional
    public SolicitacaoViewDTO createSolicitacao(SolicitacaoCreateDTO dto) {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setUsfOrigem(dto.usfOrigem());
        
        solicitacao.setNomePaciente(dto.nomePaciente());
        solicitacao.setCpfPaciente(dto.cpfPaciente());
        solicitacao.setCns(dto.cns());
        solicitacao.setTelefone(dto.telefone());
        solicitacao.setDataNascimento(dto.datanascimento());
        solicitacao.setObservacoes(dto.observacoes());
        solicitacao.setDataMalote(dto.dataMalote());

        var especialidades = dto.especialidades().stream()
            .map(e -> new SolicitacaoEspecialidade(
                null,
                solicitacao,
                null,
                e.especialidadeSolicitada(),
                e.status(),
                e.prioridade()
            ))
            .collect(Collectors.toList());

        solicitacao.setEspecialidades(especialidades);
        var saved = solicitacaoRepository.save(solicitacao);
        return SolicitacaoViewDTO.fromSolicitacao(saved);
    }

    @Transactional
    public SolicitacaoViewDTO updateSolicitacao(Long id, SolicitacaoUpdateDTO dto) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada."));

        solicitacao.setNomePaciente(dto.nomePaciente());
        solicitacao.setCns(dto.cns());
        solicitacao.setTelefone(dto.telefone());
        solicitacao.setDataNascimento(dto.datanascimento());
        solicitacao.setObservacoes(dto.observacoes());
        solicitacao.setDataMalote(dto.dataMalote());
        solicitacao.setUsfOrigem(dto.usfOrigem());

        var updated = solicitacaoRepository.save(solicitacao);
        return SolicitacaoViewDTO.fromSolicitacao(updated);
    }

    @Transactional(readOnly = true)
    public SolicitacaoViewDTO getSolicitacaoById(Long id) {
        Solicitacao s = solicitacaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada."));
        return SolicitacaoViewDTO.fromSolicitacao(s);
    }

    @Transactional(readOnly = true)
    public Page<SolicitacaoViewDTO> listSolicitacoes(SolicitacaoListFiltersDTO filters, Pageable pageable) {
        Specification<Solicitacao> spec = SolicitacaoSpecification.aplicarFiltros(filters);
        return solicitacaoRepository.findAll(spec, pageable)
                .map(SolicitacaoViewDTO::fromSolicitacao);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoViewDTO> todasSolicitacoes(){
        return solicitacaoRepository.findAll().stream().map(SolicitacaoViewDTO::fromSolicitacao).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AgendamentoSolicitacaoSimpleViewDTO> listAllAgendamentos() {
        return agendamentoRepository.findAll().stream()
            .map(AgendamentoSolicitacaoSimpleViewDTO::fromAgendamentoSolicitacao)
            .collect(Collectors.toList());
    }

    @Transactional
    public AgendamentoSolicitacaoSimpleViewDTO createAgendamento(
            Long solicitacaoId,
            AgendamentoSolicitacaoCreateDTO dto) {
        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
            .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada."));

        AgendamentoSolicitacao ag = new AgendamentoSolicitacao();
        ag.setSolicitacao(solicitacao);
        ag.setLocalAgendado(dto.localAgendado());
        ag.setObservacoes(dto.observacoes());
        ag.setDataAgendada(dto.dataAgendada());
        ag = agendamentoRepository.save(ag);

        SolicitacaoEspecialidade se = solicitacao.getEspecialidades().stream()
            .filter(e -> e.getEspecialidadeSolicitada() == dto.especialidadeSolicitada())
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Especialidade não encontrada para agendamento."));

        se.setStatus(StatusDaMarcacao.AGENDADO);
        se.setAgendamentoSolicitacao(ag);

        solicitacaoRepository.save(solicitacao);
        return AgendamentoSolicitacaoSimpleViewDTO.fromAgendamentoSolicitacao(ag);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoSolicitacaoSimpleViewDTO> listarAgendamentosPorSolicitacaoId(Long solicitacaoId) {
        List<AgendamentoSolicitacao> agendamentos = agendamentoRepository.findBySolicitacaoId(solicitacaoId);
        return agendamentos.stream()
                .map(AgendamentoSolicitacaoSimpleViewDTO::fromAgendamentoSolicitacao)
                .collect(Collectors.toList());
    }

    @Transactional
    public SolicitacaoViewDTO adicionarEspecialidadeASolicitacao(Long solicitacaoId, EspecialidadeAdicionarDTO dto) {
        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
            .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada."));

        // Cria a nova especialidade usando os dados do DTO (status e prioridade vêm do frontend)
        SolicitacaoEspecialidade novaEspecialidade = new SolicitacaoEspecialidade(
            null, // ID será gerado automaticamente
            solicitacao,
            null, // agendamentoSolicitacao é nulo inicialmente
            dto.especialidadeSolicitada(),
            dto.status(),   // Usando o status do DTO
            dto.prioridade() // Usando a prioridade do DTO
        );

        solicitacao.getEspecialidades().add(novaEspecialidade);

        // Salva a solicitação, o que também persistirá a nova especialidade devido ao CascadeType.ALL
        solicitacao = solicitacaoRepository.save(solicitacao);
        return SolicitacaoViewDTO.fromSolicitacao(solicitacao);
    }

    // Exemplo de modificação em SolicitacaoService.java
    @Transactional(readOnly = true)
    public List<Map<String, String>> listarTodasEspecialidadesComDetalhes() {
        return Arrays.stream(EspecialidadesEnum.values())
                .map(e -> {
                    Map<String, String> detalhes = new HashMap<>();
                    detalhes.put("name", e.name());
                    detalhes.put("descricao", e.getDescricao());
                    detalhes.put("categoria", e.getCategoria().getDisplayValue());
                    return detalhes;
                })
                .collect(Collectors.toList());
    }

     @Transactional
    public void removerEspecialidade(Long id) {
        // Verifica se a especialidade existe antes de tentar deletar
        if (!especialidadeRepository.existsById(id)) {
            throw new EntityNotFoundException("Especialidade de solicitação com ID " + id + " não encontrada.");
        }
        especialidadeRepository.deleteById(id);
    }

    @Transactional
    public List<SolicitacaoPublicViewDTO> buscarPacientePorCpf(String cpf){
        return solicitacaoRepository.findByCpfPaciente(cpf).stream().map(SolicitacaoPublicViewDTO::fromSolicitacao).collect(Collectors.toList());
    }
}