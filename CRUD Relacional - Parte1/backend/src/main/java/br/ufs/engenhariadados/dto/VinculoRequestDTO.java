package br.ufs.engenhariadados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record VinculoRequestDTO(

        @NotBlank(message = "A matrícula do estudante é obrigatória.")
        @Size(max = 7, message = "A matrícula deve ter no máximo 7 caracteres.")
        String matriculaEstudante,

        @NotNull(message = "O ID do curso é obrigatório.")
        Integer cursoId,

        LocalDate dataEntrada,

        @NotBlank(message = "O status é obrigatório.")
        String status,

        LocalDate dataSaida
) {
}