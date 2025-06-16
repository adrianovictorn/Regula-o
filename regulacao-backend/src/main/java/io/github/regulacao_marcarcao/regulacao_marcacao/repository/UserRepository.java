package io.github.regulacao_marcarcao.regulacao_marcacao.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    
    Optional<User> findByCpf(String cpf);
}
