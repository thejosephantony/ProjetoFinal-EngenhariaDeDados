package br.ufs.engenhariadados.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudanteResponseDTO {
    private String id;          // _id do MongoDB
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private List<String> email;
    private List<String> telefone;
    private String login;
    // senha omitida por segurança
    private String matricula;
    private Integer mc;
    private Integer anoIngresso;
    private List<CursoVinculoDTO> cursos;
}