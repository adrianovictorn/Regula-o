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
            return true; // A validação de nulo/vazio é feita pelo @NotBlank
        }
        // Retorna 'true' se o CPF for válido (não existe no banco)
        return !solicitacaoRepository.existsByCpfPaciente(cpf);
    }
}