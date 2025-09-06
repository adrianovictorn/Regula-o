package io.github.regulacao_marcarcao.regulacao_marcacao.dto.pacto.convite;

import java.util.List;
import java.util.UUID;

public record CriarConvitesDTO(List<UUID> convidados, String mensagem) {}

