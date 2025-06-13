package io.github.regulacao_marcarcao.regulacao_marcacao.entity.enums;

public enum ItemCategoria {
    ESPECIALIDADE_MEDICA("Especialidade Médica"),
    EXAME_OU_PROCEDIMENTO("Exame ou Procedimento");

    private final String displayValue; // Valor para exibição

    ItemCategoria(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}