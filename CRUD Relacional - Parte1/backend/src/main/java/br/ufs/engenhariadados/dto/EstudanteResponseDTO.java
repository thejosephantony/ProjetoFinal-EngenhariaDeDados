package br.ufs.engenhariadados.dto;

import br.ufs.engenhariadados.model.Estudante;

import java.math.BigDecimal;

public record EstudanteResponseDTO(
        String matricula,
        BigDecimal cpf,
        String nomeUsuario,
        BigDecimal mc,
        Integer anoIngresso
) {

    public static EstudanteResponseDTO fromEntity(Estudante estudante) {
        return new EstudanteResponseDTO(
                estudante.getMatricula(),
                estudante.getUsuario() == null ? null : estudante.getUsuario().getCpf(),
                estudante.getUsuario() == null ? null : estudante.getUsuario().getNome(),
                estudante.getMc(),
                estudante.getAnoIngresso()
        );
    }
}