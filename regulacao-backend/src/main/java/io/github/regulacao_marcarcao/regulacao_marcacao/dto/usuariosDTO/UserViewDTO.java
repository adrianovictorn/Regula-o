package io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.User;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.Roles;

public record UserViewDTO(
    Long id,
    String cpf, // autenticado via cpf
    String nome, // identifica o usuário
    Roles role // função dentro do sistema
) {


    public static UserViewDTO from (User user){
        return new UserViewDTO(
        user.getId(),
        user.getUsername(),
        user.getNome(), 
        user.getRole());
    }
}