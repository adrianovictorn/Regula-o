package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO.UserCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO.UserViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.User;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserViewDTO criarUsuario(UserCreateDTO dto){

        userRepository.findByCpf(dto.getCpf()).ifPresent(user -> {
            throw new IllegalArgumentException("CPF já cadastrado como usuário");
        
        });

        User novoUsuario = new User();
        novoUsuario.setCpf(dto.getCpf());
        novoUsuario.setNome(dto.getNome());

        novoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        novoUsuario.setRole(dto.getCargo());

        User usuarioSalvo = userRepository.save(novoUsuario);
        return UserViewDTO.from(usuarioSalvo);

    }
}
