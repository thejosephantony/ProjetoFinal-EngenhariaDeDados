package br.ufs.engenhariadados.dto;

import br.ufs.engenhariadados.model.Curso;

public record CursoResponseDTO(
        Integer idCurso,
        String nome,
        String grau,
        String turno,
        String campus,
        String nivel
) {

    public static CursoResponseDTO fromEntity(Curso curso) {
        return new CursoResponseDTO(
                curso.getIdCurso(),
                curso.getNome(),
                curso.getGrau(),
                curso.getTurno(),
                curso.getCampus(),
                curso.getNivel()
        );
    }
}