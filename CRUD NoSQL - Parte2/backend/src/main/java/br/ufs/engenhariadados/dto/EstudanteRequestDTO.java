package br.ufs.engenhariadados.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudanteRequestDTO {

    @NotBlank
    @Size(min = 11, max = 11)
    private String cpf;

    @NotBlank
    private String nome;

    @NotNull
    @Past
    private LocalDate dataNascimento;

    private List<String> email;
    private List<String> telefone;

    @NotBlank
    private String login;

    @NotBlank
    @Size(min = 6)
    private String senha;

    @NotBlank
    @Size(max = 7)
    private String matricula;

    @NotNull
    private Integer mc;

    @NotNull
    private Integer anoIngresso;

    @Valid   // valida os subobjetos
    private List<CursoVinculoDTO> cursos;
}