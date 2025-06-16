package io.github.regulacao_marcarcao.regulacao_marcacao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO.LoginRequestDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO.LoginResponseDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.User;
import io.github.regulacao_marcarcao.regulacao_marcacao.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        // 1. Cria um objeto de autenticação com CPF e senha
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.cpf(), data.password());

        // 2. O Spring Security usa seu AuthenticationService para validar o usuário
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Se a autenticação for bem-sucedida, pega os detalhes do usuário
        var user = (User) auth.getPrincipal();

        // 4. Usa o TokenService para gerar um novo token
        var token = tokenService.generateToken(user);

        // 5. Retorna o token em um DTO
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}