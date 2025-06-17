package io.github.regulacao_marcarcao.regulacao_marcacao.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// Esta anotação torna a classe um gerenciador de exceções global para todos os controllers.
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        
        boolean isCpfConflict = ex.getBindingResult().getFieldErrors().stream()
                .anyMatch(error -> "cpfPaciente".equals(error.getField()) && 
                                   error.getDefaultMessage().contains("CPF já cadastrado"));

        if (isCpfConflict) {
            // Se for o erro de CPF, criamos uma resposta simples.
            Map<String, String> body = new HashMap<>();
            body.put("message", "CPF já cadastrado, consulte o módulo Paciente");
            
            // Retornamos o status 409 Conflict. Este é o nosso "sinal" para o frontend.
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        }

        // Para qualquer outro erro de validação, retornamos um 400 Bad Request padrão.
        return new ResponseEntity<>("Erro de validação. Verifique os campos.", HttpStatus.BAD_REQUEST);
    }
}