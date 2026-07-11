package br.ufs.engenhariadados.model.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "estudantes")
public class EstudanteDocument {

    @Id
    private String id;

    @NotBlank
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
    @Indexed(unique = true)
    private String cpf;

    @NotBlank
    private String nome;

    @NotNull
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    private List<String> email;       // pode ser vazio

    private List<String> telefone;    // pode ser vazio

    @NotBlank
    private String login;

    @NotBlank
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotBlank
    @Size(max = 7, message = "Matrícula deve ter no máximo 7 caracteres")
    @Indexed(unique = true)
    private String matricula;

    @NotNull
    private Integer mc;               // média acumulada

    @NotNull
    private Integer anoIngresso;

    private List<CursoVinculo> cursos; // lista de cursos com vínculos (pode ser vazia)
}