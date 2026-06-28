package br.ufs.engenhariadados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EstudanteRequestDTO(

        @NotBlank(message = "A matrícula é obrigatória.")
        @Size(max = 7, message = "A matrícula deve ter no máximo 7 caracteres.")
        String matricula,

        BigDecimal cpf,

        BigDecimal mc,

        Integer anoIngresso
) {
}