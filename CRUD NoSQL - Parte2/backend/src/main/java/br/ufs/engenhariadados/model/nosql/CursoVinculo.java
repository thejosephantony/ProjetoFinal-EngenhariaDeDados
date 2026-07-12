package br.ufs.engenhariadados.model.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoVinculo {

    private String cursoId;
    private String nome;
    private String grau;        // Bacharelado, Tecnólogo, etc.
    private String turno;       // Matutino, Vespertino, Noturno
    private String campus;
    private String nivel;       // Graduação, Pós-graduação, etc.

    private Vinculo vinculo;    // dados do vínculo específico com este curso
}