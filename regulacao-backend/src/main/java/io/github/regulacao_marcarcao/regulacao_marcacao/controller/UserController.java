package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO.UserCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO.UserViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    // Apenas usuários com o perfil ADMIN podem cadastrar outros usuários
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserViewDTO> createUser(@RequestBody @Valid UserCreateDTO createDTO) {
        UserViewDTO createdUser = userService.criarUsuario(createDTO);
        // Retorna o status 201 Created, que é o correto para criação de recursos
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}