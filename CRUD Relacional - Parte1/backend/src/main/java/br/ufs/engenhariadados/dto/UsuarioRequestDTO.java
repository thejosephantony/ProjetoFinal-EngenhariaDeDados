package br.ufs.engenhariadados.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UsuarioRequestDTO(

        @NotNull(message = "O CPF é obrigatório.")
        BigDecimal cpf,

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        LocalDate dataNascimento,

        List<@Email(message = "E-mail inválido.") String> email,

        List<@Size(max = 30, message = "O telefone deve ter no máximo 30 caracteres.") String> telefone,

        @Size(max = 45, message = "O login deve ter no máximo 45 caracteres.")
        String login,

        @Size(max = 32, message = "A senha deve ter no máximo 32 caracteres.")
        String senha
) {
}