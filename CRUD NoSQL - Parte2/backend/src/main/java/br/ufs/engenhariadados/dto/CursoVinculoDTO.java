package br.ufs.engenhariadados.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoVinculoDTO {
    private String cursoId;
    private String nome;
    private String grau;
    private String turno;
    private String campus;
    private String nivel;
    private VinculoDTO vinculo;
}