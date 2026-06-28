package br.ufs.engenhariadados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CursoRequestDTO(

        @NotBlank(message = "O nome do curso é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        String grau,

        @NotBlank(message = "O turno é obrigatório.")
        String turno,

        @Size(max = 100, message = "O campus deve ter no máximo 100 caracteres.")
        String campus,

        String nivel
) {
}