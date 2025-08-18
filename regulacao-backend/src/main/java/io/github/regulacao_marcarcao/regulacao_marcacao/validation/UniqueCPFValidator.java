package io.github.regulacao_marcarcao.regulacao_marcacao.validation;

import io.github.regulacao_marcarcao.regulacao_marcacao.repository.SolicitacaoRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueCPFValidator implements ConstraintValidator<UniqueCPF, String> {

    private final SolicitacaoRepository solicitacaoRepository;

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isBlank()) {
            // Considera-se válido se não for preenchido,
            // para não conflitar com a anotação @NotBlank
            return true;
        }
        // 1. Limpa o CPF de qualquer caractere não numérico para a consulta
        String cpfLimpo = cpf.replaceAll("\\D", "");

        // 2. Utiliza o novo método que busca por CPF sem pontuação
        // A validação é bem-sucedida (retorna true) se a lista de resultados estiver vazia,
        // o que significa que o CPF é único.
        return solicitacaoRepository.findByCpfPacienteSemPonto(cpfLimpo).isEmpty();
    }
}
