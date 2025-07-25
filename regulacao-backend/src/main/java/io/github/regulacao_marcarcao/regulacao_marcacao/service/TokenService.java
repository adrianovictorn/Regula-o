package io.github.regulacao_marcarcao.regulacao_marcacao.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import io.github.regulacao_marcarcao.regulacao_marcacao.entity.User;

@Service
public class TokenService {
   
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User usuario){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            String token = JWT.create()
            .withIssuer("regulacao-api")
            .withSubject(usuario.getCpf())
            .withClaim("role", usuario.getRole().name()) 
            .withClaim("nome", usuario.getNome())
            .withExpiresAt(genExpirationDate())
            .sign(algorithm);
            return token;
        }catch(JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar Token", exception);
        }

    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("regulacao-api") 
                .build()
                .verify(token) 
                .getSubject(); 
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
