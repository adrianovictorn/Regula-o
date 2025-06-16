package io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO;

// 'record' é uma forma moderna e concisa de criar DTOs no Java
public record LoginRequestDTO(String cpf, String password) {
}