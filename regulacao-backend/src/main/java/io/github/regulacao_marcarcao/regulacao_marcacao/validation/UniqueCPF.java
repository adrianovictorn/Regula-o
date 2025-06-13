package io.github.regulacao_marcarcao.regulacao_marcacao.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueCPFValidator.class)
@Documented
public @interface UniqueCPF {
    String message() default "CPF já cadastrado no sistema.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}