package io.github.regulacao_marcarcao.regulacao_marcacao.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.User;
import io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums.Roles;
import io.github.regulacao_marcarcao.regulacao_marcacao.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o usuário admin já existe pelo CPF
        if (userRepository.findByCpf("07679069506").isEmpty()) {
            System.out.println("Criando usuário administrador padrão...");
            
            User admin = new User();
            admin.setCpf("07679069506"); // Use um CPF válido para testes se preferir
            admin.setNome("SIRGE - Adriano Victor");
            // Codifica a senha antes de salvar
            admin.setPassword(passwordEncoder.encode("admin123")); 
            admin.setRole(Roles.ADMIN);

            userRepository.save(admin);
            System.out.println("Usuário administrador criado com sucesso.");
        }
    }
}