package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO.UserCreateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO.UserUpdateDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.dto.usuariosDTO.UserViewDTO;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.User;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.Roles;
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

    //[LISTAS]

    public List<UserViewDTO> listarUsuarios(){
       List<User> user = userRepository.findAll();
       List<UserViewDTO> viewDTOs = user.stream().map(UserViewDTO::from).toList();
       return viewDTOs;
    }


    public List<UserViewDTO> listarAdministradores(){
        List<User> admins = userRepository.findAll();
        List<UserViewDTO> viewDTOs = admins.stream().filter(user -> user.getRole() == Roles.ADMIN).map(UserViewDTO::from).toList();
        return viewDTOs;
    }

    public List<UserViewDTO> listarRoleUsers(){
        List<User> usuarios = userRepository.findAll();
        List<UserViewDTO> viewDTOs = usuarios.stream().filter(user -> user.getRole() == Roles.USER).map(user -> UserViewDTO.from(user)).toList();
        return viewDTOs;
    }

    public List<UserViewDTO> listarRoleEnfermeiro(){
        List<User> enfermeiros = userRepository.findAll();
        List<UserViewDTO> viewDTOs = enfermeiros.stream().filter(users -> users.getRole() == Roles.ENFERMEIRO).map(UserViewDTO::from).toList();
        return viewDTOs;
    }

    public List<UserViewDTO> listarRoleMedico(){
        List<User> medicos = userRepository.findAll();
        List<UserViewDTO> viewDTOs = medicos.stream().filter(medico -> medico.getRole() == Roles.MEDICO).map(UserViewDTO::from).toList();

        return viewDTOs;
    }

        public List<UserViewDTO> listarRoleRecepcionista(){
        List<User> recepcionista = userRepository.findAll();
        List<UserViewDTO> viewDTOs = recepcionista.stream().filter(recepcao -> recepcao.getRole() == Roles.RECEPCAO).map(UserViewDTO::from).toList();
        return viewDTOs;
    }

        //[LISTAS]
// -------------------

        //[ATUALIZAR]
        public UserViewDTO atualizarUser(Long id, UserUpdateDTO user){
            User usuarioExistente = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            usuarioExistente.setCpf(user.cpf());
            usuarioExistente.setNome(user.nome());
            if (user.password() != null && !user.password().isEmpty()) {
                    // Criptografa a nova senha antes de salvar.
                    usuarioExistente.setPassword(passwordEncoder.encode(user.password()));
            }           
             usuarioExistente.setRole(user.role());

            User usuarioAtualizado = userRepository.save(usuarioExistente);
            return UserViewDTO.from(usuarioAtualizado);
        }
        
        //[ATUALIZAR]

// ------

        //[DELETAR]
        public void deletarUsuario(Long id){
            userRepository.deleteById(id);
        }

        //[DELETAR]


}