package br.ufs.engenhariadados.dto;

import br.ufs.engenhariadados.model.Vinculo;

import java.time.LocalDate;

public record VinculoResponseDTO(
        Integer idVinculo,
        String matriculaEstudante,
        String nomeEstudante,
        Integer cursoId,
        String nomeCurso,
        LocalDate dataEntrada,
        String status,
        LocalDate dataSaida
) {

    public static VinculoResponseDTO fromEntity(Vinculo vinculo) {
        return new VinculoResponseDTO(
                vinculo.getIdVinculo(),
                vinculo.getEstudante() == null ? null : vinculo.getEstudante().getMatricula(),
                vinculo.getEstudante() == null || vinculo.getEstudante().getUsuario() == null
                        ? null
                        : vinculo.getEstudante().getUsuario().getNome(),
                vinculo.getCurso() == null ? null : vinculo.getCurso().getIdCurso(),
                vinculo.getCurso() == null ? null : vinculo.getCurso().getNome(),
                vinculo.getDataEntrada(),
                vinculo.getStatus(),
                vinculo.getDataSaida()
        );
    }
}