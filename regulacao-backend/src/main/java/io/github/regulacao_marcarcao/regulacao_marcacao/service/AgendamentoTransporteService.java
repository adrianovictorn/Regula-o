package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteListDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransportePacientePayload;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.agendamento.transporte.AgendamentoTransporteViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoTransporte;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.AgendamentoTransportePaciente;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Cidade;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.LocalAgendamento;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Motorista;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Solicitacao;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.Transporte;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.StatusAgendamento;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.TurnoEnum;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.AgendamentoTransporteRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.CidadeRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.LocalAgendamentoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.MotoristaRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoRepository;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.TransporteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendamentoTransporteService {

    private final CidadeRepository cidadeRepository;
    private final TransporteRepository transporteRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final AgendamentoTransporteRepository agendamentoTransporteRepository;
    private final LocalAgendamentoRepository localAgendamentoRepository;
    private final MotoristaRepository motoristaRepository;

    @Transactional
    public AgendamentoTransporteViewDTO criarAgendamentoTransporte(AgendamentoTransporteCreateDTO dto){
        Cidade cidade = cidadeRepository.findById(dto.cidadeId())
            .orElseThrow(() -> new IllegalArgumentException("Cidade inexistente"));
        Transporte transporte = transporteRepository.findById(dto.transporteId())
            .orElseThrow(() -> new IllegalArgumentException("Transporte inexistente"));

        agendamentoTransporteRepository.findByTransporteIdAndData(transporte.getId(), dto.data())
            .ifPresent(x -> { throw new IllegalStateException("JÃ¡ existe agendamento para esse transporte nesta data"); });

        AgendamentoTransporte agendamento = new AgendamentoTransporte();
        agendamento.setCidade(cidade);
        agendamento.setTransporte(transporte);
        agendamento.setData(dto.data());
        agendamento.setHoraSaida(dto.horaSaida());
        agendamento.setStatus(StatusAgendamento.PENDENTE);
        agendamento.setPacientes(new HashSet<>());
        agendamento.setLocaisAgendamento(new java.util.ArrayList<>());

        if (dto.motoristaId() != null) {
            Motorista motorista = motoristaRepository.findById(dto.motoristaId())
                .orElseThrow(() -> new IllegalArgumentException("Motorista inexistente"));
            agendamento.setMotorista(motorista);
        }

        agendamento = agendamentoTransporteRepository.save(agendamento);
        return AgendamentoTransporteViewDTO.fromDTO(agendamento);
    }

    @Transactional
    public List<AgendamentoTransporteListDTO> listarAgendamentoTransporte(LocalDate data, Long transporteId, Long motoristaId){
        List<AgendamentoTransporte> agendamentos;

        if (data != null && transporteId != null) {
            agendamentos = agendamentoTransporteRepository.findByTransporteIdAndData(transporteId, data)
                .map(List::of)
                .orElseGet(List::of);
        } else if (data != null) {
            agendamentos = agendamentoTransporteRepository.findByData(data);
        } else if (transporteId != null) {
            agendamentos = agendamentoTransporteRepository.findByTransporteId(transporteId);
        } else {
            agendamentos = agendamentoTransporteRepository.findAll();
        }

        if (motoristaId != null) {
            agendamentos = agendamentos.stream()
                .filter(ag -> ag.getMotorista() != null && motoristaId.equals(ag.getMotorista().getId()))
                .toList();
        }

        return agendamentos.stream()
            .map(AgendamentoTransporteListDTO::fromEntity)
            .toList();
    }

    @Transactional
    public AgendamentoTransporteViewDTO buscarAgendamentoPorId(Long id) {
        AgendamentoTransporte agendamento = agendamentoTransporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agendamento nÃ£o encontrado!"));
        return AgendamentoTransporteViewDTO.fromDTO(agendamento);
    }

    @Transactional
    public AgendamentoTransporteViewDTO atualizarAgendamentoTransporte(AgendamentoTransporteUpdateDTO dto, Long id){
        AgendamentoTransporte agendamento = agendamentoTransporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agendamento nÃ£o encontrado!"));

        Cidade cidade = dto.cidadeId() != null
            ? cidadeRepository.findById(dto.cidadeId())
                .orElseThrow(() -> new RuntimeException("Cidade nÃ£o existente!"))
            : agendamento.getCidade();

        Transporte transporte = dto.transporteId() != null
            ? transporteRepository.findById(dto.transporteId())
                .orElseThrow(() -> new RuntimeException("Transporte nÃ£o existe!"))
            : agendamento.getTransporte();

        agendamento.setCidade(cidade);
        agendamento.setTransporte(transporte);

        if (dto.data() != null) {
            Optional<AgendamentoTransporte> existenteMesmoDia = agendamentoTransporteRepository.findByTransporteIdAndData(transporte.getId(), dto.data());
            if (existenteMesmoDia.isPresent() && !existenteMesmoDia.get().getId().equals(agendamento.getId())) {
                throw new IllegalStateException("JÃ¡ existe agendamento para esse transporte nesta data");
            }
            agendamento.setData(dto.data());
        }

        if (dto.status() != null) {
            agendamento.setStatus(dto.status());
        }

        if (dto.motoristaId() != null) {
            Motorista motorista = motoristaRepository.findById(dto.motoristaId())
                .orElseThrow(() -> new RuntimeException("Motorista nÃ£o encontrado!"));
            agendamento.setMotorista(motorista);
        } else if (dto.motoristaId() == null && dto.pacientes() != null) {
            agendamento.setMotorista(null);
        }

        if (dto.horaSaida() != null) {
            agendamento.setHoraSaida(dto.horaSaida());
        }

        if (dto.localId() != null) {
            sincronizarLocais(agendamento, dto.localId());
        }

        sincronizarPacientes(agendamento, dto.pacientes());
        validarCapacidade(agendamento);

        agendamentoTransporteRepository.save(agendamento);
        return AgendamentoTransporteViewDTO.fromDTO(agendamento);
    }

    @Transactional
    public AgendamentoTransporteViewDTO confirmarAgendamento(Long id) {
        AgendamentoTransporte agendamento = agendamentoTransporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agendamento nÃ£o encontrado!"));
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        agendamentoTransporteRepository.save(agendamento);
        return AgendamentoTransporteViewDTO.fromDTO(agendamento);
    }

    @Transactional
    public void deletarAgendamentoTransporte(Long id) {
        AgendamentoTransporte agendamento = agendamentoTransporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agendamento de Transporte nÃ£o encontrado!"));
        agendamentoTransporteRepository.delete(agendamento);
    }

    @Transactional
    public AgendamentoTransporteViewDTO agendarTransporteDia(Long transporteId,
                                                             Long cidadeId,
                                                             LocalDate data,
                                                             List<AgendamentoTransportePacientePayload> pacientesPayload,
                                                             List<Long> locaisAgendamento,
                                                             Long motoristaId,
                                                             LocalTime horaSaida){
        if (transporteId == null || cidadeId == null || data == null) {
            throw new IllegalArgumentException("Transporte, cidade e data são obrigatórios para o agendamento.");
        }

        List<AgendamentoTransportePacientePayload> pacientes = filtrarPacientesValidos(pacientesPayload);
        Set<Long> solicitacaoIds = pacientes.stream()
            .map(AgendamentoTransportePacientePayload::solicitacaoId)
            .collect(Collectors.toCollection(HashSet::new));

        Map<Long, Solicitacao> solicitacoes = solicitacaoIds.isEmpty()
            ? Collections.emptyMap()
            : solicitacaoRepository.findAllById(solicitacaoIds).stream()
                .collect(Collectors.toMap(Solicitacao::getId, Function.identity()));

        if (solicitacaoIds.size() != solicitacoes.size()) {
            throw new IllegalArgumentException("Alguma solicitação informada não existe.");
        }

        if (pacientes.stream().anyMatch(p -> p.localAgendamentoId() == null)) {
            throw new IllegalArgumentException("Informe o local de atendimento para todos os pacientes.");
        }

        Map<Long, LocalAgendamento> locaisPorId = carregarLocaisPacientes(pacientes, cidadeId);

        AgendamentoTransporte agendamento = agendamentoTransporteRepository.findByTransporteIdAndData(transporteId, data)
            .orElseGet(() -> criarOuRecuperarAgendamento(transporteId, cidadeId, data));

        if (motoristaId != null) {
            Motorista motorista = motoristaRepository.findById(motoristaId)
                .orElseThrow(() -> new IllegalArgumentException("Motorista inexistente."));
            agendamento.setMotorista(motorista);
        }

        if (horaSaida != null) {
            agendamento.setHoraSaida(horaSaida);
        }

        List<Long> locaisDoItinerario = locaisAgendamento != null && !locaisAgendamento.isEmpty()
            ? locaisAgendamento
            : new java.util.ArrayList<>(locaisPorId.keySet());
        sincronizarLocais(agendamento, locaisDoItinerario);

        Map<Long, AgendamentoTransportePaciente> existentes = agendamento.getPacientes().stream()
            .collect(Collectors.toMap(p -> p.getSolicitacao().getId(), Function.identity(), (a, b) -> a, LinkedHashMap::new));

        int novos = (int) pacientes.stream()
            .map(AgendamentoTransportePacientePayload::solicitacaoId)
            .filter(id -> !existentes.containsKey(id))
            .count();

        validarCapacidade(agendamento, novos);

        for (AgendamentoTransportePacientePayload payload : pacientes) {
            AgendamentoTransportePaciente existente = existentes.get(payload.solicitacaoId());
            LocalAgendamento local = locaisPorId.get(payload.localAgendamentoId());
            if (existente != null) {
                aplicarDadosPaciente(existente, payload, local);
            } else {
                AgendamentoTransportePaciente novo = new AgendamentoTransportePaciente();
                novo.setAgendamento(agendamento);
                novo.setSolicitacao(solicitacoes.get(payload.solicitacaoId()));
                aplicarDadosPaciente(novo, payload, local);
                agendamento.getPacientes().add(novo);
            }
        }

        validarCapacidade(agendamento);
        agendamento = agendamentoTransporteRepository.saveAndFlush(agendamento);
        return AgendamentoTransporteViewDTO.fromDTO(agendamento);
    }

    private AgendamentoTransporte criarOuRecuperarAgendamento(Long transporteId, Long cidadeId, LocalDate data) {
        Cidade cidade = cidadeRepository.findById(cidadeId)
            .orElseThrow(() -> new IllegalArgumentException("Cidade inexistente."));
        Transporte transporte = transporteRepository.findById(transporteId)
            .orElseThrow(() -> new IllegalArgumentException("Transporte inexistente."));

        AgendamentoTransporte novo = new AgendamentoTransporte();
        novo.setCidade(cidade);
        novo.setTransporte(transporte);
        novo.setData(data);
        novo.setStatus(StatusAgendamento.PENDENTE);
        novo.setPacientes(new HashSet<>());
        novo.setLocaisAgendamento(new java.util.ArrayList<>());
        return agendamentoTransporteRepository.save(novo);
    }

    private List<AgendamentoTransportePacientePayload> filtrarPacientesValidos(List<AgendamentoTransportePacientePayload> pacientes) {
        if (pacientes == null) {
            return List.of();
        }
        return pacientes.stream()
            .filter(p -> p != null && p.solicitacaoId() != null)
            .collect(Collectors.toList());
    }

    private void sincronizarLocais(AgendamentoTransporte agendamento, List<Long> locaisIds) {
        List<Long> ids = locaisIds == null ? List.of() : locaisIds;
        List<LocalAgendamento> novosLocais = ids.isEmpty()
            ? List.of()
            : localAgendamentoRepository.findAllById(ids);

        agendamento.getLocaisAgendamento().clear();
        agendamento.getLocaisAgendamento().addAll(novosLocais);
    }

    private Map<Long, LocalAgendamento> carregarLocaisPacientes(List<AgendamentoTransportePacientePayload> pacientes, Long cidadeId) {
        List<Long> ids = pacientes == null ? List.of() : pacientes.stream()
            .map(AgendamentoTransportePacientePayload::localAgendamentoId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }

        List<LocalAgendamento> locais = localAgendamentoRepository.findAllById(ids);
        if (locais.size() != ids.size()) {
            throw new IllegalArgumentException("Algum local informado não existe.");
        }

        if (cidadeId != null) {
            boolean localInvalido = locais.stream()
                .anyMatch(local -> local.getCidade() != null && !Objects.equals(local.getCidade().getId(), cidadeId));
            if (localInvalido) {
                throw new IllegalArgumentException("Algum local informado não pertence à cidade selecionada.");
            }
        }

        Map<Long, LocalAgendamento> mapa = new LinkedHashMap<>();
        for (LocalAgendamento local : locais) {
            mapa.put(local.getId(), local);
        }
        return mapa;
    }


    private void sincronizarPacientes(AgendamentoTransporte agendamento, List<AgendamentoTransportePacientePayload> pacientesPayload) {
        List<AgendamentoTransportePacientePayload> pacientesValidos = filtrarPacientesValidos(pacientesPayload);
        Map<Long, AgendamentoTransportePacientePayload> payloadPorSolicitacao = new LinkedHashMap<>();
        for (AgendamentoTransportePacientePayload payload : pacientesValidos) {
            payloadPorSolicitacao.put(payload.solicitacaoId(), payload);
        }

        if (payloadPorSolicitacao.isEmpty()) {
            agendamento.getPacientes().clear();
            return;
        }

        if (payloadPorSolicitacao.values().stream().anyMatch(p -> p.localAgendamentoId() == null)) {
            throw new IllegalArgumentException("Informe o local de atendimento para todos os pacientes.");
        }

        List<Solicitacao> solicitacoes = solicitacaoRepository.findAllById(payloadPorSolicitacao.keySet());
        if (solicitacoes.size() != payloadPorSolicitacao.size()) {
            throw new IllegalArgumentException("Alguma solicitação informada não existe.");
        }

        Map<Long, Solicitacao> solicitacaoPorId = solicitacoes.stream()
            .collect(Collectors.toMap(Solicitacao::getId, Function.identity()));

        Long cidadeId = agendamento.getCidade() != null ? agendamento.getCidade().getId() : null;
        Map<Long, LocalAgendamento> locaisPorId = carregarLocaisPacientes(pacientesValidos, cidadeId);

        agendamento.getPacientes().removeIf(p -> !payloadPorSolicitacao.containsKey(p.getSolicitacao().getId()));

        for (AgendamentoTransportePaciente paciente : agendamento.getPacientes()) {
            AgendamentoTransportePacientePayload payload = payloadPorSolicitacao.get(paciente.getSolicitacao().getId());
            LocalAgendamento local = payload.localAgendamentoId() != null ? locaisPorId.get(payload.localAgendamentoId()) : null;
            aplicarDadosPaciente(paciente, payload, local);
        }

        Set<Long> existentes = agendamento.getPacientes().stream()
            .map(p -> p.getSolicitacao().getId())
            .collect(Collectors.toSet());

        payloadPorSolicitacao.entrySet().stream()
            .filter(entry -> !existentes.contains(entry.getKey()))
            .forEach(entry -> {
                AgendamentoTransportePaciente novo = new AgendamentoTransportePaciente();
                novo.setAgendamento(agendamento);
                novo.setSolicitacao(solicitacaoPorId.get(entry.getKey()));
                LocalAgendamento local = entry.getValue().localAgendamentoId() != null ? locaisPorId.get(entry.getValue().localAgendamentoId()) : null;
                aplicarDadosPaciente(novo, entry.getValue(), local);
                agendamento.getPacientes().add(novo);
            });
    }

    private void aplicarDadosPaciente(AgendamentoTransportePaciente paciente, AgendamentoTransportePacientePayload payload, LocalAgendamento local) {
        TurnoEnum turno = payload.turno() != null ? payload.turno() : TurnoEnum.NAO_INFORMADO;
        paciente.setTurno(turno);
        paciente.setRetornaMesmoDia(payload.retornaMesmoDia());
        paciente.setLocalAgendamento(local);
    }

    private void validarCapacidade(AgendamentoTransporte agendamento) {
        validarCapacidade(agendamento, 0);
    }

    private void validarCapacidade(AgendamentoTransporte agendamento, int novos) {
        Transporte transporte = agendamento.getTransporte();
        if (transporte == null || transporte.getVagas() == null) {
            return;
        }
        int capacidade = transporte.getVagas().intValue();
        int ocupadas = agendamento.getPacientes().size();
        if ((long) ocupadas + novos > capacidade) {
            throw new IllegalStateException("NÃ£o hÃ¡ vagas suficientes para esta data.");
        }
    }
}

